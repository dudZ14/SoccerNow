package pt.ul.fc.css.soccernow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import pt.ul.fc.css.soccernow.dto.CampeonatoDto;
import pt.ul.fc.css.soccernow.dto.CampeonatoDtoGet;
import pt.ul.fc.css.soccernow.handlers.CampeonatoHandler;

import java.util.List;

@RestController
@RequestMapping("/api/campeonatos")
public class CampeonatoController {

    @Autowired
    private CampeonatoHandler campeonatoHandler;

    @GetMapping
    @Operation(summary = "Obter todos os campeonatos", description = "Retorna uma lista com todos os campeonatos.")
    public ResponseEntity<List<CampeonatoDtoGet>> getAllCampeonatos() {
        List<CampeonatoDtoGet> campeonatos = campeonatoHandler.getAllCampeonatos();
        return ResponseEntity.ok(campeonatos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter campeonato por ID", description = "Retorna as informações de um campeonato específico pelo ID.")
    public ResponseEntity<CampeonatoDtoGet> getCampeonatoById(@PathVariable Long id) {
        CampeonatoDtoGet campeonato = campeonatoHandler.getCampeonatoById(id);
        if (campeonato != null) {
            return ResponseEntity.ok(campeonato);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Criar campeonato", description = "Cria um novo campeonato com as informações fornecidas.")
    public ResponseEntity<CampeonatoDtoGet> createCampeonato(@RequestBody CampeonatoDto campeonatoDto) {
        CampeonatoDtoGet createdCampeonato = campeonatoHandler.createCampeonato(campeonatoDto);
        return ResponseEntity.ok(createdCampeonato);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar campeonato", description = "Atualiza as informações de um campeonato pelo ID fornecido.")
    public ResponseEntity<CampeonatoDtoGet> updateCampeonato(@PathVariable Long id, @RequestBody CampeonatoDto campeonatoDto) {
        CampeonatoDtoGet updatedCampeonato = campeonatoHandler.updateCampeonato(id, campeonatoDto);
        if (updatedCampeonato != null) {
            return ResponseEntity.ok(updatedCampeonato);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover campeonato", description = "Remove um campeonato pelo ID fornecido.")
    public ResponseEntity<CampeonatoDtoGet> deleteCampeonato(@PathVariable Long id) {
        CampeonatoDtoGet deletedCampeonato = campeonatoHandler.deleteCampeonato(id);
        if (deletedCampeonato != null) {
            return ResponseEntity.ok(deletedCampeonato);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{campeonatoId}/cancelar-jogo/{jogoId}")
    @Operation(summary = "Cancelar jogo de campeonato", description = "Cancela um jogo específico de um campeonato.")
    public ResponseEntity<Void> cancelarJogo(@PathVariable Long campeonatoId, @PathVariable Long jogoId) {
        boolean cancelled = campeonatoHandler.cancelarJogo(campeonatoId, jogoId);
        if (cancelled) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/filtro/nome")
    @Operation(summary = "Filtrar campeonatos por nome", description = "Filtra os campeonatos cujo nome contenha o texto informado.")
    public ResponseEntity<List<CampeonatoDtoGet>> filtrarPorNome(@RequestParam String nome) {
        List<CampeonatoDtoGet> resultados = campeonatoHandler.filtrarPorNome(nome);
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/filtro/equipa/{equipaId}")
    @Operation(summary = "Filtrar campeonatos por equipa", description = "Filtra os campeonatos que contenham a equipa especificada.")
    public ResponseEntity<List<CampeonatoDtoGet>> filtrarPorEquipa(@PathVariable Long equipaId) {
        List<CampeonatoDtoGet> resultados = campeonatoHandler.filtrarPorEquipa(equipaId);
        if (resultados == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultados);
    }


    @GetMapping("/filtro/jogos-realizados")
    @Operation(summary = "Filtrar campeonatos por número de jogos realizados", description = "Filtra campeonatos que tenham o número indicado de jogos realizados.")
    public ResponseEntity<List<CampeonatoDtoGet>> filtrarPorJogosRealizados(@RequestParam long minJogos) {
        List<CampeonatoDtoGet> resultados = campeonatoHandler.filtrarPorMinJogosRealizados(minJogos);
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/filtro/jogos-por-realizar")
    @Operation(summary = "Filtrar campeonatos por número de jogos a realizar", description = "Filtra campeonatos que tenham o número indicado de jogos por realizar.")
    public ResponseEntity<List<CampeonatoDtoGet>> filtrarPorJogosPorRealizar(@RequestParam long minJogos) {
        List<CampeonatoDtoGet> resultados = campeonatoHandler.filtrarPorMinJogosPorRealizar(minJogos);
        return ResponseEntity.ok(resultados);
    }
    
}
