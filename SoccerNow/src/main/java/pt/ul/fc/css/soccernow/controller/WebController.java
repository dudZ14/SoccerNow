package pt.ul.fc.css.soccernow.controller;

import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import pt.ul.fc.css.soccernow.dto.ArbitroDtoGet;
import pt.ul.fc.css.soccernow.dto.JogoVista;
import pt.ul.fc.css.soccernow.dto.CampeonatoDtoGet;
import pt.ul.fc.css.soccernow.dto.EquipaDtoGet;
import pt.ul.fc.css.soccernow.dto.EquipaPontuacaoDto;
import pt.ul.fc.css.soccernow.dto.EstatisticaDto;
import pt.ul.fc.css.soccernow.dto.JogadorDtoGet;
import pt.ul.fc.css.soccernow.dto.JogoDtoGet;
import pt.ul.fc.css.soccernow.enumerados.Posicao;

@Controller
public class WebController {
    
    Logger logger = LoggerFactory.getLogger(WebController.class);

    @Autowired
    private JogadorController jogadorController;

    @Autowired
    private ArbitroController arbitroController;

    @Autowired
    private EquipaController equipaController;

    @Autowired
    private CampeonatoController campeonatoController;

    @Autowired
    private JogoController jogoController;

    public WebController() {
        super();
    }

    @GetMapping("/")
    public String home() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {
        if (!email.contains("@")) {
            model.addAttribute("error", "Email deve ter @");
            return "login";
        }

        // Mock de autenticação (qualquer senha aceita)
        session.setAttribute("user", email);
        model.addAttribute("username", email);
        return "layout";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "login";
    }

    @GetMapping({"/jogadores"})
    public String jogadores(final Model model) {
        logger.info("Jogadores retornados: " + jogadorController.getAllJogadores().getBody());
        model.addAttribute("jogadores", jogadorController.getAllJogadores().getBody());
        return "busca_jogadores";
    }

    @GetMapping({"/equipas"})
    public String equipas(final Model model) {
        List<EquipaDtoGet> equipas = equipaController.getAllEquipas().getBody();
        List<JogoDtoGet> jogos = jogoController.getJogosRealizados().getBody();

        Map<Long, Map<Long, String>> mapaJogos = criarMapaDescricaoJogos(jogos, equipas);
        atualizarHistoricoEquipas(equipas, mapaJogos);

        model.addAttribute("equipas", equipas);
        return "busca_equipas";
    }

    private Map<Long, Map<Long, String>> criarMapaDescricaoJogos(List<JogoDtoGet> jogos, List<EquipaDtoGet> equipas) {
        Map<Long, Map<Long, String>> mapa = new HashMap<>();

        for (JogoDtoGet jogo : jogos) {
            Long casaId = jogo.getEquipaCasaId();
            Long visitanteId = jogo.getEquipaVisitanteId();
            String data = jogo.getLocal().getDataHoraInicio().toLocalDate().toString();

            String nomeCasa = getNomeEquipa(casaId, equipas);
            String nomeVisitante = getNomeEquipa(visitanteId, equipas);

            Map<Long, String> porEquipa = new HashMap<>();
            porEquipa.put(casaId, String.format("(%s) vs %s", data, nomeVisitante));
            porEquipa.put(visitanteId, String.format("(%s) vs %s", data, nomeCasa));

            mapa.put(jogo.getId(), porEquipa);
        }

        return mapa;
    }

