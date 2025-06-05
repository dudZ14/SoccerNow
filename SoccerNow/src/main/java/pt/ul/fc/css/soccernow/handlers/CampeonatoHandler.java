package pt.ul.fc.css.soccernow.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.soccernow.dto.CampeonatoDto;
import pt.ul.fc.css.soccernow.dto.CampeonatoDtoGet;
import pt.ul.fc.css.soccernow.entities.Arbitro;
import pt.ul.fc.css.soccernow.entities.ArbitrosNoJogo;
import pt.ul.fc.css.soccernow.entities.Campeonato;
import pt.ul.fc.css.soccernow.entities.Equipa;
import pt.ul.fc.css.soccernow.entities.Jogo;
import pt.ul.fc.css.soccernow.enumerados.EstadoJogo;
import pt.ul.fc.css.soccernow.exceptions.CampeonatoInvalidoException;
import pt.ul.fc.css.soccernow.exceptions.NomeVazioException;
import pt.ul.fc.css.soccernow.repository.CampeonatoRepository;
import pt.ul.fc.css.soccernow.repository.EquipaRepository;
import pt.ul.fc.css.soccernow.repository.JogoRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CampeonatoHandler {

    @Autowired
    private CampeonatoRepository campeonatoRepository;

    @Autowired
    private EquipaRepository equipaRepository;

    @Autowired
    private JogoRepository jogoRepository;

    public CampeonatoDtoGet createCampeonato(CampeonatoDto campeonatoDto) {

        if (campeonatoDto.getNome() == null || campeonatoDto.getNome().trim().isEmpty()) {
            throw new NomeVazioException("O nome do campeonato não pode ser vazio");
        }

        if (campeonatoDto.getModalidade() == null || campeonatoDto.getModalidade().trim().isEmpty()) {
            throw new NomeVazioException("O nome da modalidade não pode ser vazio");
        }


        if (campeonatoDto.getEquipaIds() == null || campeonatoDto.getEquipaIds().size() < 8) {
            throw new CampeonatoInvalidoException("Um campeonato deve ter pelo menos 8 equipas.");
        }

        List<Equipa> equipas = equipaRepository.findAllById(campeonatoDto.getEquipaIds());
        if (equipas.size() < 8) {
            throw new CampeonatoInvalidoException("Tem equipas repetidas ou forneceu o id incorreto");
        }

        // Buscar jogos se existirem IDs
        List<Jogo> jogos = List.of();
        if (campeonatoDto.getJogoIds() != null && !campeonatoDto.getJogoIds().isEmpty()) {
            jogos = jogoRepository.findAllById(campeonatoDto.getJogoIds());
            if (jogos.size() != campeonatoDto.getJogoIds().size()) {
                throw new CampeonatoInvalidoException("Tem jogos repetidos ou forneceu o id incorreto");
            }
            if (jogos.stream().anyMatch(j -> j.getEstado() == EstadoJogo.ENCERRADO || j.getEstado() == EstadoJogo.CANCELADO)) {
                throw new CampeonatoInvalidoException("Os jogos do campeonato têm de estar por jogar");
            }
            jogos.forEach(j -> j.setCampeonato(null));
            verificarArbitrosCertificados(jogos); 
        }

        Campeonato campeonato = new Campeonato(
            campeonatoDto.getNome(),
            campeonatoDto.getModalidade(),
            equipas
        );
        campeonato.setJogos(jogos);


        // Ajustar a referência dos jogos ao campeonato 
        jogos.forEach(j -> j.setCampeonato(campeonato));

        Campeonato saved = campeonatoRepository.save(campeonato);
        return toDtoGet(saved);
    }

    public CampeonatoDtoGet updateCampeonato(Long id, CampeonatoDto campeonatoDto) {
        return campeonatoRepository.findById(id).map(campeonato -> {
            if (campeonatoDto.getNome() == null || campeonatoDto.getNome().trim().isEmpty()) {
                throw new NomeVazioException("O nome do campeonato não pode ser vazio");
            }

            if (campeonatoDto.getModalidade() == null || campeonatoDto.getModalidade().trim().isEmpty()) {
                throw new NomeVazioException("O nome da modalidade não pode ser vazio");
            }

            if (campeonato.terminou()) {
                throw new CampeonatoInvalidoException("Não é possível atualizar um campeonato que já terminou.");
            }

            campeonato.setNome(campeonatoDto.getNome());
            campeonato.setModalidade(campeonatoDto.getModalidade());

            if (campeonatoDto.getEquipaIds() != null) {
                List<Equipa> equipas = equipaRepository.findAllById(campeonatoDto.getEquipaIds());
                if (equipas.size() < 8) {
                    throw new CampeonatoInvalidoException("Um campeonato deve ter pelo menos 8 equipas.");
                }
                campeonato.setEquipas(equipas);
            }

            if (campeonatoDto.getJogoIds() != null) {
                List<Jogo> novosJogos = jogoRepository.findAllById(campeonatoDto.getJogoIds());

                if (novosJogos.size() != campeonatoDto.getJogoIds().size()) {
                    throw new CampeonatoInvalidoException("Alguns jogos não foram encontrados.");
                }

                verificarArbitrosCertificados(novosJogos);
                List<Jogo> jogosExistentes = campeonato.getJogos();

                // Dissociar jogos antigos
                for (Jogo jogo : jogosExistentes) {
                    jogo.setCampeonato(null);
                }

                // Limpar e associar os novos jogos
                jogosExistentes.clear();
                for (Jogo jogo : novosJogos) {
                    jogo.setCampeonato(campeonato);
                    jogosExistentes.add(jogo);
                }

            }


            Campeonato updated = campeonatoRepository.save(campeonato);
            return toDtoGet(updated);
        }).orElse(null);
    }


    // Obter todos os campeonatos
    public List<CampeonatoDtoGet> getAllCampeonatos() {
        return campeonatoRepository.findAll().stream()
                .map(CampeonatoHandler::toDtoGet)
                .collect(Collectors.toList());
    }

    // Obter campeonato por id
    public CampeonatoDtoGet getCampeonatoById(Long id) {
        return campeonatoRepository.findById(id)
                .map(CampeonatoHandler::toDtoGet)
                .orElse(null);
    }


    // Remover campeonato
    public CampeonatoDtoGet deleteCampeonato(Long id) {
        return campeonatoRepository.findById(id).map(campeonato -> {

            if (!campeonato.terminou()) {
                throw new CampeonatoInvalidoException("Não é possível eliminar um campeonato que ainda está em andamento.");
            }

            // Inicializar listas antes de apagar (força o Hibernate a carregar as coleções)
            campeonato.getJogos().size();
            campeonato.getEquipas().size();

            // Remover os jogos do histórico das equipas
            for (Jogo jogo : campeonato.getJogos()) {
                for (Equipa equipa : equipaRepository.findAll()) {
                    equipa.getHistorico().remove(jogo);
                }
            }

            equipaRepository.saveAll(equipaRepository.findAll());

            CampeonatoDtoGet dto = toDtoGet(campeonato); // criar DTO antes do delete
            campeonatoRepository.delete(campeonato);
            return dto;

        }).orElse(null);
    }



    public boolean cancelarJogo(Long campeonatoId, Long jogoId) {
        Optional<Campeonato> campeonatoOpt = campeonatoRepository.findById(campeonatoId);
        if (campeonatoOpt.isEmpty()) {
            return false; 
        }

        Campeonato campeonato = campeonatoOpt.get();

        boolean jogoPertence = campeonato.getJogos().stream()
                .anyMatch(j -> j.getId().equals(jogoId));

        if (!jogoPertence) {
            return false; 
        }

        Optional<Jogo> jogoOpt = jogoRepository.findById(jogoId);
        if (jogoOpt.isEmpty()) {
            return false; 
        }

        Jogo jogo = jogoOpt.get();

        if (jogo.getEstado() == EstadoJogo.ENCERRADO) {
            throw new CampeonatoInvalidoException("Um jogo já encerrado não pode ser cancelado");
        }

        if (jogo.getEstado() == EstadoJogo.CANCELADO) {
            return false;
        }

        jogo.setEstado(EstadoJogo.CANCELADO);
        jogoRepository.save(jogo);

        return true;
}

    private void verificarArbitrosCertificados(List<Jogo> jogos) {
        boolean algumInvalido = jogos.stream()
            .anyMatch(jogo -> {
                ArbitrosNoJogo anj = jogo.getArbitrosNoJogo();
                return anj == null ||
                    anj.getArbitros() == null ||
                    anj.getArbitros().stream().noneMatch(Arbitro::getCertificado);
            });
        if (algumInvalido) {
            throw new CampeonatoInvalidoException("Existe pelo menos um jogo que não tem arbitro certificado");
        }
    }

    public List<CampeonatoDtoGet> filtrarPorNome(String nome) {
        List<Campeonato> campeonatos = campeonatoRepository.findByNomeIgnoreCaseContaining(nome);
        return campeonatos.stream()
                .map(CampeonatoHandler::toDtoGet)
                .collect(Collectors.toList());
    }

    public List<CampeonatoDtoGet> filtrarPorEquipa(Long equipaId) {
        Equipa equipa = equipaRepository.findById(equipaId).orElse(null);
        if (equipa == null) {
            return null; 
        }
        List<Campeonato> campeonatos = campeonatoRepository.findByEquipa(equipa);
        return campeonatos.stream()
                .map(CampeonatoHandler::toDtoGet)
                .collect(Collectors.toList());
    }


    public List<CampeonatoDtoGet> filtrarPorMinJogosRealizados(long minJogos) {
        List<Campeonato> campeonatos = campeonatoRepository.findByJogosRealizados(minJogos);
        return campeonatos.stream()
                .map(CampeonatoHandler::toDtoGet)
                .collect(Collectors.toList());
    }

    public List<CampeonatoDtoGet> filtrarPorMinJogosPorRealizar(long minJogos) {
        List<Campeonato> campeonatos = campeonatoRepository.findByJogosPorRealizar(minJogos);
        return campeonatos.stream()
                .map(CampeonatoHandler::toDtoGet)
                .collect(Collectors.toList());
    }


    // Método para converter entity para DTO get
    public static CampeonatoDtoGet toDtoGet(Campeonato campeonato) {
        List<Long> equipaIds = campeonato.getEquipas() != null ?
                campeonato.getEquipas().stream()
                        .map(Equipa::getId)
                        .collect(Collectors.toList())
                : List.of();

        List<Long> jogoIds = campeonato.getJogos() != null ?
                campeonato.getJogos().stream()
                        .map(Jogo::getId)
                        .collect(Collectors.toList())
                : List.of();

        int totalJogos = campeonato.getJogos() != null ? campeonato.getJogos().size() : 0;

        int jogosFeitos = campeonato.getJogos() != null ?
            (int) campeonato.getJogos().stream()
                .filter(j -> j.getEstado() == EstadoJogo.ENCERRADO)
                .count()
            : 0;

        //ordenar por pontuacoes decrescente como na vida real
        Map<String, Integer> pontuacoes = campeonato.getPontuacoes().entrySet().stream()
        .collect(Collectors.toMap(
            e -> e.getKey().getNome(),
            Map.Entry::getValue,
            (e1, e2) -> e1,
            LinkedHashMap::new  
        ));


        return new CampeonatoDtoGet(
                campeonato.getId(),
                campeonato.getNome(),
                campeonato.getModalidade(),
                equipaIds,
                jogoIds,
                totalJogos,
                jogosFeitos,
                pontuacoes
        );
    }


}
