package pt.ul.fc.css.soccernow.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.soccernow.dto.ConquistaDtoGet;
import pt.ul.fc.css.soccernow.dto.EquipaDto;
import pt.ul.fc.css.soccernow.dto.EquipaDtoGet;
import pt.ul.fc.css.soccernow.dto.JogadorDtoGet;
import pt.ul.fc.css.soccernow.entities.Equipa;
import pt.ul.fc.css.soccernow.entities.Estatistica;
import pt.ul.fc.css.soccernow.entities.Golo;
import pt.ul.fc.css.soccernow.entities.Jogador;
import pt.ul.fc.css.soccernow.entities.Jogo;
import pt.ul.fc.css.soccernow.enumerados.EstadoJogo;
import pt.ul.fc.css.soccernow.enumerados.Posicao;
import pt.ul.fc.css.soccernow.exceptions.EquipaComJogoMarcadoException;
import pt.ul.fc.css.soccernow.exceptions.NomeVazioException;
import pt.ul.fc.css.soccernow.repository.EquipaRepository;
import pt.ul.fc.css.soccernow.repository.JogadorRepository;
import pt.ul.fc.css.soccernow.repository.JogoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipaHandler {

    @Autowired
    private EquipaRepository equipaRepository;

    @Autowired
    private JogadorRepository jogadorRepository;

    @Autowired
    private JogoRepository jogoRepository;

    public List<EquipaDtoGet> getAllEquipas() {
        return equipaRepository.findAll().stream()
                .map(EquipaHandler::toDtoGet)
                .collect(Collectors.toList());
    }

    public EquipaDtoGet getEquipaById(Long id) {
        return equipaRepository.findById(id)
                .map(EquipaHandler::toDtoGet)
                .orElse(null);
    }

    public EquipaDtoGet createEquipa(EquipaDto equipaDto) {
        Equipa equipa = toEntity(equipaDto);
        if (equipa.getNome().trim().equals("")) throw new NomeVazioException("O nome da equipa não pode ser vazio");
        
        Equipa savedEquipa = equipaRepository.save(equipa);
        return toDtoGet(savedEquipa);
    }

    public EquipaDtoGet updateEquipa(Long id, EquipaDto equipaDto) {
        return equipaRepository.findById(id)
                .map(equipa -> {
                    List<Jogo> jogosPorJogar = jogoRepository.findByEstado(EstadoJogo.POR_JOGAR);
                    boolean equipaTemJogoMarcado = jogosPorJogar.stream().anyMatch(
                        jogo -> jogo.getEquipaCasa().getId().equals(id) || jogo.getEquipaVisitante().getId().equals(id)
                    );
            
                    if (equipaTemJogoMarcado) {
                        throw new EquipaComJogoMarcadoException("Não é possível atualizar a equipa, pois ela tem jogos por jogar.");
                    }

                    List<Jogador> novosJogadores = jogadorRepository.findAllById(equipaDto.getJogadoresIds());

                   // Remove jogadores antigos que não estão na nova lista
                    for (Jogador jogador : new ArrayList<>(equipa.getJogadores())) {
                        if (!novosJogadores.contains(jogador)) {
                            equipa.removeJogador(jogador); // método bidirecional
                        }
                    }

                    // Adiciona novos jogadores
                    for (Jogador jogador : novosJogadores) {
                        equipa.addJogador(jogador); // método bidirecional
                    }

                    // Atualiza o nome da equipa
                    equipa.setNome(equipaDto.getNome());

                    Equipa updatedEquipa = equipaRepository.save(equipa);
                    return toDtoGet(updatedEquipa);
                })
                .orElse(null);
    }

    public EquipaDtoGet deleteEquipa(Long id) {
        return equipaRepository.findById(id).map(equipa -> {
            // Verificar se tem jogos POR_JOGAR
            List<Jogo> jogosPorJogar = jogoRepository.findByEstado(EstadoJogo.POR_JOGAR);
            boolean equipaTemJogoMarcado = jogosPorJogar.stream().anyMatch(
                jogo -> jogo.getEquipaCasa().getId().equals(id) || jogo.getEquipaVisitante().getId().equals(id)
            );
    
            if (equipaTemJogoMarcado) {
                throw new EquipaComJogoMarcadoException("Não é possível eliminar a equipa, pois ela tem jogos por jogar.");
            }
    
            // Remover associações com jogadores (respeitando a bidirecionalidade)
            for (Jogador jogador : new ArrayList<>(equipa.getJogadores())) {
                jogador.removeEquipa(equipa); // remove da lista do jogador
            }
            equipa.getJogadores().clear(); // limpa a lista da equipa também, opcional

            // Remover referências de estatísticas  
            List<Jogo> todosOsJogos = jogoRepository.findByEstado(EstadoJogo.ENCERRADO);
            for (Jogo jogo : todosOsJogos) {
                Estatistica estatistica = jogo.getEstatistica();
                if (estatistica != null && equipa.equals(estatistica.getEquipaVencedora())) {
                    estatistica.setEquipaVencedora(null);
                }
                 for (Golo golo : estatistica.getGolos()) {
                    if (equipa.equals(golo.getEquipa())) {
                        golo.setEquipa(null);
                    }
                }
                if (equipa.equals(jogo.getEquipaCasa())) {
                    jogo.setEquipaCasa(null);
                }
                if (equipa.equals(jogo.getEquipaVisitante())) {
                    jogo.setEquipaVisitante(null);
                }
            }

            EquipaDtoGet equipaDto = toDtoGet(equipa);
            equipaRepository.delete(equipa);
            return equipaDto;
        }).orElse(null);
    }
    

    public List<EquipaDtoGet> getEquipasComMenosDeCincoJogadores() {
        return equipaRepository.findEquipasComMenosDeCincoJogadores().stream()
                .map(EquipaHandler::toDtoGet)
                .collect(Collectors.toList());
    }

    public List<EquipaDtoGet> getEquipasComMaisCartoes() {
        List<Equipa> equipasComMaisCartoes = equipaRepository.findEquipasComMaisCartoes();
        return equipasComMaisCartoes.stream()
                .map(EquipaHandler::toDtoGet)
                .collect(Collectors.toList());
    }

    public List<EquipaDtoGet> getEquipasComMaisVitorias() {
        List<Equipa> equipasComMaisVitorias = equipaRepository.findEquipasComMaisVitorias();
        return equipasComMaisVitorias.stream()
                .map(EquipaHandler::toDtoGet)
                .collect(Collectors.toList());
    }

    public List<EquipaDtoGet> getEquipasPorNome(String nome) {
        List<Equipa> equipas = equipaRepository.findByNome(nome);
        return equipas.stream()
                .map(EquipaHandler::toDtoGet)
                .collect(Collectors.toList());
    }

    public List<EquipaDtoGet> getEquipasPorNumeroDeJogadores(int num) {
        List<Equipa> equipas = equipaRepository.findByNumeroDeJogadores(num);
        return equipas.stream()
                .map(EquipaHandler::toDtoGet)
                .collect(Collectors.toList());
    }

    public List<EquipaDtoGet> getEquipasPorNumeroDeVitorias(int num) {
        List<Equipa> equipas = equipaRepository.findByNumeroDeVitorias(num);
        return equipas.stream()
                .map(EquipaHandler::toDtoGet)
                .collect(Collectors.toList());
    }

    public List<EquipaDtoGet> getEquipasPorNumeroDeEmpates(int num) {
        List<Equipa> equipas = equipaRepository.findByNumeroDeEmpates(num);
        return equipas.stream()
                .map(EquipaHandler::toDtoGet)
                .collect(Collectors.toList());
    }
    public List<EquipaDtoGet> getEquipasPorNumeroDeDerrotas(int num) {
        List<Equipa> equipas = equipaRepository.findByNumeroDeDerrotas(num);
        return equipas.stream()
                .map(EquipaHandler::toDtoGet)
                .collect(Collectors.toList());
    }

    public List<EquipaDtoGet> getEquipasPorNumeroDeConquistas(int num) {
        List<Equipa> equipas = equipaRepository.findByNumeroDeConquistas(num);
        return equipas.stream()
                .map(EquipaHandler::toDtoGet)
                .collect(Collectors.toList());
    }

    public List<EquipaDtoGet> getEquipasPorFaltaDeJogadorNaPosicao(Posicao pos) {
        List<Equipa> equipas = equipaRepository.findEquipasSemJogadorNaPosicao(pos);
        return equipas.stream()
                .map(EquipaHandler::toDtoGet)
                .collect(Collectors.toList());
    }

    public static EquipaDtoGet toDtoGet(Equipa equipa) {
        // Mapeia os jogadores para JogadorDtoGet
        List<JogadorDtoGet> jogadorDtos = equipa.getJogadores().stream()
                .map(JogadorHandler::toDtoGet) // método para mapear Jogador → JogadorDtoGet
                .collect(Collectors.toList());

        // Modificação para mapear a lista de jogos para uma lista de IDs
        List<Long> jogosDto = equipa.getHistorico() != null ? equipa.getHistorico().stream()
                .map(Jogo::getId) // Mapeia para o ID do jogo
                .collect(Collectors.toList()) : List.of();

        List<ConquistaDtoGet> conquistasDto = equipa.getConquistas().stream()
        .map(conquista -> new ConquistaDtoGet(
            conquista.getCampeonato().getId(),
            conquista.getCampeonato().getNome(),
            conquista.getPosicao()
        ))
        .collect(Collectors.toList());


        // Retorna a resposta com os dados da equipa
        return new EquipaDtoGet(
                equipa.getId(),
                equipa.getNome(),
                jogadorDtos,
                jogosDto,
                conquistasDto,
                equipa.getNumVitorias()
        );
    }

    private Equipa toEntity(EquipaDto equipaDto) {
        Equipa equipa = new Equipa();
        equipa.setNome(equipaDto.getNome());
        if (equipaDto.getJogadoresIds() != null) {
            // Busca os jogadores pelo ID e configura na equipa
            List<Jogador> jogadores = jogadorRepository.findAllById(equipaDto.getJogadoresIds());
            jogadores.forEach(jogador -> jogador.addEquipa(equipa)); // Configura a equipa para cada jogador
                                                                     // (bidirecional)
            equipa.setJogadores(jogadores);
        }
        return equipa;
    }
}