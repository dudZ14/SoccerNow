package pt.ul.fc.css.soccernow;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import pt.ul.fc.css.soccernow.dto.EquipaDto;
import pt.ul.fc.css.soccernow.dto.JogadorDto;
import pt.ul.fc.css.soccernow.entities.Equipa;
import pt.ul.fc.css.soccernow.entities.Jogador;
import pt.ul.fc.css.soccernow.enumerados.Posicao;
import pt.ul.fc.css.soccernow.repository.EquipaRepository;
import pt.ul.fc.css.soccernow.repository.JogadorRepository;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class EquipaTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EquipaRepository equipaRepository;

    @Autowired
    private JogadorRepository jogadorRepository;

    private Jogador jogador;
    private Long equipaId;

    @BeforeEach
    void setUp() throws JsonProcessingException, Exception {
        
        // Save and retrieve the player to ensure it's managed by the persistence context
        JogadorDto jogadorDto = new JogadorDto("Jogador Teste", "jogador@teste.com", "senha", true, Posicao.DEFESA);

        MvcResult result = mockMvc.perform(post("/api/jogadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jogadorDto)))
                        .andExpect(status().isOk())
                        .andReturn();
        String response = result.getResponse().getContentAsString();
        Long id = JsonPath.parse(response).read("$.id", Long.class);
        jogador = jogadorRepository.findById(id).orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

        EquipaDto equipaDto = new EquipaDto("Equipa Teste", List.of(id));

        MvcResult benficaResult = mockMvc.perform(post("/api/equipas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(equipaDto)))
                        .andExpect(status().isOk())
                        .andReturn();

        String response2 = benficaResult.getResponse().getContentAsString();
        this.equipaId = JsonPath.parse(response2).read("$.id", Long.class);
        System.out.println("ID da equipa: " + equipaId);
    }

    @Test
    void testGetAllEquipas() throws Exception {
        mockMvc.perform(get("/api/equipas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Equipa Teste"));
    }

    @Test
    void testCreateEquipa() throws Exception {
        EquipaDto dto = new EquipaDto("Nova Equipa", List.of(jogador.getId()));

        mockMvc.perform(post("/api/equipas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nova Equipa"));
    }

    @Test
    void testEquipaNomeVazio() throws Exception {
        EquipaDto dto = new EquipaDto("", List.of(jogador.getId()));

        mockMvc.perform(post("/api/equipas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string("O nome da equipa não pode ser vazio"));
    }

    @Test
    void testGetEquipaById() throws Exception {
        Equipa equipa = equipaRepository.findAll().get(0);

        mockMvc.perform(get("/api/equipas/" + equipa.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Equipa Teste"));
    }

    @Test
    void testGetEquipasMenosde5Jogadores() throws Exception {
        //testar equipas com menos de 5 jogadores
        mockMvc.perform(get("/api/equipas/menos-de-cinco-jogadores"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1)) //existe uma equipa com menos de 5 jogadores
        .andExpect(jsonPath("$[0].nome").value("Equipa Teste"))
        .andReturn();
    }

    @Test
    void testUpdateEquipa() throws Exception {
        EquipaDto dto = new EquipaDto("Equipa Atualizada", List.of(jogador.getId()));

        mockMvc.perform(put("/api/equipas/" + equipaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Equipa Atualizada"));
    }

     @Test
    void testDeleteEquipa() throws Exception {
        EquipaDto dto = new EquipaDto("Equipa a Eliminar", List.of(jogador.getId()));

        MvcResult result = mockMvc.perform(post("/api/equipas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Equipa a Eliminar"))
                .andReturn();

        Long id = JsonPath.parse(result.getResponse().getContentAsString()).read("$.id", Long.class);

        mockMvc.perform(delete("/api/equipas/" + id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/equipas/" + id))
                .andExpect(status().isNotFound());
    }  
}
