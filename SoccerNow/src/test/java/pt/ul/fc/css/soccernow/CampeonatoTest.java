package pt.ul.fc.css.soccernow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

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

import pt.ul.fc.css.soccernow.dto.ArbitroDto;
import pt.ul.fc.css.soccernow.dto.ArbitrosNoJogoDto;
import pt.ul.fc.css.soccernow.dto.CampeonatoDto;
import pt.ul.fc.css.soccernow.dto.CartaoJogadorDto;
import pt.ul.fc.css.soccernow.dto.EquipaDto;
import pt.ul.fc.css.soccernow.dto.EstatisticaDto;
import pt.ul.fc.css.soccernow.dto.GoloDto;
import pt.ul.fc.css.soccernow.dto.JogadorDto;
import pt.ul.fc.css.soccernow.dto.JogadorNoJogoDto;
import pt.ul.fc.css.soccernow.dto.JogoDto;
import pt.ul.fc.css.soccernow.dto.LocalDto;
import pt.ul.fc.css.soccernow.enumerados.Posicao;
import pt.ul.fc.css.soccernow.exceptions.CampeonatoInvalidoException;
import pt.ul.fc.css.soccernow.exceptions.NomeVazioException;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class CampeonatoTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    
    List<Long> jogadorIds = new ArrayList<>();
    List<Long> jogoIds = new ArrayList<>();
    List<Long> arbitroIds;
    List<Long> equipaIds = new ArrayList<>();
    
    
    @BeforeEach
    void setUp() throws Exception {

        // Criar 40 jogadores (8 equipas * 5 jogadores cada)
        for (int i = 1; i <= 40; i++) {
            JogadorDto jogadorDto = new JogadorDto(
                "Jogador" + i,
                "jogador" + i + "@email.com",
                "senha",
                true,
                Posicao.DEFESA
            );

            MvcResult result = mockMvc.perform(post("/api/jogadores")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(jogadorDto)))
                    .andExpect(status().isOk())
                    .andReturn();

            String response = result.getResponse().getContentAsString();
            Long id = JsonPath.parse(response).read("$.id", Long.class);
            jogadorIds.add(id);
        }


        // Criar 8 equipas, cada uma com 5 jogadores
        for (int i = 0; i < 8; i++) {
            String nomeEquipa = "Equipa" + (i + 1);
            List<Long> jogadoresEquipa = new ArrayList<>();

            // Associar 5 jogadores consecutivos por equipa
            int startIndex = i * 5;
            for (int j = startIndex; j < startIndex + 5; j++) {
                jogadoresEquipa.add(jogadorIds.get(j));
            }

            EquipaDto equipaDto = new EquipaDto(nomeEquipa, jogadoresEquipa);

            MvcResult result = mockMvc.perform(post("/api/equipas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(equipaDto)))
                    .andExpect(status().isOk())
                    .andReturn();

            Long equipaId = JsonPath.parse(result.getResponse().getContentAsString()).read("$.id", Long.class);
            equipaIds.add(equipaId);
        }

        // Criar 2 árbitros via endpoint
        arbitroIds = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            ArbitroDto arbitroDto = new ArbitroDto("Árbitro" + i, "arbitro" + i + "@email.com", "senha", true, 5 + i);

            MvcResult result = mockMvc.perform(post("/api/arbitros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(arbitroDto)))
                        .andExpect(status().isOk())
                        .andReturn();

            Long id = JsonPath.parse(result.getResponse().getContentAsString()).read("$.id", Long.class);
            arbitroIds.add(id);
        }

       
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
                        jogadorIds.get(i),
                        posicao
                );
                jogadoresNoJogo.add(jogadorNoJogoDto);
        }


        // Árbitros
       ArbitrosNoJogoDto arbitrosDto = new ArbitrosNoJogoDto(
            List.of(arbitroIds.get(0), arbitroIds.get(1)),
            arbitroIds.get(0)
        );


        JogoDto jogoDto = new JogoDto(
                localDto,
                equipaIds.get(0),
                equipaIds.get(1),
                jogadoresNoJogo,
                arbitrosDto
        );


        // criar jogo
        MvcResult jogoResult = mockMvc.perform(post("/api/jogos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jogoDto)))
                        .andReturn();
        
        Long jogoId = JsonPath.parse(jogoResult.getResponse().getContentAsString()).read("$.id", Long.class);
        jogoIds.add(jogoId);
    }

    @Test
    void testCreateCampeonato() throws Exception {
        CampeonatoDto campeonatoDto = new CampeonatoDto("Campeonato Teste", "Futebol", equipaIds, jogoIds);

        mockMvc.perform(post("/api/campeonatos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(campeonatoDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.modalidade").value("Futebol"))
        .andExpect(jsonPath("$.equipaIds.length()").value(equipaIds.size()))
        .andExpect(jsonPath("$.jogoIds.length()").value(jogoIds.size()))
        .andExpect(jsonPath("$.numeroJogos").value(jogoIds.size()))
        .andExpect(jsonPath("$.numeroJogosFeitos").value(0));
    }

    @Test
    void testUpdateCampeonato() throws Exception {
        // Criar campeonato inicialmente
        CampeonatoDto campeonatoDto = new CampeonatoDto("Campeonato Teste", "Futebol", equipaIds, jogoIds);

        MvcResult result = mockMvc.perform(post("/api/campeonatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(campeonatoDto)))
                .andExpect(status().isOk())
                .andReturn();

        Long campeonatoId = JsonPath.parse(result.getResponse().getContentAsString()).read("$.id", Long.class);

        // Atualizar nome e modalidade
        CampeonatoDto updateDto = new CampeonatoDto("Campeonato Atualizado", "Basket", equipaIds, jogoIds);

        mockMvc.perform(put("/api/campeonatos/" + campeonatoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Campeonato Atualizado"))
                .andExpect(jsonPath("$.modalidade").value("Basket"));
    }

    @Test
    void testGetAllCampeonatos() throws Exception {
        // Criar campeonato
        CampeonatoDto campeonatoDto = new CampeonatoDto("Campeonato A", "Futebol", equipaIds, jogoIds);
        mockMvc.perform(post("/api/campeonatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(campeonatoDto)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/campeonatos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("Campeonato A"));
    }

    @Test
    void testGetCampeonatoById() throws Exception {
        CampeonatoDto campeonatoDto = new CampeonatoDto("Campeonato X", "Futebol", equipaIds, jogoIds);

        MvcResult result = mockMvc.perform(post("/api/campeonatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(campeonatoDto)))
                .andReturn();

        Long campeonatoId = JsonPath.parse(result.getResponse().getContentAsString()).read("$.id", Long.class);

        mockMvc.perform(get("/api/campeonatos/" + campeonatoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Campeonato X"));
    }

    @Test
    void testFiltrarCampeonatoPorNome() throws Exception {
        CampeonatoDto campeonatoDto = new CampeonatoDto("Campeonato Filtrado", "Futebol", equipaIds, jogoIds);

        mockMvc.perform(post("/api/campeonatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(campeonatoDto)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/campeonatos/filtro/nome")
                .param("nome", "Filtrado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("Campeonato Filtrado"));
    }

    @Test
    void testFiltrarCampeonatoPorEquipa() throws Exception {
        CampeonatoDto campeonatoDto = new CampeonatoDto("Campeonato Equipa", "Futebol", equipaIds, jogoIds);

        mockMvc.perform(post("/api/campeonatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(campeonatoDto)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/campeonatos/filtro/equipa/" + equipaIds.get(0)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Campeonato Equipa"));
    }

    @Test
    void testFiltrarMinJogosPorRealizar() throws Exception {
        CampeonatoDto campeonatoDto = new CampeonatoDto("Campeonato Jogos", "Futebol", equipaIds, jogoIds);

        mockMvc.perform(post("/api/campeonatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(campeonatoDto)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/campeonatos/filtro/jogos-por-realizar")
                .param("minJogos", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Campeonato Jogos"));
    }

    @Test
    void testFiltrarJogosRealizados() throws Exception {
        CampeonatoDto campeonatoDto = new CampeonatoDto("Campeonato Jogos", "Futebol", equipaIds, jogoIds);

        mockMvc.perform(post("/api/campeonatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(campeonatoDto)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/campeonatos/filtro/jogos-realizados")
                .param("minJogos", "0"))    
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Campeonato Jogos"));

        // 3. Realizar o jogo
        GoloDto goloDto = new GoloDto(jogadorIds.get(0), 10); 
        CartaoJogadorDto cartaoJogadorDto = new CartaoJogadorDto(jogadorIds.get(0), "VERMELHO"); 
        EstatisticaDto estatisticaDto = new EstatisticaDto(List.of(goloDto), List.of(cartaoJogadorDto));


        mockMvc.perform(post("/api/jogos/resultado/" + jogoIds.get(0))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(estatisticaDto)))
                .andExpect(status().isOk());

        // 4. Confirmar que o campeonato NÃO aparece mais quando filtramos por jogos por realizar
        mockMvc.perform(get("/api/campeonatos/filtro/jogos-por-realizar")
                .param("minJogos", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        // 4. Confirmar que o campeonato quando filtramos por jogos realizados
        mockMvc.perform(get("/api/campeonatos/filtro/jogos-realizados")
                .param("minJogos", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Campeonato Jogos"))
                .andExpect(jsonPath("$[0].nome").value("Campeonato Jogos"))
                .andExpect(jsonPath("$[0].pontuacoes.Equipa1").value(3))
                .andExpect(jsonPath("$[0].pontuacoes.Equipa2").value(0));
        
    }

    @Test
    void testCancelarJogo() throws Exception {
        CampeonatoDto campeonatoDto = new CampeonatoDto("Campeonato Cancelamento", "Futebol", equipaIds, jogoIds);

        MvcResult result = mockMvc.perform(post("/api/campeonatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(campeonatoDto)))
                .andExpect(status().isOk())
                .andReturn();

        Long campeonatoId = JsonPath.parse(result.getResponse().getContentAsString()).read("$.id", Long.class);
        Long jogoId = jogoIds.get(0);

        // Cancela o jogo
        mockMvc.perform(post("/api/campeonatos/" + campeonatoId + "/cancelar-jogo/" + jogoId))
                .andExpect(status().isOk());

        // Faz GET para verificar o estado do jogo
        mockMvc.perform(get("/api/jogos/" + jogoId))
                .andExpect(status().isOk());
        
        // Faz GET para verificar o estado do jogo = CANCELADO
        mockMvc.perform(get("/api/jogos/" + jogoId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.estado").value("CANCELADO"));
    }

    @Test
    void testCreateCampeonato_NomeVazio() throws Exception {
        CampeonatoDto dto = new CampeonatoDto("  ", "Futebol", equipaIds, null); // nome vazio

        mockMvc.perform(post("/api/campeonatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                    assertTrue(result.getResolvedException() instanceof NomeVazioException))
                .andExpect(result ->
                    assertEquals("O nome do campeonato não pode ser vazio", result.getResolvedException().getMessage()));
    }

    @Test
    void testCreateCampeonato_ModalidadeVazia() throws Exception {
        CampeonatoDto dto = new CampeonatoDto("Liga", "   ", equipaIds, null); // modalidade vazia

        mockMvc.perform(post("/api/campeonatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                    assertTrue(result.getResolvedException() instanceof NomeVazioException))
                .andExpect(result ->
                    assertEquals("O nome da modalidade não pode ser vazio", result.getResolvedException().getMessage()));
    }

    @Test
    void testCreateCampeonato_MenosDe8Equipas() throws Exception {
        List<Long> poucasEquipas = equipaIds.subList(0, 5);
        CampeonatoDto dto = new CampeonatoDto("Liga", "Futebol", poucasEquipas, null);

        mockMvc.perform(post("/api/campeonatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                    assertTrue(result.getResolvedException() instanceof CampeonatoInvalidoException))
                .andExpect(result ->
                    assertEquals("Um campeonato deve ter pelo menos 8 equipas.", result.getResolvedException().getMessage()));
    }


    @Test
    void testDeleteCampeonato_Sucesso() throws Exception {
        // criar campeonato e encerrar jogo
        CampeonatoDto campeonatoDto = new CampeonatoDto("Campeonato Delete", "Futebol", equipaIds, jogoIds);

        mockMvc.perform(post("/api/campeonatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(campeonatoDto)))
                .andExpect(status().isOk());

        EstatisticaDto estatisticaDto = new EstatisticaDto(
                List.of(new GoloDto(jogadorIds.get(0), 1)),
                List.of());

        mockMvc.perform(post("/api/jogos/resultado/" + jogoIds.get(0))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(estatisticaDto)))
                .andExpect(status().isOk());

        // buscar id do campeonato
        MvcResult result = mockMvc.perform(get("/api/campeonatos/filtro/jogos-realizados")
                .param("minJogos", "1"))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        long campeonatoId = JsonPath.parse(json).read("$[0].id", Long.class);


        // deletar
        mockMvc.perform(delete("/api/campeonatos/" + campeonatoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Campeonato Delete"));
    }
    @Test
    void testUpdateCampeonatoTerminado() throws Exception {
        // criar campeonato e encerrar jogo
        CampeonatoDto campeonatoDto = new CampeonatoDto("Campeonato Delete", "Futebol", equipaIds, jogoIds);

        mockMvc.perform(post("/api/campeonatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(campeonatoDto)))
                .andExpect(status().isOk());

        EstatisticaDto estatisticaDto = new EstatisticaDto(
                List.of(new GoloDto(jogadorIds.get(0), 1)),
                List.of());

        mockMvc.perform(post("/api/jogos/resultado/" + jogoIds.get(0))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(estatisticaDto)))
                .andExpect(status().isOk());

        // buscar id do campeonato
        MvcResult result = mockMvc.perform(get("/api/campeonatos/filtro/jogos-realizados")
                .param("minJogos", "1"))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        long campeonatoId = JsonPath.parse(json).read("$[0].id", Long.class);


        // atualizar
        CampeonatoDto updateDto = new CampeonatoDto("Campeonato Atualizado", "Basket", equipaIds, jogoIds);
        mockMvc.perform(put("/api/campeonatos/" + campeonatoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result2 ->
                    assertTrue(result2.getResolvedException() instanceof CampeonatoInvalidoException))
                .andExpect(result2 ->
                    assertEquals("Não é possível atualizar um campeonato que já terminou.",
                                result2.getResolvedException().getMessage()));
    }

    @Test
    void testDeleteCampeonato_EmAndamento() throws Exception {
        CampeonatoDto campeonatoDto = new CampeonatoDto("Campeonato Ativo", "Futebol", equipaIds, jogoIds);

        mockMvc.perform(post("/api/campeonatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(campeonatoDto)))
                .andExpect(status().isOk());

        // buscar id
        MvcResult result = mockMvc.perform(get("/api/campeonatos"))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        long campeonatoId = JsonPath.parse(json).read("$[0].id", Long.class);


        mockMvc.perform(delete("/api/campeonatos/" + campeonatoId))
                .andExpect(status().isBadRequest())
                .andExpect(result2 ->
                    assertTrue(result2.getResolvedException() instanceof CampeonatoInvalidoException))
                .andExpect(result2 ->
                    assertEquals("Não é possível eliminar um campeonato que ainda está em andamento.",
                                result2.getResolvedException().getMessage()));
    }



}
