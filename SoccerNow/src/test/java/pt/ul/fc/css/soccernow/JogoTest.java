package pt.ul.fc.css.soccernow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import pt.ul.fc.css.soccernow.dto.*;
import pt.ul.fc.css.soccernow.entities.*;
import pt.ul.fc.css.soccernow.enumerados.Posicao;
import pt.ul.fc.css.soccernow.repository.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class JogoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JogoRepository jogoRepository;

    @Autowired
    private EquipaRepository equipaRepository;

    @Autowired
    private JogadorRepository jogadorRepository;

    @Autowired
    private ArbitroRepository arbitroRepository;

    @Autowired
    private EntityManager entityManager;


    private List<Jogador> jogadoresMock;
    List<Long> jogadorIds = new ArrayList<>();
    private List<Arbitro> arbitrosMock;
    private Equipa benfica;
    private Equipa sporting;
    private Jogo jogoMock;

    @BeforeEach
    void setUp() throws Exception {

        jogadoresMock = new ArrayList<>();

        // Criar 10 jogadores via endpoint
        for (int i = 1; i <= 10; i++) {
            JogadorDto jogadorDto = new JogadorDto("Jogador" + i, "jogador" + i + "@email.com", "senha", true,Posicao.DEFESA);

            MvcResult result = mockMvc.perform(post("/api/jogadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jogadorDto)))
                        .andExpect(status().isOk())
                        .andReturn();

            String response = result.getResponse().getContentAsString();
            Long id = JsonPath.parse(response).read("$.id", Long.class);
            jogadorIds.add(id);
            jogadoresMock.add(jogadorRepository.findById(id).orElseThrow());
        }

        // Criar 2 árbitros via endpoint
        arbitrosMock = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            ArbitroDto arbitroDto = new ArbitroDto("Árbitro" + i, "arbitro" + i + "@email.com", "senha", true, 5 + i);

            MvcResult result = mockMvc.perform(post("/api/arbitros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(arbitroDto)))
                        .andExpect(status().isOk())
                        .andReturn();

            String response = result.getResponse().getContentAsString();
            Long id = JsonPath.parse(response).read("$.id", Long.class);
            arbitrosMock.add(arbitroRepository.findById(id).orElseThrow());
        }

        // Criar equipa "Benfica" com jogadores 1 a 5
        EquipaDto benficaDto = new EquipaDto("Benfica", jogadorIds.subList(0, 5));
        MvcResult benficaResult = mockMvc.perform(post("/api/equipas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(benficaDto)))
                        .andExpect(status().isOk())
                        .andReturn();

        Long benficaId = JsonPath.parse(benficaResult.getResponse().getContentAsString()).read("$.id", Long.class);
        benfica = equipaRepository.findById(benficaId).orElseThrow();

        // Criar equipa "Sporting" com jogadores 6 a 10
        EquipaDto sportingDto = new EquipaDto("Sporting", jogadorIds.subList(5, 10));
        MvcResult sportingResult = mockMvc.perform(post("/api/equipas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sportingDto)))
                        .andExpect(status().isOk())
                        .andReturn();

        Long sportingId = JsonPath.parse(sportingResult.getResponse().getContentAsString()).read("$.id", Long.class);
        sporting = equipaRepository.findById(sportingId).orElseThrow();

        //criar jogo
        // Dados do local
        LocalDto localDto = new LocalDto("Estádio Teste",
                LocalDateTime.of(2025, 4, 24, 15, 30),
                LocalDateTime.of(2025, 4, 24, 16, 30));

        // Cria a lista de JogadorNoJogoDto com 10 jogadores, com 1 guarda-redes em cada equipa
        List<JogadorNoJogoDto> jogadoresNoJogo = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
                String posicao;
                
                if (i == 0 || i == 5) { // Jogador 0 e jogador 5 serão GUARDA_REDES
                        posicao = "GUARDA_REDES";
                } else {
                        posicao = "AVANCADO"; // Os restantes serão AVANCADO
                }

                JogadorNoJogoDto jogadorNoJogoDto = new JogadorNoJogoDto(
                        jogadoresMock.get(i).getId(),
                        posicao
                );
                jogadoresNoJogo.add(jogadorNoJogoDto);
        }


        // Árbitros
        ArbitrosNoJogoDto arbitrosDto = new ArbitrosNoJogoDto(
                List.of(arbitrosMock.get(0).getId(), arbitrosMock.get(1).getId()), 
                arbitrosMock.get(0).getId()
        );

        // Criação do jogo com 10 jogadores, mas sem guarda-redes
        JogoDto jogoDto = new JogoDto(
                localDto,
                benfica.getId(),
                sporting.getId(),
                jogadoresNoJogo,
                arbitrosDto
        );


        // criar jogo
        MvcResult jogoResult = mockMvc.perform(post("/api/jogos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jogoDto)))
                        .andReturn();
        Long jogoId = JsonPath.parse(jogoResult.getResponse().getContentAsString()).read("$.id", Long.class);
        jogoMock= jogoRepository.findById(jogoId).orElseThrow();
    }

    @Test 
    void testEquipasIguaisException() throws Exception {
        // Dados do local
        LocalDto localDto = new LocalDto("Estádio Teste",
                LocalDateTime.of(2025, 4, 24, 15, 30),
                LocalDateTime.of(2025, 4, 24, 16, 30));

        // Jogador no jogo
        JogadorNoJogoDto jogadorNoJogoDto = new JogadorNoJogoDto(jogadoresMock.get(0).getId(), "AVANCADO");

        // Árbitros
        ArbitrosNoJogoDto arbitrosDto = new ArbitrosNoJogoDto(List.of(arbitrosMock.get(0).getId(),arbitrosMock.get(1).getId()), arbitrosMock.get(0).getId());

        // Jogo com mesma equipa como casa e visitante
        JogoDto jogoDto = new JogoDto(
                localDto,
                benfica.getId(),
                benfica.getId(),
                List.of(jogadorNoJogoDto),
                arbitrosDto
        );

        // Executar e verificar erro esperado
        mockMvc.perform(post("/api/jogos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jogoDto)))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string("Uma equipa não pode jogar contra si própria."));
    }

    @Test 
    void testDataHoraInvalidaException() throws Exception {
        // Dados do local com data e hora inválidas (data/hora de início não é anterior da data/hora de fim)
        LocalDto localDto = new LocalDto("Estádio Teste",
                LocalDateTime.of(2025, 4, 24, 15, 30),
                LocalDateTime.of(2025, 4, 24, 14, 30));

        // Jogador no jogo
        JogadorNoJogoDto jogadorNoJogoDto = new JogadorNoJogoDto(jogadoresMock.get(0).getId(), "AVANCADO");

        // Árbitros
        ArbitrosNoJogoDto arbitrosDto = new ArbitrosNoJogoDto(List.of(arbitrosMock.get(0).getId(),arbitrosMock.get(1).getId()), arbitrosMock.get(0).getId());

        // Jogo com equipas diferentes
        JogoDto jogoDto = new JogoDto(
                localDto,
                benfica.getId(),
                sporting.getId(),
                List.of(jogadorNoJogoDto),
                arbitrosDto
        );

        // Executar e verificar erro esperado
        mockMvc.perform(post("/api/jogos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jogoDto)))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string("A data/hora de início deve ser estritamente anterior à data/hora de fim."));
    }

    @Test 
    void testPosicaoInvalidaException() throws Exception {
        // Dados do local com data e hora inválidas (data/hora de início não é antes da data/hora de fim)
        LocalDto localDto = new LocalDto("Estádio Teste",
                LocalDateTime.of(2025, 4, 24, 15, 30),
                LocalDateTime.of(2025, 4, 24, 16, 30));

        // Jogador no jogo
        JogadorNoJogoDto jogadorNoJogoDto = new JogadorNoJogoDto(jogadoresMock.get(0).getId(), "AVANCADO100");

        // Árbitros
        ArbitrosNoJogoDto arbitrosDto = new ArbitrosNoJogoDto(List.of(arbitrosMock.get(0).getId(),arbitrosMock.get(1).getId()), arbitrosMock.get(0).getId());

        // Jogo com equipas diferentes
        JogoDto jogoDto = new JogoDto(
                localDto,
                benfica.getId(),
                sporting.getId(),
                List.of(jogadorNoJogoDto),
                arbitrosDto
        );

        // Executar e verificar erro esperado
        mockMvc.perform(post("/api/jogos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jogoDto)))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string("Posição não válida, as posiçoes válidas são: GUARDA_REDES, DEFESA, MEDIO e AVANCADO"));
    }

    @Test
    void testJogadorInvalidoException() throws Exception {
        //criar jogador sem equipa 
        JogadorDto jogadorSemEquipaDto = new JogadorDto("jogadorSemEquipa", "jogadorSemEquipa@email.com", "senha", true,Posicao.DEFESA);

        MvcResult result = mockMvc.perform(post("/api/jogadores")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(jogadorSemEquipaDto)))
                            .andExpect(status().isOk())
                            .andReturn();

        String response = result.getResponse().getContentAsString();
        Long jogadorSemEquipaId = JsonPath.parse(response).read("$.id", Long.class);


        // Dados do local
        LocalDto localDto = new LocalDto("Estádio Teste",
                LocalDateTime.of(2025, 4, 24, 15, 30),
                LocalDateTime.of(2025, 4, 24, 16, 30));

        // Jogador no jogo
        JogadorNoJogoDto jogadorNoJogoDto = new JogadorNoJogoDto(jogadorSemEquipaId, "AVANCADO");

        // Árbitros
        ArbitrosNoJogoDto arbitrosDto = new ArbitrosNoJogoDto(List.of(arbitrosMock.get(0).getId(),arbitrosMock.get(1).getId()), arbitrosMock.get(0).getId());

        // Jogo com equipas diferentes
        JogoDto jogoDto = new JogoDto(
                localDto,
                benfica.getId(),
                sporting.getId(),
                List.of(jogadorNoJogoDto),
                arbitrosDto
        );

        // Executar e verificar erro esperado
        mockMvc.perform(post("/api/jogos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jogoDto)))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string("Todos os jogadores devem pertencer à equipa da casa ou visitante."));
    }

    @Test 
    void testNumeroJogadoresInvalidoException() throws Exception {
        // Dados do local
        LocalDto localDto = new LocalDto("Estádio Teste",
                LocalDateTime.of(2025, 4, 24, 15, 30),
                LocalDateTime.of(2025, 4, 24, 16, 30));

        // 2 Jogadores no jogo
        JogadorNoJogoDto jogadorNoJogoDto = new JogadorNoJogoDto(jogadoresMock.get(0).getId(), "AVANCADO");
        JogadorNoJogoDto jogadorNoJogoDto2 = new JogadorNoJogoDto(jogadoresMock.get(1).getId(), "AVANCADO");

        // Árbitros
        ArbitrosNoJogoDto arbitrosDto = new ArbitrosNoJogoDto(List.of(arbitrosMock.get(0).getId(),arbitrosMock.get(1).getId()), arbitrosMock.get(0).getId());

        // Jogo com equipas diferentes
        JogoDto jogoDto = new JogoDto(
                localDto,
                benfica.getId(),
                sporting.getId(),
                List.of(jogadorNoJogoDto, jogadorNoJogoDto2),
                arbitrosDto
        );

        // Executar e verificar erro esperado
        mockMvc.perform(post("/api/jogos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jogoDto)))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string("Cada equipa deve ter exatamente 5 jogadores."));
    }

    @Test 
    void testNumeroGuardaRedesInvalidoException() throws Exception {
        // Dados do local
        LocalDto localDto = new LocalDto("Estádio Teste",
                LocalDateTime.of(2025, 4, 24, 15, 30),
                LocalDateTime.of(2025, 4, 24, 16, 30));

        // Cria a lista de JogadorNoJogoDto com 10 jogadores, mas sem guarda-redes
        List<JogadorNoJogoDto> jogadoresNoJogo = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
                JogadorNoJogoDto jogadorNoJogoDto = new JogadorNoJogoDto(
                        jogadoresMock.get(i).getId(),
                        "AVANCADO" 
                );
                jogadoresNoJogo.add(jogadorNoJogoDto);
        }

        // Árbitros
        ArbitrosNoJogoDto arbitrosDto = new ArbitrosNoJogoDto(
                List.of(arbitrosMock.get(0).getId(), arbitrosMock.get(1).getId()), 
                arbitrosMock.get(0).getId()
        );

        // Criação do jogo com 10 jogadores, mas sem guarda-redes
        JogoDto jogoDto = new JogoDto(
                localDto,
                benfica.getId(),
                sporting.getId(),
                jogadoresNoJogo,
                arbitrosDto
        );


        // Executar e verificar erro esperado
        mockMvc.perform(post("/api/jogos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jogoDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cada equipa deve ter exatamente 1 guarda-redes."));
    }

    @Test 
    void testJogadorEmAmbasEquipasException() throws Exception {
        // Dados do local
        LocalDto localDto = new LocalDto("Estádio Teste",
                LocalDateTime.of(2025, 4, 24, 15, 30),
                LocalDateTime.of(2025, 4, 24, 16, 30));

        // Cria a lista de JogadorNoJogoDto com 10 jogadores, mas sem guarda-redes
        List<JogadorNoJogoDto> jogadoresNoJogo = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
                JogadorNoJogoDto jogadorNoJogoDto = new JogadorNoJogoDto(
                        jogadoresMock.get(i).getId(),
                        "AVANCADO" 
                );
                jogadoresNoJogo.add(jogadorNoJogoDto);
        }

        // Árbitros
        ArbitrosNoJogoDto arbitrosDto = new ArbitrosNoJogoDto(
                List.of(arbitrosMock.get(0).getId(), arbitrosMock.get(1).getId()), 
                arbitrosMock.get(0).getId()
        );

        //atualizar o sporting para comecar no indice 4 em vez de 5, ou seja, contem um jogador do benfica
        EquipaDto sportingDto = new EquipaDto("Sporting", jogadorIds.subList(4, 10));
        MvcResult res = mockMvc.perform(post("/api/equipas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sportingDto)))
                        .andExpect(status().isOk())
                        .andReturn();
                        
        String responseBody = res.getResponse().getContentAsString();
        EquipaDtoGet sportingCriada = objectMapper.readValue(responseBody, EquipaDtoGet.class);
        // Criação do jogo com 10 jogadores, mas sem guarda-redes
        JogoDto jogoDto = new JogoDto(
                localDto,
                benfica.getId(),
                sportingCriada.getId(),
                jogadoresNoJogo,
                arbitrosDto
        );


        // Executar e verificar erro esperado
        mockMvc.perform(post("/api/jogos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jogoDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Um ou mais jogadores pertencem tanto à equipa da casa como à visitante."));
    }

    @Test
    void testCreateAndGetByIdJogo() throws Exception {
        // Dados do local
        LocalDto localDto = new LocalDto("Estádio Teste2",
                LocalDateTime.of(2025, 4, 24, 18, 30),
                LocalDateTime.of(2025, 4, 24, 19, 30));

        // Cria a lista de JogadorNoJogoDto com 10 jogadores, com 1 guarda-redes em cada equipa
        List<JogadorNoJogoDto> jogadoresNoJogo = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
                String posicao;
                
                if (i == 0 || i == 5) { // Jogador 0 e jogador 5 serão GUARDA_REDES
                        posicao = "GUARDA_REDES";
                } else {
                        posicao = "AVANCADO"; // Os restantes serão AVANCADO
                }

                JogadorNoJogoDto jogadorNoJogoDto = new JogadorNoJogoDto(
                        jogadoresMock.get(i).getId(),
                        posicao
                );
                jogadoresNoJogo.add(jogadorNoJogoDto);
        }


        // Árbitros
        ArbitrosNoJogoDto arbitrosDto = new ArbitrosNoJogoDto(
                List.of(arbitrosMock.get(0).getId(), arbitrosMock.get(1).getId()), 
                arbitrosMock.get(0).getId()
        );

        // Criação do jogo com 10 jogadores
        JogoDto jogoDto = new JogoDto(
                localDto,
                benfica.getId(),
                sporting.getId(),
                jogadoresNoJogo,
                arbitrosDto
        );


        // testar create jogo
        MvcResult result = mockMvc.perform(post("/api/jogos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jogoDto)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.local.local").value("Estádio Teste2"))
                        .andExpect(jsonPath("$.local.dataHoraInicio").value("2025-04-24T18:30:00"))
                        .andExpect(jsonPath("$.local.dataHoraFim").value("2025-04-24T19:30:00"))
                        .andExpect(jsonPath("$.equipaCasaId").value(benfica.getId()))
                        .andExpect(jsonPath("$.equipaVisitanteId").value(sporting.getId()))
                        .andReturn();

                
        String response = result.getResponse().getContentAsString();
        Long id = JsonPath.parse(response).read("$.id", Long.class);

        //testar getJogoById
        mockMvc.perform(get("/api/jogos/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.local.local").value("Estádio Teste2"))
                .andExpect(jsonPath("$.local.dataHoraInicio").value("2025-04-24T18:30:00"))
                .andExpect(jsonPath("$.local.dataHoraFim").value("2025-04-24T19:30:00"))
                .andExpect(jsonPath("$.equipaCasaId").value(benfica.getId()))
                .andExpect(jsonPath("$.equipaVisitanteId").value(sporting.getId()));
        
                
        }

    @Test
    void testJogoConflitanteException() throws Exception {
        // Dados do local
        // Criar um jogo com o mesmo horario e local do jogo criado (e salvo) no beforeEach
        LocalDto localDto = new LocalDto("Estádio Teste",
                LocalDateTime.of(2025, 4, 24, 15, 30),
                LocalDateTime.of(2025, 4, 24, 16, 30));

        // Cria a lista de JogadorNoJogoDto com 10 jogadores, com 1 guarda-redes em cada equipa
        List<JogadorNoJogoDto> jogadoresNoJogo = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
                String posicao;
                
                if (i == 0 || i == 5) { // Jogador 0 e jogador 5 serão GUARDA_REDES
                        posicao = "GUARDA_REDES";
                } else {
                        posicao = "AVANCADO"; // Os restantes serão AVANCADO
                }

                JogadorNoJogoDto jogadorNoJogoDto = new JogadorNoJogoDto(
                        jogadoresMock.get(i).getId(),
                        posicao
                );
                jogadoresNoJogo.add(jogadorNoJogoDto);
        }


        // Árbitros
        ArbitrosNoJogoDto arbitrosDto = new ArbitrosNoJogoDto(
                List.of(arbitrosMock.get(0).getId(), arbitrosMock.get(1).getId()), 
                arbitrosMock.get(0).getId()
        );

        JogoDto jogoDto = new JogoDto(
                localDto,
                benfica.getId(),
                sporting.getId(),
                jogadoresNoJogo,
                arbitrosDto
        );


        // Executar e verificar erro esperado
        mockMvc.perform(post("/api/jogos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jogoDto)))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string("Já existe um jogo neste local e horário."));
    } 

    @Test
    void testEquipaConflitanteException() throws Exception {
        // Dados do local
        // Criar um jogo com o mesmo com local diferente, mas horario que interseta o do jogo criado (e salvo) no beforeEach
        LocalDto localDto = new LocalDto("Estádio Maracana",
                LocalDateTime.of(2025, 4, 24, 15, 30),
                LocalDateTime.of(2025, 4, 24, 16, 30));

        // Cria a lista de JogadorNoJogoDto com 10 jogadores, com 1 guarda-redes em cada equipa
        List<JogadorNoJogoDto> jogadoresNoJogo = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
                String posicao;
                
                if (i == 0 || i == 5) { // Jogador 0 e jogador 5 serão GUARDA_REDES
                        posicao = "GUARDA_REDES";
                } else {
                        posicao = "AVANCADO"; // Os restantes serão AVANCADO
                }

                JogadorNoJogoDto jogadorNoJogoDto = new JogadorNoJogoDto(
                        jogadoresMock.get(i).getId(),
                        posicao
                );
                jogadoresNoJogo.add(jogadorNoJogoDto);
        }


        // Árbitros
        ArbitrosNoJogoDto arbitrosDto = new ArbitrosNoJogoDto(
                List.of(arbitrosMock.get(0).getId(), arbitrosMock.get(1).getId()), 
                arbitrosMock.get(0).getId()
        );


        JogoDto jogoDto = new JogoDto(
                localDto,
                benfica.getId(),
                sporting.getId(),
                jogadoresNoJogo,
                arbitrosDto
        );


        // Executar e verificar erro esperado
        mockMvc.perform(post("/api/jogos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jogoDto)))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string("Uma das equipas já tem um jogo marcado nesse horário."));
    }
    
    @Test
    void testArbitroConflitanteException() throws Exception {
        //criar outras 2 equipas que ainda nao fizeram jogos, logo nao pode dar equipaConflitanteExcpetion
        // Criar equipa "Benfica2" com jogadores 1 a 5
        EquipaDto benfica2Dto = new EquipaDto("Benfica2", jogadorIds.subList(0, 5));
        MvcResult benfica2Result = mockMvc.perform(post("/api/equipas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(benfica2Dto)))
                        .andExpect(status().isOk())
                        .andReturn();

        Long benfica2Id = JsonPath.parse(benfica2Result.getResponse().getContentAsString()).read("$.id", Long.class);
        Equipa benfica2 = equipaRepository.findById(benfica2Id).orElseThrow();

        // Criar equipa "Sporting2" com jogadores 6 a 10
        EquipaDto sporting2Dto = new EquipaDto("Sporting2", jogadorIds.subList(5, 10));
        MvcResult sporting2Result = mockMvc.perform(post("/api/equipas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sporting2Dto)))
                        .andExpect(status().isOk())
                        .andReturn();

        Long sporting2Id = JsonPath.parse(sporting2Result.getResponse().getContentAsString()).read("$.id", Long.class);
        Equipa sporting2 = equipaRepository.findById(sporting2Id).orElseThrow();



        // Dados do local
        // Criar um jogo com o mesmo com local diferente, mas horario que interseta o do jogo criado (e salvo) no beforeEach
        LocalDto localDto = new LocalDto("Estádio Maracana",
                LocalDateTime.of(2025, 4, 24, 15, 30),
                LocalDateTime.of(2025, 4, 24, 16, 30));

        // Cria a lista de JogadorNoJogoDto com 10 jogadores, com 1 guarda-redes em cada equipa
        List<JogadorNoJogoDto> jogadoresNoJogo = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
                String posicao;
                
                if (i == 0 || i == 5) { // Jogador 0 e jogador 5 serão GUARDA_REDES
                        posicao = "GUARDA_REDES";
                } else {
                        posicao = "AVANCADO"; // Os restantes serão AVANCADO
                }

                JogadorNoJogoDto jogadorNoJogoDto = new JogadorNoJogoDto(
                        jogadoresMock.get(i).getId(),
                        posicao
                );
                jogadoresNoJogo.add(jogadorNoJogoDto);
        }


        // Usar os mesmos arbitros do jogo criado no beforeEach
        ArbitrosNoJogoDto arbitrosDto = new ArbitrosNoJogoDto(
                List.of(arbitrosMock.get(0).getId(), arbitrosMock.get(1).getId()), 
                arbitrosMock.get(0).getId()
        );


        JogoDto jogoDto = new JogoDto(
                localDto,
                benfica2.getId(),
                sporting2.getId(),
                jogadoresNoJogo,
                arbitrosDto
        );


        // Executar e verificar erro esperado
        mockMvc.perform(post("/api/jogos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jogoDto)))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string("Um dos árbitros já está atribuido para outro jogo nesse horário."));
    }


    @Test
    void testGetJogo() throws Exception {
            
        //testar get jogos
        mockMvc.perform(get("/api/jogos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].local.local").value("Estádio Teste"))
                .andExpect(jsonPath("$[0].local.dataHoraInicio").value("2025-04-24T15:30:00"))
                .andExpect(jsonPath("$[0].local.dataHoraFim").value("2025-04-24T16:30:00"))
                .andExpect(jsonPath("$[0].equipaCasaId").value(benfica.getId()))
                .andExpect(jsonPath("$[0].equipaVisitanteId").value(sporting.getId()));
    }

    @Test
    void testResultado() throws Exception {
            
        //testar post resultado
        //golos 
        GoloDto goloDto1 = new GoloDto(jogadoresMock.get(0).getId(), 10); //jogador do benfica
        CartaoJogadorDto cartaoJogadorDto1 = new CartaoJogadorDto(jogadoresMock.get(7).getId(), "VERMELHO"); //jogador do sporting
        EstatisticaDto estatisticaDto = new EstatisticaDto(List.of(goloDto1), List.of(cartaoJogadorDto1));

        mockMvc.perform(post("/api/jogos/resultado/" + jogoMock.getId())
        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(estatisticaDto)))
                .andExpect(status().isOk())
                .andReturn();

        // FORCE DB SYNC
        entityManager.flush();
        entityManager.clear();
                
                
        //testar get desse  resultado
        mockMvc.perform(get("/api/jogos/resultado/" + jogoMock.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.golos[0].jogadorId").value(jogadoresMock.get(0).getId()))
        .andExpect(jsonPath("$.golos[0].minuto").value(10))
        .andExpect(jsonPath("$.cartoes[0].jogadorId").value(jogadoresMock.get(7).getId()))
        .andExpect(jsonPath("$.cartoes[0].tipoCartao").value("VERMELHO"))
        .andExpect(jsonPath("$.equipaVencedoraId").value(benfica.getId()))
        .andReturn();

        //apos definir estatistica 

        //testar jogador com mais vermelhos
        mockMvc.perform(get("/api/jogadores/mais-vermelhos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(jogadoresMock.get(7).getId()))
                .andReturn();

        //testar media de golos do jogador 

        //jogador que marcou golo
        mockMvc.perform(get("/api/jogadores/" + jogadoresMock.get(0).getNome() + "/media-golos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1))
                .andReturn(); 

        //jogador que nao marcou golo
        mockMvc.perform(get("/api/jogadores/" + jogadoresMock.get(6).getNome() + "/media-golos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(0))
                .andReturn(); 

                
        //testar equipas com mais cartoes
        mockMvc.perform(get("/api/equipas/mais-cartoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(sporting.getId())) //jogador (no indice7) do sporting teve vermelho
                .andReturn();
                
        //testar equipas com mais vitorias
        mockMvc.perform(get("/api/equipas/mais-vitorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(benfica.getId())) //benfica ganhou o jogo com golo do jogador no indice 0
                .andReturn(); 
        
        //testar arbitros com mais jogos
        mockMvc.perform(get("/api/arbitros/mais-jogos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(arbitrosMock.get(0).getId())) //arbitro no indice 0 do beforeEach
                .andExpect(jsonPath("$[1].id").value(arbitrosMock.get(1).getId())) //arbitro no indice 1 do beforeEach
                .andReturn();
     }  

     @Test
    void testUpdateJogo() throws Exception {
         // Dados do local atualizado 
         LocalDto localDto = new LocalDto("Estádio Novo",
         LocalDateTime.of(2025, 4, 24, 20, 30),
         LocalDateTime.of(2025, 4, 24, 21, 30));

        // Cria a lista de JogadorNoJogoDto com 10 jogadores, com 1 guarda-redes em cada equipa
        List<JogadorNoJogoDto> jogadoresNoJogo = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
                String posicao;
                
                if (i == 0 || i == 5) { // Jogador 0 e jogador 5 serão GUARDA_REDES
                        posicao = "GUARDA_REDES";
                } else {
                        posicao = "AVANCADO"; // Os restantes serão AVANCADO
                }

                JogadorNoJogoDto jogadorNoJogoDto = new JogadorNoJogoDto(
                        jogadoresMock.get(i).getId(),
                        posicao
                );
                jogadoresNoJogo.add(jogadorNoJogoDto);
        }


        // Árbitros
        ArbitrosNoJogoDto arbitrosDto = new ArbitrosNoJogoDto(
                List.of(arbitrosMock.get(0).getId(), arbitrosMock.get(1).getId()), 
                arbitrosMock.get(0).getId()
        );

        // Criação com a ordem das equipas invertida
        JogoDto jogoDto = new JogoDto(
                localDto,
                sporting.getId(),
                benfica.getId(),
                jogadoresNoJogo,
                arbitrosDto
        );


        // testar create jogo
        MvcResult result = mockMvc.perform(put("/api/jogos/"+jogoMock.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jogoDto)))
                        .andExpect(status().isOk())
                        .andReturn();

                
        String response = result.getResponse().getContentAsString();
        Long id = JsonPath.parse(response).read("$.id", Long.class);

        //testar getJogoById
        mockMvc.perform(get("/api/jogos/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.local.local").value("Estádio Novo"))
                .andExpect(jsonPath("$.local.dataHoraInicio").value("2025-04-24T20:30:00"))
                .andExpect(jsonPath("$.local.dataHoraFim").value("2025-04-24T21:30:00"))
                .andExpect(jsonPath("$.equipaCasaId").value(sporting.getId()))
                .andExpect(jsonPath("$.equipaVisitanteId").value(benfica.getId()));
    }

    @Test
    void testDeleteJogo() throws Exception {
        // Deletar o jogo criado no setUp
        mockMvc.perform(delete("/api/jogos/" + jogoMock.getId()))
                .andExpect(status().isOk())
                .andReturn();

        // Verificar se o jogo foi realmente deletado
        mockMvc.perform(get("/api/jogos/" + jogoMock.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }
                
}