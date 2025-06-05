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

import pt.ul.fc.css.soccernow.dto.JogadorDto;
import pt.ul.fc.css.soccernow.entities.Jogador;
import pt.ul.fc.css.soccernow.enumerados.Posicao;
import pt.ul.fc.css.soccernow.repository.JogadorRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // uses application-test.yml
@Transactional
public class JogadorTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JogadorRepository jogadorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        jogadorRepository.deleteAll(); // reset state before each test
        Jogador jogador = new Jogador();
        jogador.setNome("Test Player");
        jogador.setEmail("test@example.com");
        jogador.setPassword("1234");
        jogador.setCertificado(true);
        jogador.setPosicaoPreferida(Posicao.DEFESA);
        jogadorRepository.save(jogador);
    }

    @Test
    void testGetAllJogadores() throws Exception {
        mockMvc.perform(get("/api/jogadores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Test Player"));
    }

    @Test
    void testCreateJogador() throws Exception {
        JogadorDto dto = new JogadorDto("Novo Jogador", "novo@email.com", "password", true, Posicao.DEFESA);

        mockMvc.perform(post("/api/jogadores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Novo Jogador"));
    }

    @Test
    void testJogadorNomeVazio() throws Exception {
        JogadorDto dto = new JogadorDto("", "novo@email.com", "password", true,Posicao.DEFESA);

        mockMvc.perform(post("/api/jogadores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("O nome do jogador não pode ser vazio"));
    }

    @Test
    void testEmailInvalido() throws Exception {
        JogadorDto dto = new JogadorDto("João", "novoemail.com", "password", true,Posicao.DEFESA);

        mockMvc.perform(post("/api/jogadores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("O email tem de ter @"));
    }

    @Test
    void testGetJogadorById() throws Exception {
        Jogador jogador = jogadorRepository.findAll().get(0);

        mockMvc.perform(get("/api/jogadores/" + jogador.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Test Player"));
    }

    @Test
    void testDeleteJogador() throws Exception {
        Jogador jogador = jogadorRepository.findAll().get(0);

        mockMvc.perform(delete("/api/jogadores/" + jogador.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/jogadores/" + jogador.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateJogador() throws Exception {
        // Pega o jogador inserido no setUp
        Jogador jogador = jogadorRepository.findAll().get(0);
    
        // Cria um DTO com dados atualizados
        JogadorDto updatedDto = new JogadorDto("Updated Player", "updated@example.com", "newpassword", false,Posicao.DEFESA);
    
        mockMvc.perform(put("/api/jogadores/" + jogador.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Updated Player"))
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.password").value("newpassword"))
                .andExpect(jsonPath("$.temCertificado").value(false));
    }

}

