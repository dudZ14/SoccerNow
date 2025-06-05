package pt.ul.fc.css.soccernow.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.soccernow.dto.ArbitroDto;
import pt.ul.fc.css.soccernow.dto.ArbitroDtoGet;
import pt.ul.fc.css.soccernow.entities.Arbitro;
import pt.ul.fc.css.soccernow.entities.ArbitrosNoJogo;
import pt.ul.fc.css.soccernow.entities.Jogo;
import pt.ul.fc.css.soccernow.enumerados.EstadoJogo;
import pt.ul.fc.css.soccernow.exceptions.ArbitroComJogoMarcadoException;
import pt.ul.fc.css.soccernow.exceptions.EmailInvalidoException;
import pt.ul.fc.css.soccernow.exceptions.NomeVazioException;
import pt.ul.fc.css.soccernow.repository.ArbitroRepository;
import pt.ul.fc.css.soccernow.repository.JogoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArbitroHandler {

    @Autowired
    private ArbitroRepository arbitroRepository;

    @Autowired
    private JogoRepository jogoRepository;

    public List<ArbitroDtoGet> getAllArbitros() {
        return arbitroRepository.findAll()
                .stream()
                .map(this::toDtoGet)
                .collect(Collectors.toList());
    }

    public ArbitroDtoGet createArbitro(ArbitroDto arbitroDto) {
        Arbitro arbitro = toEntity(arbitroDto);
        if (arbitro.getNome().trim().equals("")) throw new NomeVazioException("O nome do árbitro não pode ser vazio");
        if (arbitro.getEmail().trim().equals("")) throw new NomeVazioException("O email do árbitro não pode ser vazio");
        if (arbitro.getPassword().trim().equals("")) throw new NomeVazioException("A password do árbitro não pode ser vazia");
        if (!arbitro.getEmail().contains("@")) throw new EmailInvalidoException("O email tem de ter @");
        Arbitro savedArbitro = arbitroRepository.save(arbitro);
        return toDtoGet(savedArbitro);
    }

    public ArbitroDtoGet getArbitroById(Long id) {
        return arbitroRepository.findById(id)
                .map(this::toDtoGet)
                .orElse(null);
    }

    public ArbitroDtoGet deleteArbitro(Long id) {
        return arbitroRepository.findById(id).map(arbitro -> {
            List<Jogo> jogosPorJogar = jogoRepository.findByEstado(EstadoJogo.POR_JOGAR);

            boolean arbitroTemJogoMarcado = jogosPorJogar.stream()
                .filter(jogo -> jogo.getArbitrosNoJogo() != null)
                .anyMatch(jogo -> {
                    ArbitrosNoJogo arbitrosNoJogo = jogo.getArbitrosNoJogo();

                    if (arbitrosNoJogo.getArbitros() != null) {
                        return arbitrosNoJogo.getArbitros().stream()
                            .anyMatch(arb -> arb.getId().equals(id)); 
                    }
                    return false;
                });

            if (arbitroTemJogoMarcado) {
                throw new ArbitroComJogoMarcadoException("Não é possível eliminar o árbitro, pois ele tem jogos por jogar.");
            }

            arbitroRepository.delete(arbitro);
            return toDtoGet(arbitro); 
        }).orElse(null);
    }

    public ArbitroDtoGet updateArbitro(Long id, ArbitroDto arbitroDto) {
        return arbitroRepository.findById(id).map(arbitro -> {
            List<Jogo> jogosPorJogar = jogoRepository.findByEstado(EstadoJogo.POR_JOGAR);

            boolean arbitroTemJogoMarcado = jogosPorJogar.stream()
                .filter(jogo -> jogo.getArbitrosNoJogo() != null)
                .anyMatch(jogo -> {
                    ArbitrosNoJogo arbitrosNoJogo = jogo.getArbitrosNoJogo();

                    if (arbitrosNoJogo.getArbitros() != null) {
                        return arbitrosNoJogo.getArbitros().stream()
                            .anyMatch(arb -> arb.getId().equals(id)); 
                    }
                    return false;
                });

            if (arbitroTemJogoMarcado) {
                throw new ArbitroComJogoMarcadoException("Não é possível atualizar o árbitro, pois ele tem jogos por jogar.");
            }
            
            if (arbitroDto.getNome().trim().equals("")) throw new NomeVazioException("O nome do árbitro não pode ser vazio");
            if (arbitroDto.getEmail().trim().equals("")) throw new NomeVazioException("O email do árbitro não pode ser vazio");
            if (arbitroDto.getPassword().trim().equals("")) throw new NomeVazioException("A password do árbitro não pode ser vazia");
            if (!arbitroDto.getEmail().contains("@")) throw new EmailInvalidoException("O email tem de ter @");
            arbitro.setNome(arbitroDto.getNome());
            arbitro.setEmail(arbitroDto.getEmail());
            arbitro.setPassword(arbitroDto.getPassword());
            arbitro.setCertificado(arbitroDto.getTemCertificado());
            arbitro.setAnosDeExperiencia(arbitroDto.getAnosDeExperiencia());
            Arbitro updatedArbitro = arbitroRepository.save(arbitro);
            return toDtoGet(updatedArbitro);
        }).orElse(null);
    }

    public List<ArbitroDtoGet> getArbitrosComMaisJogos() {
        List<Arbitro> arbitrosComMaisJogos = arbitroRepository.findArbitrosComMaisJogos();
        return arbitrosComMaisJogos.stream()
            .map(this::toDtoGet)
            .collect(Collectors.toList());
    }

    public List<ArbitroDtoGet> getArbitrosPorNome(String nome) {
        List<Arbitro> arbitros = arbitroRepository.findByNome(nome);
        return arbitros.stream()
            .map(this::toDtoGet)
            .collect(Collectors.toList());
    }
    
    public List<ArbitroDtoGet> getArbitrosPorJogosRealizados(int num) {
        List<Arbitro> arbitros = arbitroRepository.findArbitrosComNumeroDeJogosEncerrados(num);
        return arbitros.stream()
            .map(this::toDtoGet)
            .collect(Collectors.toList());
    }

    public List<ArbitroDtoGet> getArbitrosPorCartoesMostrados(int num) {
        List<Arbitro> arbitros = arbitroRepository.findArbitrosComNumeroDeCartoesMostrados(num);
        return arbitros.stream()
            .map(this::toDtoGet)
            .collect(Collectors.toList());
    }

    public ArbitroDto toDto(Arbitro arbitro) {
    return new ArbitroDto(
        arbitro.getNome(),
        arbitro.getEmail(),
        arbitro.getPassword(),
        arbitro.getCertificado(),
        arbitro.getAnosDeExperiencia()
        );
    }

    public ArbitroDtoGet toDtoGet(Arbitro arbitro) {
        return new ArbitroDtoGet(
            arbitro.getId(),
            arbitro.getNome(),
            arbitro.getEmail(),
            arbitro.getPassword(),
            arbitro.getCertificado(),
            arbitro.getAnosDeExperiencia(),
            arbitro.getNumJogos(),
            arbitro.getNumCartoesMostrados()
        );
    }

    public Arbitro toEntity(ArbitroDto arbitroDto) {
        Arbitro arbitro = new Arbitro();
        arbitro.setNome(arbitroDto.getNome());
        arbitro.setEmail(arbitroDto.getEmail());
        arbitro.setPassword(arbitroDto.getPassword());
        arbitro.setCertificado(arbitroDto.getTemCertificado());
        arbitro.setAnosDeExperiencia(arbitroDto.getAnosDeExperiencia());
        return arbitro;
    }
}
