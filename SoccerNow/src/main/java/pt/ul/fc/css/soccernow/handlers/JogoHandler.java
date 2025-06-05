package pt.ul.fc.css.soccernow.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.soccernow.dto.*;
import pt.ul.fc.css.soccernow.entities.*;
import pt.ul.fc.css.soccernow.enumerados.EstadoJogo;
import pt.ul.fc.css.soccernow.enumerados.Posicao;
import pt.ul.fc.css.soccernow.enumerados.TipoCartao;
import pt.ul.fc.css.soccernow.exceptions.ArbitroConflitanteException;
import pt.ul.fc.css.soccernow.exceptions.EquipaConflitanteException;
import pt.ul.fc.css.soccernow.exceptions.EquipasIguaisException;
import pt.ul.fc.css.soccernow.exceptions.ImpossivelRegistarException;
import pt.ul.fc.css.soccernow.exceptions.JogadorEmAmbasEquipasException;
import pt.ul.fc.css.soccernow.exceptions.JogadorInvalidoException;
import pt.ul.fc.css.soccernow.exceptions.JogoConflitanteException;
import pt.ul.fc.css.soccernow.exceptions.NumeroGuardaRedesInvalidoException;
import pt.ul.fc.css.soccernow.exceptions.NumeroJogadoresInvalidoException;
import pt.ul.fc.css.soccernow.exceptions.PosicaoInvalidaException;
import pt.ul.fc.css.soccernow.repository.ArbitroRepository;
import pt.ul.fc.css.soccernow.repository.CampeonatoRepository;
import pt.ul.fc.css.soccernow.repository.EquipaRepository;
import pt.ul.fc.css.soccernow.repository.JogadorRepository;
import pt.ul.fc.css.soccernow.repository.JogoRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JogoHandler {

    @Autowired
    private JogoRepository jogoRepository;

    @Autowired
    private JogadorRepository jogadorRepository;

    @Autowired
    private ArbitroRepository arbitroRepository;

    @Autowired
    private EquipaRepository equipaRepository;

    @Autowired
    private CampeonatoRepository campeonatoRepository;

    public List<JogoDtoGet> getAllJogos() {
        return jogoRepository.findAll()
                .stream()
                .map(this::toDtoGet)
                .collect(Collectors.toList());
    }

    public JogoDtoGet createJogo(JogoDto jogoDto) {
        Jogo jogo = toEntity(jogoDto);
    
        validarPosicoesEEquipasJogadores(jogoDto, jogo);
        validarEquipasIguais(jogo);
        validarJogadoresEmAmbasEquipas(jogo);
        Map<Long, List<JogadorNoJogo>> jogadoresPorEquipa = validarJogadoresPorEquipa(jogo);
        validarNumeroJogadoresEGuardaRedes(jogadoresPorEquipa, jogo);
        validarConflitosDeJogo(jogo);
    
        Jogo savedJogo = jogoRepository.save(jogo);
        return toDtoGet(savedJogo);
    }
    

    public JogoDtoGet getJogoById(Long id) {
        return jogoRepository.findById(id)
                .map(this::toDtoGet)
                .orElse(null);
    }

    public JogoDtoGet deleteJogo(Long id) {
        return jogoRepository.findById(id).map(jogo -> {
            // Inicializa as coleções necessárias
            jogo.getJogadoresParticipantes().size(); 
            if (jogo.getArbitrosNoJogo() != null) {
                jogo.getArbitrosNoJogo().getArbitros().size(); 
            }
    
            // Converte para DTO antes de excluir
            JogoDtoGet jogoDto = toDtoGet(jogo);
    
            // Exclui o jogo
            jogoRepository.delete(jogo);
    
            return jogoDto;
        }).orElse(null);
    }

    public JogoDtoGet updateJogo(Long id, JogoDto jogoDto) {
        return jogoRepository.findById(id).map(jogo -> {
            // Atualiza o local
            jogo.setLocal(new Local(jogoDto.getLocal().getLocal(), jogoDto.getLocal().getDataHoraInicio(), jogoDto.getLocal().getDataHoraFim()));
    
            // Atualiza as equipas
            jogo.setEquipaCasa(equipaRepository.findById(jogoDto.getEquipaCasaId()).orElseThrow());
            jogo.setEquipaVisitante(equipaRepository.findById(jogoDto.getEquipaVisitanteId()).orElseThrow());
    
            // Atualiza os jogadores participantes
            if (jogoDto.getJogadoresNoJogo() != null) {
                List<JogadorNoJogo> novosJogadoresParticipantes = jogoDto.getJogadoresNoJogo().stream()
                    .map(jogadorNoJogoDto -> {
                        Jogador jogador = jogadorRepository.findById(jogadorNoJogoDto.getJogadorId())
                            .orElseThrow(() -> new NoSuchElementException("Jogador com ID " + jogadorNoJogoDto.getJogadorId() + " não encontrado."));
                        
                        Posicao posicao = Posicao.valueOf(jogadorNoJogoDto.getPosicao().toUpperCase());
    
                        JogadorNoJogo jogadorNoJogo = new JogadorNoJogo();
                        jogadorNoJogo.setJogador(jogador);
                        jogadorNoJogo.setJogo(jogo);
                        jogadorNoJogo.setPosicao(posicao);
                        jogadorNoJogo.setEquipa(jogador.getEquipas().stream()
                                                .filter(equipa -> equipa.equals(jogo.getEquipaCasa()) || equipa.equals(jogo.getEquipaVisitante()))
                                                .findFirst()
                                                .orElseThrow(() -> new JogadorInvalidoException("Todos os jogadores devem pertencer à equipa da casa ou visitante."))
                                                );

                        return jogadorNoJogo;
                    })
                    .collect(Collectors.toList());
    
                jogo.getJogadoresParticipantes().clear(); // Limpa os jogadores existentes
                jogo.getJogadoresParticipantes().addAll(novosJogadoresParticipantes); // Adiciona os novos jogadores
            }
    
            // Atualiza os árbitros no jogo
            if (jogoDto.getArbitrosNoJogo() != null) {
                ArbitrosNoJogoDto arbitrosNoJogoDto = jogoDto.getArbitrosNoJogo();
    
                List<Arbitro> novosArbitros = arbitrosNoJogoDto.getArbitrosIds().stream()
                    .map(arbitroId -> arbitroRepository.findById(arbitroId)
                        .orElseThrow(() -> new NoSuchElementException("Árbitro com ID " + arbitroId + " não encontrado.")))
                    .collect(Collectors.toList());
    
                Arbitro novoArbitroPrincipal = arbitroRepository.findById(arbitrosNoJogoDto.getArbitroPrincipalId())
                    .orElseThrow(() -> new NoSuchElementException("Árbitro principal com ID " + arbitrosNoJogoDto.getArbitroPrincipalId() + " não encontrado."));
    
                ArbitrosNoJogo arbitrosNoJogo = jogo.getArbitrosNoJogo();
                if (arbitrosNoJogo == null) {
                    arbitrosNoJogo = new ArbitrosNoJogo();
                    arbitrosNoJogo.setJogo(jogo); // Configura a relação bidirecional
                    jogo.setArbitrosNoJogo(arbitrosNoJogo);
                }
    
                arbitrosNoJogo.getArbitros().clear(); // Limpa os árbitros existentes
                arbitrosNoJogo.getArbitros().addAll(novosArbitros); // Adiciona os novos árbitros
                arbitrosNoJogo.setArbitroPrincipal(novoArbitroPrincipal); // Atualiza o árbitro principal
            }
    
            // Salva as alterações no banco de dados
            Jogo updatedJogo = jogoRepository.save(jogo);
            return toDtoGet(updatedJogo);
        }).orElse(null);
    }

    public JogoDtoGet registrarResultado(Long id, EstatisticaDto estatisticaDto) {
        return jogoRepository.findById(id).map(jogo -> {
            Estatistica estatistica = new Estatistica();

            if (jogo.getEstado() == EstadoJogo.ENCERRADO) {
                throw new ImpossivelRegistarException("Não é possível registar o resultado de um jogo encerrado");
            }

            if (jogo.getEstado() == EstadoJogo.CANCELADO) {
                throw new ImpossivelRegistarException("Não é possível registar o resultado de um jogo cancelado");
            }

            // Calcula o placar dinamicamente
            int golosCasa = (int) estatisticaDto.getGolos().stream()
                    .filter(golo -> {
                        Jogador jogador = jogadorRepository.findById(golo.getJogadorId()).orElseThrow();
                        return jogador.getEquipas().stream()
                                    .anyMatch(equipa -> equipa.equals(jogo.getEquipaCasa()));
                    })
                    .count();

            int golosVisitante = (int) estatisticaDto.getGolos().stream()
                    .filter(golo -> {
                        Jogador jogador = jogadorRepository.findById(golo.getJogadorId()).orElseThrow();
                        return jogador.getEquipas().stream()
                                    .anyMatch(equipa -> equipa.equals(jogo.getEquipaVisitante()));
                    })
                    .count();

            String resultado = golosCasa + "-" + golosVisitante;
            estatistica.setResultado(resultado);

            if (golosCasa > golosVisitante) {
                estatistica.setEquipaVencedora(jogo.getEquipaCasa());
            } else if (golosCasa < golosVisitante) {
                estatistica.setEquipaVencedora(jogo.getEquipaVisitante());
            } else {
                estatistica.setEquipaVencedora(null); // Empate
            }

            // Atualiza o número de vitórias da equipa vencedora
            if (estatistica.getEquipaVencedora() != null) {
                Equipa equipaVencedora = estatistica.getEquipaVencedora();
                equipaVencedora.incrementarNumVitorias();
                equipaRepository.save(equipaVencedora); // salva no repositório
            }


            estatistica.setGolos(
                estatisticaDto.getGolos().stream()
                    .map(goloDto -> {
                        Golo golo = new Golo(
                            jogadorRepository.findById(goloDto.getJogadorId()).orElseThrow(),
                            jogadorRepository.findById(goloDto.getJogadorId())
                                .map(jogador -> jogador.getEquipas().stream()
                                    .filter(equipa -> equipa.equals(jogo.getEquipaCasa()) || equipa.equals(jogo.getEquipaVisitante()))
                                    .findFirst()
                                    .orElseThrow())  // Caso o jogador não tenha uma das equipes, lança uma exceção
                                .orElseThrow(),
                            goloDto.getMinuto()
                        );
                        golo.setEstatistica(estatistica); // Configurar a relação bidirecional
                        return golo;
                    })
                    .collect(Collectors.toList())
            );
            estatistica.setCartoes(
                estatisticaDto.getCartoes().stream()
                    .map(cartaoDto -> {
                        CartaoJogador cartao = new CartaoJogador(
                            jogadorRepository.findById(cartaoDto.getJogadorId()).orElseThrow(),
                            TipoCartao.valueOf(cartaoDto.getTipoCartao())
                        );
                        cartao.setEstatistica(estatistica); // Configurar a relação bidirecional
                        return cartao;
                    })
                    .collect(Collectors.toList())
            );
    
            jogo.setEstatistica(estatistica);
            jogo.setEstado(EstadoJogo.ENCERRADO);

            Campeonato campeonato = jogo.getCampeonato();
            if (campeonato != null) {
                Map<Equipa, Integer> pontuacoes = campeonato.getPontuacoes();

                Equipa casa = jogo.getEquipaCasa();
                Equipa visitante = jogo.getEquipaVisitante();

                int pontosCasa = pontuacoes.getOrDefault(casa, 0);
                int pontosVisitante = pontuacoes.getOrDefault(visitante, 0);

                if (golosCasa > golosVisitante) {
                    pontuacoes.put(casa, pontosCasa + 3);
                } else if (golosCasa < golosVisitante) {
                    pontuacoes.put(visitante, pontosVisitante + 3);
                } else {
                    pontuacoes.put(casa, pontosCasa + 1);
                    pontuacoes.put(visitante, pontosVisitante + 1);
                }

                campeonato.setPontuacoes(pontuacoes); // redundante, mas seguro
                atualizarConquistasSeUltimoJogo(campeonato, jogo);

                campeonatoRepository.save(campeonato);
            }

    
            // Incrementa o número de jogos dos jogadores participantes
            jogo.getJogadoresParticipantes().forEach(jogadorNoJogo -> {
                Jogador jogador = jogadorNoJogo.getJogador();
                jogador.incrementarNumJogos();
                jogadorRepository.save(jogador); // Salva a atualização no banco de dados
            });
    
            // Incrementa o número de jogos dos árbitros
            jogo.getArbitrosNoJogo().getArbitros().forEach(arbitro -> {
                arbitro.incrementarNumJogos();

                if(arbitro.getId() == jogo.getArbitrosNoJogo().getArbitroPrincipal().getId()) {
                    int totalCartoes = estatisticaDto.getCartoes().size(); 
                    arbitro.incrementarNumCartoesMostrados(totalCartoes);
                }

                arbitroRepository.save(arbitro); // Salva a atualização no banco de dados
            });


            // Adiciona o jogo ao histórico das equipas
            jogo.getEquipaCasa().addJogoHistorico(jogo);
            jogo.getEquipaVisitante().addJogoHistorico(jogo);
            equipaRepository.save(jogo.getEquipaCasa());
            equipaRepository.save(jogo.getEquipaVisitante());

            Jogo updatedJogo = jogoRepository.save(jogo);
            return toDtoGet(updatedJogo);
        }).orElse(null);
    }

    private void atualizarConquistasSeUltimoJogo(Campeonato campeonato, Jogo jogo) {
        boolean ultimoJogo = campeonato.getJogos().stream()
            .allMatch(j -> j.getEstado() == EstadoJogo.ENCERRADO || j.getId().equals(jogo.getId()));

        if (ultimoJogo) {
            List<Map.Entry<Equipa, Integer>> topEquipas = campeonato.getPontuacoes().entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(3)
                .toList();

            for (int i = 0; i < topEquipas.size(); i++) {
                Equipa equipa = topEquipas.get(i).getKey();
                int posicao = i + 1;

                Conquista conquista = new Conquista();
                conquista.setCampeonato(campeonato);
                conquista.setPosicao(posicao);
                conquista.setEquipa(equipa);

                equipa.getConquistas().add(conquista);
                equipaRepository.save(equipa);
            }
        }
    }


    public EstatisticaDtoGet getResultado(Long id) {
        Jogo jogo = jogoRepository.findById(id).orElse(null);
        Estatistica estatistica = jogo.getEstatistica();
        if (estatistica == null) return null;
    
        List<GoloDto> golos = estatistica.getGolos().stream()
            .map(golo -> new GoloDto(
                golo.getJogador().getId(),
                golo.getMinuto()
            ))
            .collect(Collectors.toList());
    
        List<CartaoJogadorDto> cartoes = estatistica.getCartoes().stream()
            .map(cartao -> new CartaoJogadorDto(
                cartao.getJogador().getId(),
                cartao.getTipoCartao().name()
            ))
            .collect(Collectors.toList());
    
        Long equipaVencedoraId = null;
        if (estatistica.getEquipaVencedora() != null) {
            equipaVencedoraId = estatistica.getEquipaVencedora().getId();
        }
        
        return new EstatisticaDtoGet(
            estatistica.getId(),
            golos,
            cartoes,
            equipaVencedoraId,
            estatistica.getResultado()
        );
    }

    public List<JogoDtoGet> getJogoRealizados() {
        return jogoRepository.findJogosRealizados()
                .stream()
                .map(this::toDtoGet)
                .collect(Collectors.toList());
    }

    public List<JogoDtoGet> getJogoPorRealizar() {
        return jogoRepository.findJogosPorRealizar()
                .stream()
                .map(this::toDtoGet)
                .collect(Collectors.toList());
    }

    public List<JogoDtoGet> getJogoPorQuantidadeDeGolos(int num) {
        return jogoRepository.findJogosPorQuantidadeDeGolos(num)
                .stream()
                .map(this::toDtoGet)
                .collect(Collectors.toList());
    }

    public List<JogoDtoGet> getJogoPorLocal(String local) {
        return jogoRepository.findJogosPorNomeLocal(local)
                .stream()
                .map(this::toDtoGet)
                .collect(Collectors.toList());
    }

    public List<JogoDtoGet> getJogoTurnoDaManha() {
        return jogoRepository.findJogosTurnoManha()
                .stream()
                .map(this::toDtoGet)
                .collect(Collectors.toList());
    }

    public List<JogoDtoGet> getJogoTurnoDaTarde() {
        return jogoRepository.findJogosTurnoTarde()
                .stream()
                .map(this::toDtoGet)
                .collect(Collectors.toList());
    }

    public List<JogoDtoGet> getJogoTurnoDaNoite() {
        return jogoRepository.findJogosTurnoNoite()
                .stream()
                .map(this::toDtoGet)
                .collect(Collectors.toList());
    }
    

    public static JogoDto toDto(Jogo jogo) {
        LocalDto localDto = new LocalDto(
            jogo.getLocal().getLocal(),
            jogo.getLocal().getDataHoraInicio(),
            jogo.getLocal().getDataHoraFim()
        );
    
        // Mapeia os jogadores no jogo para JogadorNoJogoDto
        List<JogadorNoJogoDto> jogadoresNoJogo = jogo.getJogadoresParticipantes().stream()
            .map(jogadorNoJogo -> new JogadorNoJogoDto(
                jogadorNoJogo.getJogador().getId(),
                jogadorNoJogo.getPosicao().name()
            ))
            .collect(Collectors.toList());
    
        // Mapeia os árbitros no jogo para ArbitrosNoJogoDto
        ArbitrosNoJogoDto arbitrosNoJogoDto = null;
        if (jogo.getArbitrosNoJogo() != null) {
            arbitrosNoJogoDto = new ArbitrosNoJogoDto(
                jogo.getArbitrosNoJogo().getArbitros().stream()
                    .map(Arbitro::getId)
                    .collect(Collectors.toList()),
                jogo.getArbitrosNoJogo().getArbitroPrincipal().getId()
            );
        }
    
        return new JogoDto(
            localDto,
            jogo.getEquipaCasa().getId(),
            jogo.getEquipaVisitante().getId(),
            jogadoresNoJogo,
            arbitrosNoJogoDto // Adiciona os árbitros no jogo
        );
    }

    public JogoDtoGet toDtoGet(Jogo jogo) {
        if (jogo == null) {
            return null;  // Retorna null se o jogo for nulo
        }
    
        // Utiliza o método toDto para mapear o jogo para JogoDto
        JogoDto jogoDto = toDto(jogo);
        
        // Atribui o estado do jogo
        EstadoJogo estado = jogo.getEstado();
    
        // Retorna o JogoDtoGet, incluindo a estatística e o estado
        return new JogoDtoGet(
            jogo.getId(),
            jogoDto.getLocal(),
            jogoDto.getEquipaCasaId(),
            jogoDto.getEquipaVisitanteId(),
            jogoDto.getJogadoresNoJogo(),
            jogoDto.getArbitrosNoJogo(),
            estado
        );
    }
    

    public Jogo toEntity(JogoDto jogoDto) {
        Local local = new Local(
            jogoDto.getLocal().getLocal(),
            jogoDto.getLocal().getDataHoraInicio(),
            jogoDto.getLocal().getDataHoraFim()
        );
    
        Jogo jogo = new Jogo();
        jogo.setLocal(local);
    
        Equipa equipaCasa = equipaRepository.findById(jogoDto.getEquipaCasaId())
            .orElseThrow(() -> new NoSuchElementException("Equipa casa com ID " + jogoDto.getEquipaCasaId() + " não encontrada."));
        jogo.setEquipaCasa(equipaCasa);
    
        Equipa equipaVisitante = equipaRepository.findById(jogoDto.getEquipaVisitanteId())
            .orElseThrow(() -> new NoSuchElementException("Equipa visitante com ID " + jogoDto.getEquipaVisitanteId() + " não encontrada."));
        jogo.setEquipaVisitante(equipaVisitante);
    
        if (jogoDto.getJogadoresNoJogo() != null) {
            List<JogadorNoJogo> jogadoresParticipantes = jogoDto.getJogadoresNoJogo().stream()
                .map(dto -> {
                    Jogador jogador = jogadorRepository.findById(dto.getJogadorId())
                        .orElseThrow(() -> new NoSuchElementException("Jogador com ID " + dto.getJogadorId() + " não encontrado."));
                    
                    JogadorNoJogo jpj = new JogadorNoJogo();
                    jpj.setJogador(jogador);
                    jpj.setJogo(jogo);
                    jpj.setEquipa(null); // será preenchida depois da validação
                    jpj.setPosicao(null); // idem
                    return jpj;
                })
                .collect(Collectors.toList());
            jogo.setJogadoresParticipantes(jogadoresParticipantes);
        }
    
        if (jogoDto.getArbitrosNoJogo() != null) {
            ArbitrosNoJogoDto dto = jogoDto.getArbitrosNoJogo();
    
            List<Arbitro> arbitros = dto.getArbitrosIds().stream()
                .map(id -> arbitroRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Árbitro com ID " + id + " não encontrado.")))
                .collect(Collectors.toList());
    
            Arbitro principal = arbitroRepository.findById(dto.getArbitroPrincipalId())
                .orElseThrow(() -> new NoSuchElementException("Árbitro principal com ID " + dto.getArbitroPrincipalId() + " não encontrado."));
    
            ArbitrosNoJogo arbitrosNoJogo = new ArbitrosNoJogo(arbitros, principal);
            arbitrosNoJogo.setJogo(jogo);
            jogo.setArbitrosNoJogo(arbitrosNoJogo);
        }
    
        return jogo;
    }

    private void validarPosicoesEEquipasJogadores(JogoDto jogoDto, Jogo jogo) {
        for (int i = 0; i < jogoDto.getJogadoresNoJogo().size(); i++) {
            JogadorNoJogoDto dto = jogoDto.getJogadoresNoJogo().get(i);
            JogadorNoJogo jpj = jogo.getJogadoresParticipantes().get(i);
    
            try {
                Posicao posicao = Posicao.valueOf(dto.getPosicao().toUpperCase());
                jpj.setPosicao(posicao);
            } catch (IllegalArgumentException e) {
                throw new PosicaoInvalidaException("Posição não válida, as posiçoes válidas são: GUARDA_REDES, DEFESA, MEDIO e AVANCADO");
            }
    
            Equipa equipa = jpj.getJogador().getEquipas().stream()
                .filter(e -> e.equals(jogo.getEquipaCasa()) || e.equals(jogo.getEquipaVisitante()))
                .findFirst()
                .orElseThrow(() -> new JogadorInvalidoException("Todos os jogadores devem pertencer à equipa da casa ou visitante."));
            
            jpj.setEquipa(equipa);
        }
    }
    
    private void validarEquipasIguais(Jogo jogo) {
        if (jogo.getEquipaCasa().getId().equals(jogo.getEquipaVisitante().getId())) {
            throw new EquipasIguaisException("Uma equipa não pode jogar contra si própria.");
        }
    }
    
    private void validarJogadoresEmAmbasEquipas(Jogo jogo) {
        Set<Jogador> jogadoresCasa = new HashSet<>(jogo.getEquipaCasa().getJogadores());
        Set<Jogador> jogadoresVisitante = new HashSet<>(jogo.getEquipaVisitante().getJogadores());
    
        jogadoresCasa.retainAll(jogadoresVisitante);
        if (!jogadoresCasa.isEmpty()) {
            throw new JogadorEmAmbasEquipasException("Um ou mais jogadores pertencem tanto à equipa da casa como à visitante.");
        }
    }
    
    private Map<Long, List<JogadorNoJogo>> validarJogadoresPorEquipa(Jogo jogo) {
        Map<Long, List<JogadorNoJogo>> jogadoresPorEquipa = new HashMap<>();
        for (JogadorNoJogo jpj : jogo.getJogadoresParticipantes()) {
            Jogador jogador = jpj.getJogador();
            Long idEquipa = jogador.getEquipas().stream()
                .filter(equipa -> equipa.equals(jogo.getEquipaCasa()) || equipa.equals(jogo.getEquipaVisitante()))
                .findFirst()
                .map(Equipa::getId)
                .orElseThrow(() -> new JogadorInvalidoException("Todos os jogadores devem pertencer à equipa da casa ou visitante."));
    
            jogadoresPorEquipa.computeIfAbsent(idEquipa, k -> new ArrayList<>()).add(jpj);
        }
        return jogadoresPorEquipa;
    }
    
    private void validarNumeroJogadoresEGuardaRedes(Map<Long, List<JogadorNoJogo>> jogadoresPorEquipa, Jogo jogo) {
        for (Long idEquipa : List.of(jogo.getEquipaCasa().getId(), jogo.getEquipaVisitante().getId())) {
            List<JogadorNoJogo> jogadores = jogadoresPorEquipa.getOrDefault(idEquipa, List.of());
    
            if (jogadores.size() != 5) {
                throw new NumeroJogadoresInvalidoException("Cada equipa deve ter exatamente 5 jogadores.");
            }
    
            long numGoleiros = jogadores.stream()
                .filter(j -> j.getPosicao() == Posicao.GUARDA_REDES)
                .count();
    
            if (numGoleiros != 1) {
                throw new NumeroGuardaRedesInvalidoException("Cada equipa deve ter exatamente 1 guarda-redes.");
            }
        }
    }
    
    private void validarConflitosDeJogo(Jogo jogo) {
        List<Jogo> jogosPorJogar = jogoRepository.findByEstado(EstadoJogo.POR_JOGAR);
        for (Jogo jogoExistente : jogosPorJogar) {
            if (jogoExistente.getLocal().intersetaCom(jogo.getLocal())) {
                throw new JogoConflitanteException("Já existe um jogo neste local e horário.");
            }
    
            boolean equipaEnvolvida =
                jogoExistente.getEquipaCasa().getId().equals(jogo.getEquipaCasa().getId()) ||
                jogoExistente.getEquipaCasa().getId().equals(jogo.getEquipaVisitante().getId()) ||
                jogoExistente.getEquipaVisitante().getId().equals(jogo.getEquipaCasa().getId()) ||
                jogoExistente.getEquipaVisitante().getId().equals(jogo.getEquipaVisitante().getId());
    
            boolean horarioInterseta = jogoExistente.getLocal().horariosIntersetam(jogo.getLocal());
    
            if (equipaEnvolvida && horarioInterseta) {
                throw new EquipaConflitanteException("Uma das equipas já tem um jogo marcado nesse horário.");
            }
    
            if (jogo.getArbitrosNoJogo() != null && jogoExistente.getArbitrosNoJogo() != null) {
                List<Long> arbitrosNovosIds = jogo.getArbitrosNoJogo().getArbitros().stream()
                    .map(Arbitro::getId)
                    .collect(Collectors.toList());
    
                List<Long> arbitrosExistentesIds = jogoExistente.getArbitrosNoJogo().getArbitros().stream()
                    .map(Arbitro::getId)
                    .collect(Collectors.toList());
    
                boolean arbitroConflitante = arbitrosNovosIds.stream()
                    .anyMatch(arbitrosExistentesIds::contains);
    
                if (arbitroConflitante && horarioInterseta) {
                    throw new ArbitroConflitanteException("Um dos árbitros já está atribuido para outro jogo nesse horário.");
                }
            }
        }
    }
    
    
}