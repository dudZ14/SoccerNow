package pt.ul.fc.css.soccernow.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import pt.ul.fc.css.soccernow.dto.JogadorDto;
import pt.ul.fc.css.soccernow.dto.JogadorDtoGet;
import pt.ul.fc.css.soccernow.entities.Equipa;
import pt.ul.fc.css.soccernow.entities.Jogador;
import pt.ul.fc.css.soccernow.entities.Jogo;
import pt.ul.fc.css.soccernow.enumerados.EstadoJogo;
import pt.ul.fc.css.soccernow.enumerados.Posicao;
import pt.ul.fc.css.soccernow.exceptions.EmailInvalidoException;
import pt.ul.fc.css.soccernow.exceptions.JogadorComJogoMarcadoException;
import pt.ul.fc.css.soccernow.exceptions.NomeVazioException;
import pt.ul.fc.css.soccernow.exceptions.PosicaoInvalidaException;
import pt.ul.fc.css.soccernow.repository.JogadorRepository;
import pt.ul.fc.css.soccernow.repository.JogoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JogadorHandler {

    @Autowired
    private JogadorRepository jogadorRepository;

    @Autowired
    private JogoRepository jogoRepository;

    public List<JogadorDtoGet> getAllJogadores() {
        return jogadorRepository.findAll()
                .stream()
                .map(JogadorHandler::toDtoGet)
                .collect(Collectors.toList());
    }

    public JogadorDtoGet createJogador(JogadorDto jogadorDto) {
        Jogador jogador = toEntity(jogadorDto);
        if (jogador.getNome().trim().equals("")) throw new NomeVazioException("O nome do jogador não pode ser vazio");
        if (!jogador.getEmail().contains("@")) throw new EmailInvalidoException("O email tem de ter @");
        Jogador savedJogador = jogadorRepository.save(jogador);
        return toDtoGet(savedJogador);
    }

    public JogadorDtoGet getJogadorById(Long id) {
        return jogadorRepository.findById(id)
                .map(JogadorHandler::toDtoGet)
                .orElse(null);
    }

    @Transactional
    public JogadorDtoGet deleteJogador(Long id) {
        return jogadorRepository.findById(id).map(jogador -> {
            // Supondo que tens um repository de jogos
        List<Jogo> jogosPorJogar = jogoRepository.findByEstado(EstadoJogo.POR_JOGAR);

        boolean jogadorTemJogoMarcado = jogosPorJogar.stream()
            .flatMap(jogo -> jogo.getJogadoresParticipantes().stream())
            .anyMatch(jnj -> jnj.getJogador().getId().equals(id)); // idDoJogador é o id do jogador a verificar

        if (jogadorTemJogoMarcado) {
            throw new JogadorComJogoMarcadoException("Não é possível eliminar o jogador, pois ele tem jogos por jogar.");
        }

            jogadorRepository.delete(jogador);
            return toDtoGet(jogador);
        }).orElse(null);
    }

    public JogadorDtoGet updateJogador(Long id, JogadorDto jogadorDto) {
        return jogadorRepository.findById(id).map(jogador -> {
            List<Jogo> jogosPorJogar = jogoRepository.findByEstado(EstadoJogo.POR_JOGAR);

            boolean jogadorTemJogoMarcado = jogosPorJogar.stream()
                .flatMap(jogo -> jogo.getJogadoresParticipantes().stream())
                .anyMatch(jnj -> jnj.getJogador().getId().equals(id)); // idDoJogador é o id do jogador a verificar

            if (jogadorTemJogoMarcado) {
                throw new JogadorComJogoMarcadoException("Não é possível atualizar o jogador, pois ele tem jogos por jogar.");
            }

            jogador.setNome(jogadorDto.getNome());
            jogador.setEmail(jogadorDto.getEmail());
            jogador.setPassword(jogadorDto.getPassword());
            jogador.setCertificado(jogadorDto.getTemCertificado());
            try {
                Posicao posicao = Posicao.valueOf(jogadorDto.getPosicaoPreferida().toUpperCase());
                jogador.setPosicaoPreferida(posicao);
            } catch (IllegalArgumentException e) {
                throw new PosicaoInvalidaException("Posição não válida. As posições válidas são: GUARDA_REDES, DEFESA, MEDIO e AVANCADO.");
            }

            Jogador updatedJogador = jogadorRepository.save(jogador);
            return toDtoGet(updatedJogador);
        }).orElse(null);
    }

    public double calcularMediaGolosPorJogo(String nomeJogador) {
        return jogadorRepository.calcularMediaGolosPorJogo(nomeJogador);
    }

    public List<JogadorDtoGet> getJogadoresComMaisCartoesVermelhos() {
        List<Jogador> jogadores = jogadorRepository.findJogadoresComMaisCartoesVermelhos();
        return jogadores.stream()
            .map(JogadorHandler::toDtoGet)
            .collect(Collectors.toList());
    }

    public List<JogadorDtoGet> getJogadoresPorNome(String nome) {
        List<Jogador> jogadores = jogadorRepository.findByNome(nome);
        return jogadores.stream()
            .map(JogadorHandler::toDtoGet)
            .collect(Collectors.toList());
    }

    public List<JogadorDtoGet> getJogadoresPorNumeroDeCartoes(int quantidade) {
        List<Jogador> jogadores = jogadorRepository.findByNumeroDeCartoes(quantidade);
        return jogadores.stream()
            .map(JogadorHandler::toDtoGet)
            .collect(Collectors.toList());
    }

    public List<JogadorDtoGet> getJogadoresPorNumeroDeGolos(int quantidade) {
        List<Jogador> jogadores = jogadorRepository.findByNumeroDeGolos(quantidade);
        return jogadores.stream()
            .map(JogadorHandler::toDtoGet)
            .collect(Collectors.toList());
    }

    public List<JogadorDtoGet> getJogadoresPorNumeroDeJogos(int quantidade) {
        List<Jogador> jogadores = jogadorRepository.findByNumJogos(quantidade);
        return jogadores.stream()
            .map(JogadorHandler::toDtoGet)
            .collect(Collectors.toList());
    }

    public List<JogadorDtoGet> getJogadoresPorPosicao(Posicao pos) {
        List<Jogador> jogadores = jogadorRepository.findJogadoresPorPosicaoPreferida(pos);
        return jogadores.stream()
            .map(JogadorHandler::toDtoGet)
            .collect(Collectors.toList());
    }


    public JogadorDto toDto(Jogador jogador) {
        return new JogadorDto(
            jogador.getNome(),
            jogador.getEmail(),
            jogador.getPassword(),
            jogador.getCertificado(),
            jogador.getPosicaoPreferida()
        );
    }

    public static JogadorDtoGet toDtoGet(Jogador jogador) {
        String nomesEquipas = jogador.getEquipas() != null ? 
            jogador.getEquipas().stream()
                .map(Equipa::getNome)
                .collect(Collectors.joining(", ")) // Junta os nomes com vírgula e espaço
            : "";
    
        int numeroGolos = jogador.getGolos() != null ? jogador.getGolos().size() : 0;
        int numeroCartoes = jogador.getCartoes() != null ? jogador.getCartoes().size() : 0;
    
        return new JogadorDtoGet(
            jogador.getId(),
            jogador.getNome(),
            jogador.getEmail(),
            jogador.getPassword(),
            jogador.getCertificado(),
            nomesEquipas, // Agora é uma string com os nomes das equipas separados por vírgula
            numeroGolos,
            numeroCartoes,
            jogador.getNumJogos(),
            jogador.getPosicaoPreferida()
        );
    }
    

    public Jogador toEntity(JogadorDto jogadorDto) {
        Jogador jogador = new Jogador();
        jogador.setNome(jogadorDto.getNome());
        jogador.setEmail(jogadorDto.getEmail());
        jogador.setPassword(jogadorDto.getPassword());
        jogador.setCertificado(jogadorDto.getTemCertificado());
        try {
        Posicao posicao = Posicao.valueOf(jogadorDto.getPosicaoPreferida().toUpperCase());
        jogador.setPosicaoPreferida(posicao);
        } catch (IllegalArgumentException e) {
            throw new PosicaoInvalidaException("Posição não válida. As posições válidas são: GUARDA_REDES, DEFESA, MEDIO e AVANCADO.");
        }
        return jogador;
    }
}
