package pt.ul.fc.css.soccernow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import pt.ul.fc.css.soccernow.dto.EquipaDto;
import pt.ul.fc.css.soccernow.dto.EquipaDtoGet;
import pt.ul.fc.css.soccernow.enumerados.Posicao;
import pt.ul.fc.css.soccernow.handlers.EquipaHandler;

import java.util.List;

@RestController
@RequestMapping("/api/equipas")
public class EquipaController {

    @Autowired
    private EquipaHandler equipaHandler;

    @GetMapping
    @Operation(summary = "Obter todas as equipas", description = "Retorna uma lista com todas as equipas.")
    public ResponseEntity<List<EquipaDtoGet>> getAllEquipas() {
        List<EquipaDtoGet> equipas = equipaHandler.getAllEquipas();
        return ResponseEntity.ok(equipas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter equipa por ID", description = "Retorna as informações de uma equipa específica pelo ID.")
    public ResponseEntity<EquipaDtoGet> getEquipaById(@PathVariable Long id) {
        EquipaDtoGet equipa = equipaHandler.getEquipaById(id);
        if (equipa != null) {
            return ResponseEntity.ok(equipa);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Criar equipa", description = "Cria uma nova equipa com as informações fornecidas.")
    public ResponseEntity<EquipaDtoGet> createEquipa(@RequestBody EquipaDto equipaDto) {
        EquipaDtoGet createdEquipa = equipaHandler.createEquipa(equipaDto);
        return ResponseEntity.ok(createdEquipa);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar equipa", description = "Atualiza as informações de uma equipa pelo ID fornecido.")
    public ResponseEntity<EquipaDtoGet> updateEquipa(@PathVariable Long id, @RequestBody EquipaDto equipaDto) {
        EquipaDtoGet updatedEquipa = equipaHandler.updateEquipa(id, equipaDto);
        if (updatedEquipa != null) {
            return ResponseEntity.ok(updatedEquipa);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover equipa", description = "Remove uma equipa pelo ID fornecido.")
    public ResponseEntity<EquipaDtoGet> deleteEquipa(@PathVariable Long id) {
        EquipaDtoGet deletedEquipa = equipaHandler.deleteEquipa(id);
        return ResponseEntity.ok(deletedEquipa); // Retorna o DTO da equipa eliminada
        
    }

    @GetMapping("/menos-de-cinco-jogadores")
    @Operation(summary = "Listar equipas com menos de 5 jogadores", description = "Retorna uma lista de equipas que possuem menos de 5 jogadores.")
    public ResponseEntity<List<EquipaDtoGet>> getEquipasComMenosDeCincoJogadores() {
        List<EquipaDtoGet> equipas = equipaHandler.getEquipasComMenosDeCincoJogadores();
        return ResponseEntity.ok(equipas);
    }

    @GetMapping("/mais-cartoes")
    @Operation(summary = "Equipas com mais cartões (tem de ter pelo menos 1 cartão)", description = "Retorna as equipas que receberam mais cartões (amarelos e vermelhos).")
    public ResponseEntity<List<EquipaDtoGet>> getEquipasComMaisCartoes() {
        List<EquipaDtoGet> equipasComMaisCartoes = equipaHandler.getEquipasComMaisCartoes();
        return ResponseEntity.ok(equipasComMaisCartoes);
    }

    @GetMapping("/mais-vitorias")
    @Operation(summary = "Equipas com mais vitórias (tem de ter pelo menos 1 vitoria)", description = "Retorna as equipas com o maior número de vitórias.")
    public ResponseEntity<List<EquipaDtoGet>> getEquipasComMaisVitorias() {
        List<EquipaDtoGet> equipasComMaisVitorias = equipaHandler.getEquipasComMaisVitorias();
        return ResponseEntity.ok(equipasComMaisVitorias);
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Equipas por nome", description = "Retorna as equipas com o nome igual ao fornecido.")
    public ResponseEntity<List<EquipaDtoGet>> getEquipasPorNome(@PathVariable String nome) {
        List<EquipaDtoGet> equipasComMaisVitorias = equipaHandler.getEquipasPorNome(nome);
        return ResponseEntity.ok(equipasComMaisVitorias);
    }

    @GetMapping("quantidade/{num}")
    @Operation(summary = "Equipas por numero jogadores", description = "Retorna as equipas com o numero jogadores igual ao fornecido.")
    public ResponseEntity<List<EquipaDtoGet>> getEquipasPorNumeroDeJogadores(@PathVariable int num) {
        List<EquipaDtoGet> equipasJogadores = equipaHandler.getEquipasPorNumeroDeJogadores(num);
        return ResponseEntity.ok(equipasJogadores);
    }

    @GetMapping("/resultados/vitorias/{num}")
    @Operation(summary = "Equipas pelo número de vitórias", description = "Retorna as equipas com o numero de vitórias igual ao fornecido.")
    public ResponseEntity<List<EquipaDtoGet>> getEquipasPorNumeroDeVitorias(@PathVariable int num) {
        List<EquipaDtoGet> equipasComMaisVitorias = equipaHandler.getEquipasPorNumeroDeVitorias(num);
        return ResponseEntity.ok(equipasComMaisVitorias);
    }

    @GetMapping("/resultados/empates/{num}")
    @Operation(summary = "Equipas pelo número de empates", description = "Retorna as equipas com o numero de empates igual ao fornecido.")
    public ResponseEntity<List<EquipaDtoGet>> getEquipasPorNumeroDeEmpates(@PathVariable int num) {
        List<EquipaDtoGet> equipasComMaisVitorias = equipaHandler.getEquipasPorNumeroDeEmpates(num);
        return ResponseEntity.ok(equipasComMaisVitorias);
    }

    @GetMapping("/resultados/derrotas/{num}")
    @Operation(summary = "Equipas pelo número de derrotas", description = "Retorna as equipas com o numero de derotas igual ao fornecido.")
    public ResponseEntity<List<EquipaDtoGet>> getEquipasPorNumeroDeDerrotas(@PathVariable int num) {
        List<EquipaDtoGet> equipasComMaisVitorias = equipaHandler.getEquipasPorNumeroDeDerrotas(num);
        return ResponseEntity.ok(equipasComMaisVitorias);
    }

    @GetMapping("/conquistas/{num}")
    @Operation(summary = "Equipas pelo número de conquistas", description = "Retorna as equipas com o numero de conquistas igual ao fornecido.")
    public ResponseEntity<List<EquipaDtoGet>> getEquipasPorNumeroDeConquistas(@PathVariable int num) {
        List<EquipaDtoGet> equipasComMaisVitorias = equipaHandler.getEquipasPorNumeroDeConquistas(num);
        return ResponseEntity.ok(equipasComMaisVitorias);
    }

    @GetMapping("/posicao_em_falta/{pos}")
    @Operation(summary = "Equipas pela posição em falta", description = "Retorna as equipas com a posição fornecida em falta.")
    public ResponseEntity<List<EquipaDtoGet>> getEquipasPorPosicaoEmFalta(@PathVariable Posicao pos) {
        List<EquipaDtoGet> equipasComMaisVitorias = equipaHandler.getEquipasPorFaltaDeJogadorNaPosicao(pos);
        return ResponseEntity.ok(equipasComMaisVitorias);
    }
}