package pt.ul.fc.css.soccernow;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import pt.ul.fc.css.soccernow.dto.ArbitroDto;
import pt.ul.fc.css.soccernow.entities.Arbitro;
import pt.ul.fc.css.soccernow.repository.ArbitroRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ArbitroTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArbitroRepository arbitroRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        arbitroRepository.deleteAll();

        Arbitro arbitro = new Arbitro();
        arbitro.setNome("Árbitro Inicial");
        arbitro.setEmail("arbitro@teste.com");
        arbitro.setPassword("senha123");
        arbitro.setCertificado(true);
        arbitro.setAnosDeExperiencia(10);
        arbitroRepository.save(arbitro);
    }

    @Test
    void testGetAllArbitros() throws Exception {
        mockMvc.perform(get("/api/arbitros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Árbitro Inicial"));
    }

    @Test
    void testCreateArbitro() throws Exception {
        ArbitroDto dto = new ArbitroDto("Novo Árbitro", "novo@ref.com", "senha", true, 7);

        mockMvc.perform(post("/api/arbitros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Novo Árbitro"))
                .andExpect(jsonPath("$.temCertificado").value(true))
                .andExpect(jsonPath("$.anosDeExperiencia").value(7));
    }

    @Test
    void testEmailInvalido() throws Exception {
        ArbitroDto dto = new ArbitroDto("Novo Árbitro", "novoref.com", "senha", true, 7);

        mockMvc.perform(post("/api/arbitros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("O email tem de ter @"));
    }

    @Test
    void testArbitroNomeVazio() throws Exception {
        ArbitroDto dto = new ArbitroDto("", "novo@ref.com", "senha", true, 7);

        mockMvc.perform(post("/api/arbitros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("O nome do árbitro não pode ser vazio"));
    }

    @Test
    void testGetArbitroById() throws Exception {
        Arbitro arbitro = arbitroRepository.findAll().get(0);

        mockMvc.perform(get("/api/arbitros/" + arbitro.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Árbitro Inicial"));
    }

    @Test
    void testDeleteArbitro() throws Exception {
        Arbitro arbitro = arbitroRepository.findAll().get(0);

        mockMvc.perform(delete("/api/arbitros/" + arbitro.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/arbitros/" + arbitro.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateArbitro() throws Exception {
        Arbitro arbitro = arbitroRepository.findAll().get(0);

        ArbitroDto updatedDto = new ArbitroDto("Árbitro Atualizado", "update@ref.com", "novaSenha", false, 15);

        mockMvc.perform(put("/api/arbitros/" + arbitro.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Árbitro Atualizado"))
                .andExpect(jsonPath("$.email").value("update@ref.com"))
                .andExpect(jsonPath("$.password").value("novaSenha"))
                .andExpect(jsonPath("$.temCertificado").value(false))
                .andExpect(jsonPath("$.anosDeExperiencia").value(15));
    }
}
