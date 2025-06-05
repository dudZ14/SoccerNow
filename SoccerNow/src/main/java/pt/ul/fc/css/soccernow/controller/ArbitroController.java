package pt.ul.fc.css.soccernow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import pt.ul.fc.css.soccernow.dto.ArbitroDto;
import pt.ul.fc.css.soccernow.dto.ArbitroDtoGet;
import pt.ul.fc.css.soccernow.handlers.ArbitroHandler;

import java.util.List;

@RestController
@RequestMapping("/api/arbitros")
public class ArbitroController {

    @Autowired
    private ArbitroHandler arbitroHandler;

    @GetMapping
    @Operation(summary = "Obter todos os árbitros", description = "Retorna uma lista com todos os árbitros.")
    public ResponseEntity<List<ArbitroDtoGet>> getAllArbitros() {
        List<ArbitroDtoGet> arbitros = arbitroHandler.getAllArbitros();
        return ResponseEntity.ok(arbitros);
    }

    @PostMapping
    @Operation(summary = "Criar árbitro", description = "Cria um novo árbitro com as informações fornecidas.")
    public ResponseEntity<ArbitroDtoGet> createArbitro(@RequestBody ArbitroDto arbitroDto) {
        ArbitroDtoGet createdArbitro = arbitroHandler.createArbitro(arbitroDto);
        return ResponseEntity.ok(createdArbitro);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter árbitro por ID", description = "Retorna as informações de um árbitro específico pelo ID.")
    public ResponseEntity<ArbitroDtoGet> getArbitroById(@PathVariable Long id) {
        ArbitroDtoGet arbitro = arbitroHandler.getArbitroById(id);
        if (arbitro != null) {
            return ResponseEntity.ok(arbitro);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover árbitro", description = "Remove um árbitro pelo ID fornecido e retorna os dados do árbitro removido.")
    public ResponseEntity<ArbitroDtoGet> deleteArbitro(@PathVariable Long id) {
        ArbitroDtoGet deletedArbitro = arbitroHandler.deleteArbitro(id);
        if (deletedArbitro != null) {
            return ResponseEntity.ok(deletedArbitro); // Retorna o DTO do árbitro removido
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar árbitro", description = "Atualiza as informações de um árbitro pelo ID fornecido.")
    public ResponseEntity<ArbitroDtoGet> updateArbitro(@PathVariable Long id, @RequestBody ArbitroDto arbitroDto) {
        ArbitroDtoGet updatedArbitro = arbitroHandler.updateArbitro(id, arbitroDto);
        if (updatedArbitro != null) {
            return ResponseEntity.ok(updatedArbitro);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/mais-jogos")
    @Operation(summary = "Árbitro com mais jogos, em caso de empate retorna árbitros (tem de ter pelo menos 1 jogo)", description = "Retorna os árbitros que oficiaram o maior número de jogos.")
    public ResponseEntity<List<ArbitroDtoGet>> getArbitrosComMaisJogos() {
        List<ArbitroDtoGet> arbitrosComMaisJogos = arbitroHandler.getArbitrosComMaisJogos();
        return ResponseEntity.ok(arbitrosComMaisJogos);
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Árbitros por nome", description = "Retorna os árbitros com o nome igual ao nome fornecido.")
    public ResponseEntity<List<ArbitroDtoGet>> getArbitrosPorNome(@PathVariable String nome) {
        List<ArbitroDtoGet> arbitros = arbitroHandler.getArbitrosPorNome(nome);
        return ResponseEntity.ok(arbitros);
    }

    @GetMapping("/jogos/{num}")
    @Operation(summary = "Árbitros por número de jogos realizados", description = "Retorna os árbitros com o número de jogos realizados igual ao fornecido.")
    public ResponseEntity<List<ArbitroDtoGet>> getArbitrosPorNumeroDeJogos(@PathVariable int num) {
        List<ArbitroDtoGet> arbitros = arbitroHandler.getArbitrosPorJogosRealizados(num);
        return ResponseEntity.ok(arbitros);
    }

    @GetMapping("/cartoes/{num}")
    @Operation(summary = "Árbitros por número de cartões mostrados", description = "Retorna os árbitros com o número de carões mostrados igual ao fornecido.")
    public ResponseEntity<List<ArbitroDtoGet>> getArbitrosPorNumeroDeCartoes(@PathVariable int num) {
        List<ArbitroDtoGet> arbitros = arbitroHandler.getArbitrosPorCartoesMostrados(num);
        return ResponseEntity.ok(arbitros);
    }

}