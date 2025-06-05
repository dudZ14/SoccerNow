package pt.ul.fc.css.soccernow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import pt.ul.fc.css.soccernow.dto.JogadorDto;
import pt.ul.fc.css.soccernow.dto.JogadorDtoGet;
import pt.ul.fc.css.soccernow.enumerados.Posicao;
import pt.ul.fc.css.soccernow.handlers.JogadorHandler;

import java.util.List;

@RestController
@RequestMapping("/api/jogadores")
public class JogadorController {

    @Autowired
    private JogadorHandler jogadorHandler;

    @GetMapping
    @Operation(summary = "Obter todos os jogadores", description = "Retorna uma lista com todos os jogadores.")
    public ResponseEntity<List<JogadorDtoGet>> getAllJogadores() {
        return ResponseEntity.ok(jogadorHandler.getAllJogadores());
    }

    @PostMapping
    @Operation(summary = "Criar jogador", description = "Cria um novo jogador com as informações fornecidas.")
    public ResponseEntity<JogadorDtoGet> createJogador(@RequestBody JogadorDto jogadorDto) {
        return ResponseEntity.ok(jogadorHandler.createJogador(jogadorDto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter jogador por ID", description = "Retorna as informações de um jogador específico pelo ID.")
    public ResponseEntity<JogadorDtoGet> getJogadorById(@PathVariable Long id) {
        JogadorDtoGet jogador = jogadorHandler.getJogadorById(id);
        return jogador != null ? ResponseEntity.ok(jogador) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover jogador", description = "Remove um jogador pelo ID fornecido.")
    public ResponseEntity<JogadorDtoGet> deleteJogador(@PathVariable Long id) {
        JogadorDtoGet jogador = jogadorHandler.deleteJogador(id);
        return jogador != null ? ResponseEntity.ok(jogador) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar jogador", description = "Atualiza as informações de um jogador pelo ID fornecido.")
    public ResponseEntity<JogadorDtoGet> updateJogador(@PathVariable Long id, @RequestBody JogadorDto jogadorDto) {
        JogadorDtoGet jogador = jogadorHandler.updateJogador(id, jogadorDto);
        return jogador != null ? ResponseEntity.ok(jogador) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{nome}/media-golos")
    @Operation(summary = "Média de golos por jogo", description = "Calcula a média de golos marcados por jogo pelos jogadores com o nome fornecido.")
    public ResponseEntity<Double> getMediaGolosPorJogo(@PathVariable String nome) {
        return ResponseEntity.ok(jogadorHandler.calcularMediaGolosPorJogo(nome));
    }

    @GetMapping("/mais-vermelhos")
    @Operation(summary = "Jogadores com mais vermelhos", description = "Retorna os jogadores com mais cartões vermelhos (mínimo de 1).")
    public ResponseEntity<List<JogadorDtoGet>> getJogadoresComMaisCartoesVermelhos() {
        return ResponseEntity.ok(jogadorHandler.getJogadoresComMaisCartoesVermelhos());
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Jogadores por nome", description = "Devolve uma lista de jogadores com o nome fornecido.")
    public ResponseEntity<List<JogadorDtoGet>> getJogadorPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(jogadorHandler.getJogadoresPorNome(nome));
    }

    @GetMapping("/cartoes-total/{num}")
    @Operation(summary = "Jogadores por número de cartões", description = "Devolve uma lista de jogadores com o número de cartões fornecido.")
    public ResponseEntity<List<JogadorDtoGet>> getJogadorPorNumeroDeCartoes(@PathVariable int num) {
        return ResponseEntity.ok(jogadorHandler.getJogadoresPorNumeroDeCartoes(num));
    }

    @GetMapping("/jogos/{num}")
    @Operation(summary = "Jogadores por número de jogos realizados", description = "Devolve uma lista de jogadores com o número de jogos realizados fornecido.")
    public ResponseEntity<List<JogadorDtoGet>> getJogadorPorNumeroDeJogos(@PathVariable int num) {
        return ResponseEntity.ok(jogadorHandler.getJogadoresPorNumeroDeJogos(num));
    }

    @GetMapping("/golos/{num}")
    @Operation(summary = "Jogadores por número de golos", description = "Devolve uma lista de jogadores com o número de golos fornecido.")
    public ResponseEntity<List<JogadorDtoGet>> getJogadorPorNumeroDeGolos(@PathVariable int num) {
        return ResponseEntity.ok(jogadorHandler.getJogadoresPorNumeroDeGolos(num));
    }

    @GetMapping("/posicao/{pos}")
    @Operation(summary = "Jogadores por posição", description = "Devolve uma lista de jogadores que jogam na posição fornecida.")
    public ResponseEntity<List<JogadorDtoGet>> getJogadorPorPosicao(@PathVariable Posicao pos) {
        return ResponseEntity.ok(jogadorHandler.getJogadoresPorPosicao(pos));
    }
}