    private void atualizarHistoricoEquipas(List<EquipaDtoGet> equipas, Map<Long, Map<Long, String>> mapaJogos) {
        for (EquipaDtoGet equipa : equipas) {
            Long equipaId = equipa.getId();

            List<String> descricoesHistorico = equipa.getHistoricoJogosIds().stream()
                .map(jogoId -> {
                    Map<Long, String> porEquipa = mapaJogos.get(jogoId);
                    return porEquipa != null ? porEquipa.get(equipaId) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            equipa.setHistoricoFormatado(descricoesHistorico);
        }
    }

    private String getNomeEquipa(Long id, List<EquipaDtoGet> equipas) {
        return equipas.stream()
            .filter(e -> e.getId().equals(id))
            .map(EquipaDtoGet::getNome)
            .findFirst()
            .orElse("Desconhecida");
    }


    @GetMapping({"/campeonatos"})
    public String campeonatos(final Model model) {
        List<CampeonatoDtoGet> campeonatos = campeonatoController.getAllCampeonatos().getBody();
        List<EquipaDtoGet> todasEquipas = equipaController.getAllEquipas().getBody();

            for (CampeonatoDtoGet c : campeonatos) {
                List<String> nomesEquipas = c.getEquipaIds().stream()
                    .map(id -> getNomeEquipaById(id, todasEquipas))
                    .collect(Collectors.toList());

                 Map<String, Integer> pontuacoesOriginais = c.getPontuacoes();

                List<EquipaPontuacaoDto> listaPontuacoes = new ArrayList<>();

                for (int i = 0; i < c.getEquipaIds().size(); i++) {
                    String nomeEquipa = nomesEquipas.get(i);

                    Integer pontos = 0;
                    if (pontuacoesOriginais != null) {
                        pontos = pontuacoesOriginais.getOrDefault(nomeEquipa, 0);
                    }

                    listaPontuacoes.add(new EquipaPontuacaoDto(nomeEquipa, pontos));
                }
                listaPontuacoes.sort((a, b) -> b.getPontuacao().compareTo(a.getPontuacao()));

                c.setEquipasPontuacoes(listaPontuacoes);  
            }

        model.addAttribute("campeonatos", campeonatos);

        return "busca_campeonatos";
    }



    @GetMapping("/filtrajogadores") 
    public String opcoesDeFiltroJogadores() {
        return "filtra_jogadores";
    }

    @GetMapping("/filtraarbitros") 
    public String opcoesDeFiltroArbitros() {
        return "filtra_arbitros";
    }

    @GetMapping("/filtraequipas") 
    public String opcoesDeFiltroEquipas() {
        return "filtra_equipas";
    }

    @GetMapping("/filtrajogos") 
    public String opcoesDeFiltroJogos() {
        return "filtra_jogos";
    }

    @GetMapping("/filtracampeonatos") 
    public String opcoesDeFiltroCampeonatos() {
        return "filtra_campeonatos";
    }

    @GetMapping("/filtrajogadoresnome")
    public String filtraNomesJogadores(@RequestParam(required = false) String nome, Model model) {
        List<JogadorDtoGet> jogadores = jogadorController.getJogadorPorNome(nome).getBody(); 
        model.addAttribute("jogadores", jogadores);
        model.addAttribute("nome", nome);
        return "filtro_nome_jogadores"; 
    }

    @GetMapping("/filtrajogadorespos")
    public String filtraPosJogadores(@RequestParam(required = false) Posicao pos, Model model) {
        List<JogadorDtoGet> jogadores = jogadorController.getJogadorPorPosicao(pos).getBody(); 
        model.addAttribute("jogadores", jogadores);
        model.addAttribute("pos", pos);
        return "filtro_pos_jogadores"; 
    }

    @GetMapping("/filtrajogadoresgolos")
    public String filtraGolosJogadores(@RequestParam(required = false) Integer golos, Model model) {
        if(golos != null) {
            List<JogadorDtoGet> jogadores = jogadorController.getJogadorPorNumeroDeGolos(golos).getBody(); 
            model.addAttribute("jogadores", jogadores);
        }
        return "filtro_golos_jogadores"; 
    }

    @GetMapping("/filtrajogadorescartoes")
    public String filtraCartoesJogadores(@RequestParam(required = false) Integer cartoes, Model model) {
        if(cartoes != null) {
            List<JogadorDtoGet> jogadores = jogadorController.getJogadorPorNumeroDeCartoes(cartoes).getBody(); 
            model.addAttribute("jogadores", jogadores);
        }
        return "filtro_cartoes_jogadores"; 
    }
    
    @GetMapping("/filtrajogadoresjogos")
    public String filtraJogosJogadores(@RequestParam(required = false) Integer numJogos, Model model) {
        if(numJogos != null) {
            List<JogadorDtoGet> jogadores = jogadorController.getJogadorPorNumeroDeJogos(numJogos).getBody(); 
            model.addAttribute("jogadores", jogadores);
        }
        return "filtro_jogos_jogadores"; 
    }

    @GetMapping("/filtraarbitrosnome")
    public String filtraNomesArbitros(@RequestParam(required = false) String nome, Model model) {
        List<ArbitroDtoGet> arbitros = arbitroController.getArbitrosPorNome(nome).getBody(); 
        model.addAttribute("arbitros", arbitros);
        model.addAttribute("nome", nome);
        return "filtro_nome_arbitros"; 
    }

    @GetMapping("/filtraarbitrosjogos")
    public String filtraJogosArbitros(@RequestParam(name = "numJogos", required = false) Integer jogos, Model model) {
        if(jogos != null) {
            List<ArbitroDtoGet> arbitros = arbitroController.getArbitrosPorNumeroDeJogos(jogos).getBody(); 
            model.addAttribute("arbitros", arbitros);
        }
        return "filtro_jogos_arbitros"; 
    }


    @GetMapping("/filtraarbitroscartoes")
    public String filtraCartoesArbitros(@RequestParam(required = false) Integer numCartoes, Model model) {
        if(numCartoes != null) {
            List<ArbitroDtoGet> arbitros = arbitroController.getArbitrosPorNumeroDeCartoes(numCartoes).getBody(); 
            model.addAttribute("arbitros", arbitros);
        }
        return "filtro_cartoes_arbitros"; 
    }

    @GetMapping("/filtraequipasnome")
    public String filtraNomesEquipas(@RequestParam(required = false) String nome, Model model) {
        List<EquipaDtoGet> equipasFiltradas = equipaController.getEquipasPorNome(nome).getBody(); 
        List<JogoDtoGet> jogos = jogoController.getJogosRealizados().getBody();

        List<EquipaDtoGet> todasEquipas = equipaController.getAllEquipas().getBody();

        Map<Long, Map<Long, String>> mapaJogos = criarMapaDescricaoJogos(jogos, todasEquipas); 
        atualizarHistoricoEquipas(equipasFiltradas, mapaJogos); 

        model.addAttribute("equipas", equipasFiltradas);
        model.addAttribute("nome", nome);
        return "filtro_nome_equipas";
    }



    @GetMapping("/filtraequipasnrjogadores")
    public String filtraNrJogadoresEquipas(@RequestParam(required = false) Integer num, Model model) {
        if(num != null) {
            List<EquipaDtoGet> equipasFiltradas = equipaController.getEquipasPorNumeroDeJogadores(num).getBody(); 
            List<JogoDtoGet> jogos = jogoController.getJogosRealizados().getBody();

            List<EquipaDtoGet> todasEquipas = equipaController.getAllEquipas().getBody();

            Map<Long, Map<Long, String>> mapaJogos = criarMapaDescricaoJogos(jogos, todasEquipas); 
            atualizarHistoricoEquipas(equipasFiltradas, mapaJogos); 
            model.addAttribute("equipas", equipasFiltradas);
        }
        return "filtro_numjog_equipas"; 
    }

    @GetMapping("/filtraequipasnrvitorias")
    public String filtraNrVitoriasEquipas(@RequestParam(required = false) Integer num, Model model) {
        if(num != null) {
            List<EquipaDtoGet> equipas = equipaController.getEquipasPorNumeroDeVitorias(num).getBody(); 
            List<JogoDtoGet> jogos = jogoController.getJogosRealizados().getBody();

            List<EquipaDtoGet> todasEquipas = equipaController.getAllEquipas().getBody();

            Map<Long, Map<Long, String>> mapaJogos = criarMapaDescricaoJogos(jogos, todasEquipas); 
            atualizarHistoricoEquipas(equipas, mapaJogos);
            model.addAttribute("equipas", equipas);
        }
        return "filtro_numv_equipas"; 
    }

    @GetMapping("/filtraequipasnrempates")
    public String filtraNrEmpatesEquipas(@RequestParam(required = false) Integer num, Model model) {
        if(num != null) {
            List<EquipaDtoGet> equipas = equipaController.getEquipasPorNumeroDeEmpates(num).getBody();
            List<JogoDtoGet> jogos = jogoController.getJogosRealizados().getBody();

            List<EquipaDtoGet> todasEquipas = equipaController.getAllEquipas().getBody();

            Map<Long, Map<Long, String>> mapaJogos = criarMapaDescricaoJogos(jogos, todasEquipas); 
            atualizarHistoricoEquipas(equipas, mapaJogos); 
            model.addAttribute("equipas", equipas);
        }
        return "filtro_nume_equipas"; 
    }

    @GetMapping("/filtraequipasnrderrotas")
    public String filtraNrDerrotasEquipas(@RequestParam(required = false) Integer num, Model model) {
        if(num != null) {
            List<EquipaDtoGet> equipas = equipaController.getEquipasPorNumeroDeDerrotas(num).getBody(); 
            List<JogoDtoGet> jogos = jogoController.getJogosRealizados().getBody();

            List<EquipaDtoGet> todasEquipas = equipaController.getAllEquipas().getBody();

            Map<Long, Map<Long, String>> mapaJogos = criarMapaDescricaoJogos(jogos, todasEquipas); 
            atualizarHistoricoEquipas(equipas, mapaJogos);
            model.addAttribute("equipas", equipas);
        }
        return "filtro_numd_equipas"; 
    }

    @GetMapping("/filtraequipasnrconquistas")
    public String filtraNrConquistasEquipas(@RequestParam(required = false) Integer num, Model model) {
        if(num != null) {
            List<EquipaDtoGet> equipas = equipaController.getEquipasPorNumeroDeConquistas(num).getBody(); 
            List<JogoDtoGet> jogos = jogoController.getJogosRealizados().getBody();

            List<EquipaDtoGet> todasEquipas = equipaController.getAllEquipas().getBody();

            Map<Long, Map<Long, String>> mapaJogos = criarMapaDescricaoJogos(jogos, todasEquipas); 
            atualizarHistoricoEquipas(equipas, mapaJogos);
            model.addAttribute("equipas", equipas);
        }
        return "filtro_numconquistas_equipas"; 
    }
    
    @GetMapping("/filtraequipasausenciaspos")
    public String filtraNrAusenciasEquipas(@RequestParam(required = false) Posicao pos, Model model) {
        if (pos != null) {
            List<EquipaDtoGet> equipas = equipaController.getEquipasPorPosicaoEmFalta(pos).getBody(); 
            List<JogoDtoGet> jogos = jogoController.getJogosRealizados().getBody();

            List<EquipaDtoGet> todasEquipas = equipaController.getAllEquipas().getBody();

            Map<Long, Map<Long, String>> mapaJogos = criarMapaDescricaoJogos(jogos, todasEquipas); 
            atualizarHistoricoEquipas(equipas, mapaJogos);
            model.addAttribute("equipas", equipas);
        }
        return "filtro_ausenciaspos_equipas"; 
    }


   @GetMapping("/filtrajogosrealizados")
    public String filtraRealizadosJogos(Model model) {
        List<JogoDtoGet> jogos = jogoController.getJogosRealizados().getBody();
        List<EquipaDtoGet> todasEquipas = equipaController.getAllEquipas().getBody();
        List<ArbitroDtoGet> todosArbitros = arbitroController.getAllArbitros().getBody();
        List<JogadorDtoGet> todosJogadores = jogadorController.getAllJogadores().getBody();

        Map<Long, String> mapaEquipas = mapIdParaNomeEquipas(todasEquipas);
        Map<Long, String> mapaArbitros = mapIdParaNomeArbitros(todosArbitros);
        Map<Long, String> mapaJogadores = mapIdParaNomeJogadores(todosJogadores);

        List<JogoVista> jogosView = jogos.stream()
            .map(j -> converteParaJogoView(j, mapaEquipas, mapaArbitros, mapaJogadores))
            .toList();

        model.addAttribute("jogos", jogosView);
        return "filtro_realizados_jogos";
    }

    private Map<Long, String> mapIdParaNomeEquipas(List<EquipaDtoGet> equipas) {
        return equipas.stream()
                .collect(Collectors.toMap(EquipaDtoGet::getId, EquipaDtoGet::getNome));
    }

    private Map<Long, String> mapIdParaNomeArbitros(List<ArbitroDtoGet> arbitros) {
        return arbitros.stream()
                .collect(Collectors.toMap(ArbitroDtoGet::getId, ArbitroDtoGet::getNome));
    }

    private Map<Long, String> mapIdParaNomeJogadores(List<JogadorDtoGet> jogadores) {
        return jogadores.stream()
                .collect(Collectors.toMap(JogadorDtoGet::getId, JogadorDtoGet::getNome));
    }

    private JogoVista converteParaJogoView(JogoDtoGet jogo,
                                      Map<Long, String> mapaEquipas,
                                      Map<Long, String> mapaArbitros,
                                      Map<Long, String> mapaJogadores) {
        String equipaCasa = mapaEquipas.getOrDefault(jogo.getEquipaCasaId(), "Desconhecida");
        String equipaVisitante = mapaEquipas.getOrDefault(jogo.getEquipaVisitanteId(), "Desconhecida");

        Long principalId = jogo.getArbitrosNoJogo().getArbitroPrincipalId();
        String nomePrincipal = mapaArbitros.getOrDefault(principalId, "Desconhecido");

        List<String> nomesAssistentes = jogo.getArbitrosNoJogo().getArbitrosIds().stream()
                .filter(id -> !id.equals(principalId))
                .map(id -> mapaArbitros.getOrDefault(id, "Desconhecido"))
                .collect(Collectors.toList());

        // Monta lista de jogadores (nome + posição)
        List<String> jogadoresStr = jogo.getJogadoresNoJogo().stream()
            .map(jogadorNoJogo -> {
                String nomeJogador = mapaJogadores.getOrDefault(jogadorNoJogo.getJogadorId(), "Desconhecido");
                return nomeJogador + " (" + jogadorNoJogo.getPosicao() + ")";
            })
            .collect(Collectors.toList());

        return new JogoVista(
            jogo.getLocal().getDataHoraInicio(),
            jogo.getLocal().getDataHoraFim(),
            equipaCasa,
            equipaVisitante,
            nomePrincipal,
            String.join(", ", nomesAssistentes),
            jogadoresStr,  
            jogo.getEstado(),
            jogo.getLocal().getLocal()
    );
    }



    @GetMapping("/filtrajogosporrealizar")
    public String filtraPorRealizarJogos(Model model) {
        List<JogoDtoGet> jogos = jogoController.getJogosPorRealizar().getBody(); 
        List<EquipaDtoGet> todasEquipas = equipaController.getAllEquipas().getBody();
        List<ArbitroDtoGet> todosArbitros = arbitroController.getAllArbitros().getBody();
        List<JogadorDtoGet> todosJogadores = jogadorController.getAllJogadores().getBody();

        Map<Long, String> mapaEquipas = mapIdParaNomeEquipas(todasEquipas);
        Map<Long, String> mapaArbitros = mapIdParaNomeArbitros(todosArbitros);
        Map<Long, String> mapaJogadores = mapIdParaNomeJogadores(todosJogadores);

        List<JogoVista> jogosView = jogos.stream()
            .map(j -> converteParaJogoView(j, mapaEquipas, mapaArbitros, mapaJogadores))
            .toList();
        model.addAttribute("jogos", jogosView);
        return "filtro_arealizar_jogos"; 
    }
    

    @GetMapping("/filtrajogosgolos")
    public String filtraGolosJogos(@RequestParam(required = false) Integer numGolos, Model model) {
        if(numGolos != null) {
            List<JogoDtoGet> jogos = jogoController.getJogosPorQuantidadeDeGolos(numGolos).getBody(); 
            List<EquipaDtoGet> todasEquipas = equipaController.getAllEquipas().getBody();
        List<ArbitroDtoGet> todosArbitros = arbitroController.getAllArbitros().getBody();
        List<JogadorDtoGet> todosJogadores = jogadorController.getAllJogadores().getBody();

        Map<Long, String> mapaEquipas = mapIdParaNomeEquipas(todasEquipas);
        Map<Long, String> mapaArbitros = mapIdParaNomeArbitros(todosArbitros);
        Map<Long, String> mapaJogadores = mapIdParaNomeJogadores(todosJogadores);

        List<JogoVista> jogosView = jogos.stream()
            .map(j -> converteParaJogoView(j, mapaEquipas, mapaArbitros, mapaJogadores))
            .toList();
        model.addAttribute("jogos", jogosView);
        }
        return "filtro_numgolos_jogos"; 
    }

    @GetMapping("/filtrajogoslocal")
    public String filtraLocalizacaoJogos(@RequestParam(required = false) String nomeLocal, Model model) {
        List<JogoDtoGet> jogos = jogoController.getJogosPorLocal(nomeLocal).getBody(); 
        List<EquipaDtoGet> todasEquipas = equipaController.getAllEquipas().getBody();
        List<ArbitroDtoGet> todosArbitros = arbitroController.getAllArbitros().getBody();
        List<JogadorDtoGet> todosJogadores = jogadorController.getAllJogadores().getBody();

        Map<Long, String> mapaEquipas = mapIdParaNomeEquipas(todasEquipas);
        Map<Long, String> mapaArbitros = mapIdParaNomeArbitros(todosArbitros);
        Map<Long, String> mapaJogadores = mapIdParaNomeJogadores(todosJogadores);

        List<JogoVista> jogosView = jogos.stream()
            .map(j -> converteParaJogoView(j, mapaEquipas, mapaArbitros, mapaJogadores))
            .toList();
        model.addAttribute("jogos", jogosView);
        model.addAttribute("nomeLocal", nomeLocal);
        return "filtro_local_jogos"; 
    }

    @GetMapping("/filtrajogosmanha")
    public String filtraTurnoMJogos(Model model) {
        List<JogoDtoGet> jogos = jogoController.getJogosTurnoManha().getBody(); 
        List<EquipaDtoGet> todasEquipas = equipaController.getAllEquipas().getBody();
        List<ArbitroDtoGet> todosArbitros = arbitroController.getAllArbitros().getBody();
        List<JogadorDtoGet> todosJogadores = jogadorController.getAllJogadores().getBody();

        Map<Long, String> mapaEquipas = mapIdParaNomeEquipas(todasEquipas);
        Map<Long, String> mapaArbitros = mapIdParaNomeArbitros(todosArbitros);
        Map<Long, String> mapaJogadores = mapIdParaNomeJogadores(todosJogadores);

        List<JogoVista> jogosView = jogos.stream()
            .map(j -> converteParaJogoView(j, mapaEquipas, mapaArbitros, mapaJogadores))
            .toList();
        model.addAttribute("jogos", jogosView);
        return "filtro_manha_jogos"; 
    }

    @GetMapping("/filtrajogostarde")
    public String filtraTurnoTJogos(Model model) {
        List<JogoDtoGet> jogos = jogoController.getJogosTurnoTarde().getBody(); 
        List<EquipaDtoGet> todasEquipas = equipaController.getAllEquipas().getBody();
        List<ArbitroDtoGet> todosArbitros = arbitroController.getAllArbitros().getBody();
        List<JogadorDtoGet> todosJogadores = jogadorController.getAllJogadores().getBody();

        Map<Long, String> mapaEquipas = mapIdParaNomeEquipas(todasEquipas);
        Map<Long, String> mapaArbitros = mapIdParaNomeArbitros(todosArbitros);
        Map<Long, String> mapaJogadores = mapIdParaNomeJogadores(todosJogadores);

        List<JogoVista> jogosView = jogos.stream()
            .map(j -> converteParaJogoView(j, mapaEquipas, mapaArbitros, mapaJogadores))
            .toList();
        model.addAttribute("jogos", jogosView);
        return "filtro_tarde_jogos"; 
    }

    @GetMapping("/filtrajogosnoite")
    public String filtraTurnoNJogos(Model model) {
        List<JogoDtoGet> jogos = jogoController.getJogosTurnoNoite().getBody(); 
        List<EquipaDtoGet> todasEquipas = equipaController.getAllEquipas().getBody();
        List<ArbitroDtoGet> todosArbitros = arbitroController.getAllArbitros().getBody();
        List<JogadorDtoGet> todosJogadores = jogadorController.getAllJogadores().getBody();

        Map<Long, String> mapaEquipas = mapIdParaNomeEquipas(todasEquipas);
        Map<Long, String> mapaArbitros = mapIdParaNomeArbitros(todosArbitros);
        Map<Long, String> mapaJogadores = mapIdParaNomeJogadores(todosJogadores);

        List<JogoVista> jogosView = jogos.stream()
            .map(j -> converteParaJogoView(j, mapaEquipas, mapaArbitros, mapaJogadores))
            .toList();
        model.addAttribute("jogos", jogosView);
        return "filtro_noite_jogos"; 
    }
    @GetMapping("/filtracampeonatosnome")
    public String filtraNomeCampeonatos(@RequestParam(required = false) String nome, Model model) {
        List<CampeonatoDtoGet> campeonatos = campeonatoController.filtrarPorNome(nome).getBody();
        List<EquipaDtoGet> todasEquipas = equipaController.getAllEquipas().getBody();

        for (CampeonatoDtoGet c : campeonatos) {
            List<String> nomesEquipas = c.getEquipaIds().stream()
                .map(id -> getNomeEquipaById(id, todasEquipas))
                .collect(Collectors.toList());

            Map<String, Integer> pontuacoesOriginais = c.getPontuacoes();


            List<EquipaPontuacaoDto> listaPontuacoes = new ArrayList<>();

            for (int i = 0; i < c.getEquipaIds().size(); i++) {
                String nomeEquipa = nomesEquipas.get(i);

                Integer pontos = 0;
                if (pontuacoesOriginais != null) {
                    pontos = pontuacoesOriginais.getOrDefault(nomeEquipa, 0);
                }
                listaPontuacoes.add(new EquipaPontuacaoDto(nomeEquipa, pontos));
            }
            
            listaPontuacoes.sort((a, b) -> b.getPontuacao().compareTo(a.getPontuacao()));
            c.setEquipasPontuacoes(listaPontuacoes);  
        }

        model.addAttribute("campeonatos", campeonatos);
        return "filtro_nome_campeonatos";
    }


    private String getNomeEquipaById(Long id, List<EquipaDtoGet> equipas) {
        for (EquipaDtoGet equipa : equipas) {
            if (equipa.getId().equals(id)) {
                return equipa.getNome();
            }
        }
        return "Desconhecida";
    }


    private Long findIdByNome(String nome, List<EquipaDtoGet> equipas) {
        for (EquipaDtoGet equipa : equipas) {
            if (equipa.getNome().equalsIgnoreCase(nome)) {
                return equipa.getId();
            }
        }
        return null;
    }


    @GetMapping("/filtracampeonatosequipa")
    public String filtraEquipaCampeonatos(@RequestParam(required = false) String nome, Model model) {
        List<EquipaDtoGet> equipas = equipaController.getAllEquipas().getBody();
        
        if (nome != null && !nome.isBlank()) {
            Long equipaID = findIdByNome(nome, equipas);
            if (equipaID != null) {
                List<CampeonatoDtoGet> campeonatos = campeonatoController.filtrarPorEquipa(equipaID).getBody(); 
                List<EquipaDtoGet> todasEquipas = equipaController.getAllEquipas().getBody();

            for (CampeonatoDtoGet c : campeonatos) {
                List<String> nomesEquipas = c.getEquipaIds().stream()
                    .map(id -> getNomeEquipaById(id, todasEquipas))
                    .collect(Collectors.toList());

                Map<String, Integer> pontuacoesOriginais = c.getPontuacoes();


                List<EquipaPontuacaoDto> listaPontuacoes = new ArrayList<>();

                for (int i = 0; i < c.getEquipaIds().size(); i++) {
                    String nomeEquipa = nomesEquipas.get(i);

                    Integer pontos = 0;
                    if (pontuacoesOriginais != null) {
                        pontos = pontuacoesOriginais.getOrDefault(nomeEquipa, 0);
                    }


                    listaPontuacoes.add(new EquipaPontuacaoDto(nomeEquipa, pontos));
                }
                listaPontuacoes.sort((a, b) -> b.getPontuacao().compareTo(a.getPontuacao()));
                c.setEquipasPontuacoes(listaPontuacoes);  
            }
                model.addAttribute("campeonatos", campeonatos);
            } else {
                model.addAttribute("erro", "Não existe uma equipa com esse nome.");
            }
        }

        return "filtro_equipa_campeonatos"; 
    }


    @GetMapping("/filtracampeonatosjrealizados")
    public String filtraNumJogosRealizadosCampeonatos(@RequestParam(required = false) Long minJogos, Model model) {
        if(minJogos != null) {
            List<CampeonatoDtoGet> campeonatos = campeonatoController.filtrarPorJogosRealizados(minJogos).getBody(); 
            List<EquipaDtoGet> todasEquipas = equipaController.getAllEquipas().getBody();

            for (CampeonatoDtoGet c : campeonatos) {
                List<String> nomesEquipas = c.getEquipaIds().stream()
                    .map(id -> getNomeEquipaById(id, todasEquipas))
                    .collect(Collectors.toList());

                Map<String, Integer> pontuacoesOriginais = c.getPontuacoes();

                List<EquipaPontuacaoDto> listaPontuacoes = new ArrayList<>();

                for (int i = 0; i < c.getEquipaIds().size(); i++) {
                    String nomeEquipa = nomesEquipas.get(i);

                    Integer pontos = 0;
                    if (pontuacoesOriginais != null) {
                        pontos = pontuacoesOriginais.getOrDefault(nomeEquipa, 0);
                    }

                    
                    listaPontuacoes.add(new EquipaPontuacaoDto(nomeEquipa, pontos));
                }
                listaPontuacoes.sort((a, b) -> b.getPontuacao().compareTo(a.getPontuacao()));
                c.setEquipasPontuacoes(listaPontuacoes);  
            }

            model.addAttribute("campeonatos", campeonatos);
        }
        return "filtro_jrealizados_campeonatos"; 
    }

    @GetMapping("/filtracampeonatosjarealizar")
    public String filtraNumJogosARealizarCampeonatos(@RequestParam(required = false) Long minJogos, Model model) {
        if(minJogos != null) {
            List<CampeonatoDtoGet> campeonatos = campeonatoController.filtrarPorJogosPorRealizar(minJogos).getBody(); 
            List<EquipaDtoGet> todasEquipas = equipaController.getAllEquipas().getBody();

            for (CampeonatoDtoGet c : campeonatos) {
                List<String> nomesEquipas = c.getEquipaIds().stream()
                    .map(id -> getNomeEquipaById(id, todasEquipas))
                    .collect(Collectors.toList());

                 Map<String, Integer> pontuacoesOriginais = c.getPontuacoes();

                List<EquipaPontuacaoDto> listaPontuacoes = new ArrayList<>();

                for (int i = 0; i < c.getEquipaIds().size(); i++) {
                    String nomeEquipa = nomesEquipas.get(i);

                    Integer pontos = 0;
                    if (pontuacoesOriginais != null) {
                        pontos = pontuacoesOriginais.getOrDefault(nomeEquipa, 0);
                    }

                    listaPontuacoes.add(new EquipaPontuacaoDto(nomeEquipa, pontos));
                }
                listaPontuacoes.sort((a, b) -> b.getPontuacao().compareTo(a.getPontuacao()));

                c.setEquipasPontuacoes(listaPontuacoes);  
            }

            model.addAttribute("campeonatos", campeonatos);
        }
        return "filtro_jarealizar_campeonatos"; 
    }

    

    @GetMapping("/mostrar_registar_resultado")
    public String mostrarFormularioRegistarResultado(Model model) {
        List<JogoDtoGet> jogos = jogoController.getJogosPorRealizar().getBody();
        List<EquipaDtoGet> equipas = equipaController.getAllEquipas().getBody();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        List<JogoView> jogosView = jogos.stream().map(jogo -> {
        EquipaDtoGet equipaCasa = equipas.stream()
            .filter(eq -> eq.getId().equals(jogo.getEquipaCasaId()))
            .findFirst().orElse(null);

        EquipaDtoGet equipaVisitante = equipas.stream()
            .filter(eq -> eq.getId().equals(jogo.getEquipaVisitanteId()))
            .findFirst().orElse(null);

        LocalDateTime inicio = jogo.getLocal().getDataHoraInicio();
        LocalDateTime fim = jogo.getLocal().getDataHoraFim(); // ou calcular

        String descricao = jogo.getLocal().getLocal() + ", " +
            inicio.format(dateFormatter) + " das " + 
            inicio.format(timeFormatter) + " às " + 
            fim.format(timeFormatter) + " - " +
            (equipaCasa != null ? equipaCasa.getNome() : "Equipa Casa") + " vs " +
            (equipaVisitante != null ? equipaVisitante.getNome() : "Equipa Visitante");

        // Mapeia jogadores do jogo
        List<JogadorView> jogadoresView = jogo.getJogadoresNoJogo().stream()
            .map(j -> {
                JogadorDtoGet jogador = jogadorController.getJogadorById(j.getJogadorId()).getBody();
                return new JogadorView(j.getJogadorId(), jogador.getNome(), j.getPosicao());
            }).toList();

        return new JogoView(jogo.getId(), descricao, jogadoresView);
    }).toList();


        model.addAttribute("estatisticaDto", new EstatisticaDto(new ArrayList<>(), new ArrayList<>()));
        model.addAttribute("jogosView", jogosView);
        return "registar_resultado";
    }


    @PostMapping("/registarResultado")
    public String registarResultado(
            @RequestParam Long jogoId,
            @ModelAttribute EstatisticaDto estatisticaDto,
            Model model) {

        JogoDtoGet jogoAtualizado = jogoController.registrarResultado(jogoId, estatisticaDto).getBody();

        if (jogoAtualizado == null) {
            model.addAttribute("erro", "Erro ao registar resultado. Verifique se o jogo existe.");
            return "registar_resultado";
        }

        model.addAttribute("mensagem", "Resultado registado com sucesso!");
        model.addAttribute("jogo", jogoAtualizado);
        return "layout";
    }

    @GetMapping("/layout")
    public String mostrarLayout() {
        return "layout"; 
    }


    

}
