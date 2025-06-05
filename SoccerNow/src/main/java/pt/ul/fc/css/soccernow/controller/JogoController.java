package pt.ul.fc.css.soccernow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import pt.ul.fc.css.soccernow.dto.EstatisticaDto;
import pt.ul.fc.css.soccernow.dto.EstatisticaDtoGet;
import pt.ul.fc.css.soccernow.dto.JogoDto;
import pt.ul.fc.css.soccernow.dto.JogoDtoGet;
import pt.ul.fc.css.soccernow.handlers.JogoHandler;

import java.util.List;

@RestController
@RequestMapping("/api/jogos")
public class JogoController {

    @Autowired
    private JogoHandler jogoHandler;

    @GetMapping
    @Operation(summary = "Obter todos os jogos", description = "Retorna uma lista com todos os jogos.")
    public ResponseEntity<List<JogoDtoGet>> getAllJogos() {
        List<JogoDtoGet> jogos = jogoHandler.getAllJogos();
        return ResponseEntity.ok(jogos);
    }

    @PostMapping
    @Operation(summary = "Criar jogo", description = "Cria um novo jogo com as informações fornecidas.")
    public ResponseEntity<JogoDtoGet> createJogo(@RequestBody JogoDto jogoDto) {
        JogoDtoGet createdJogo = jogoHandler.createJogo(jogoDto);
        return ResponseEntity.ok(createdJogo);
    }

    @PostMapping("/resultado/{jogoId}")
    @Operation(summary = "Registrar resultado", description = "Registra o resultado de um jogo existente.")
    public ResponseEntity<JogoDtoGet> registrarResultado(@PathVariable Long jogoId, @RequestBody EstatisticaDto estatisticaDto) {
        JogoDtoGet updatedJogo = jogoHandler.registrarResultado(jogoId, estatisticaDto);
        if (updatedJogo != null) {
            return ResponseEntity.ok(updatedJogo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/resultado/{jogoId}")
    @Operation(summary = "Obter resultado", description = "Retorna o resultado de um jogo específico.")
    public ResponseEntity<EstatisticaDtoGet> getResultado(@PathVariable Long jogoId) {
        EstatisticaDtoGet estatistica = jogoHandler.getResultado(jogoId);
        if (estatistica != null) {
            return ResponseEntity.ok(estatistica);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter jogo por ID", description = "Retorna as informações de um jogo específico pelo ID.")
    public ResponseEntity<JogoDtoGet> getJogoById(@PathVariable Long id) {
        JogoDtoGet jogo = jogoHandler.getJogoById(id);
        if (jogo != null) {
            return ResponseEntity.ok(jogo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover jogo", description = "Remove um jogo pelo ID fornecido.")
    public ResponseEntity<JogoDtoGet> deleteJogo(@PathVariable Long id) {
        JogoDtoGet deletedJogo = jogoHandler.deleteJogo(id);
        if (deletedJogo != null) {
            return ResponseEntity.ok(deletedJogo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar jogo", description = "Atualiza as informações de um jogo pelo ID fornecido.")
    public ResponseEntity<JogoDtoGet> updateJogo(@PathVariable Long id, @RequestBody JogoDto jogoDto) {
        JogoDtoGet updatedJogo = jogoHandler.updateJogo(id, jogoDto);
        if (updatedJogo != null) {
            return ResponseEntity.ok(updatedJogo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/realizados")
    @Operation(summary = "Obter todos os jogos realizados", description = "Retorna uma lista com todos os jogos realizados.")
    public ResponseEntity<List<JogoDtoGet>> getJogosRealizados() {
        List<JogoDtoGet> jogos = jogoHandler.getJogoRealizados();
        return ResponseEntity.ok(jogos);
    }

    @GetMapping("/agendados")
    @Operation(summary = "Obter todos os jogos agendados", description = "Retorna uma lista com todos os jogos ainda por realizar.")
    public ResponseEntity<List<JogoDtoGet>> getJogosPorRealizar() {
        List<JogoDtoGet> jogos = jogoHandler.getJogoPorRealizar();
        return ResponseEntity.ok(jogos);
    }

    @GetMapping("/turno/manha")
    @Operation(summary = "Obter todos os jogos do turno da manhã", description = "Retorna uma lista com todos os jogos agendados para o turno da manhã.")
    public ResponseEntity<List<JogoDtoGet>> getJogosTurnoManha() {
        List<JogoDtoGet> jogos = jogoHandler.getJogoTurnoDaManha();
        return ResponseEntity.ok(jogos);
    }

    @GetMapping("/turno/tarde")
    @Operation(summary = "Obter todos os jogos do turno da tarde", description = "Retorna uma lista com todos os jogos agendados para o turno da tarde.")
    public ResponseEntity<List<JogoDtoGet>> getJogosTurnoTarde() {
        List<JogoDtoGet> jogos = jogoHandler.getJogoTurnoDaTarde();
        return ResponseEntity.ok(jogos);
    }

    @GetMapping("/turno/noite")
    @Operation(summary = "Obter todos os jogos do turno da noite", description = "Retorna uma lista com todos os jogos agendados para o turno da noite.")
    public ResponseEntity<List<JogoDtoGet>> getJogosTurnoNoite() {
        List<JogoDtoGet> jogos = jogoHandler.getJogoTurnoDaNoite();
        return ResponseEntity.ok(jogos);
    }

    @GetMapping("/local/{nome}")
    @Operation(summary = "Obter todos os jogos por local", description = "Retorna uma lista com todos os jogos realizados ou agendados num determinado local.")
    public ResponseEntity<List<JogoDtoGet>> getJogosPorLocal(@PathVariable String nome) {
        List<JogoDtoGet> jogos = jogoHandler.getJogoPorLocal(nome);
        return ResponseEntity.ok(jogos);
    }

    @GetMapping("/golos/{num}")
    @Operation(summary = "Obter todos os jogos com quantidade específica de golos", description = "Retorna uma lista com todos os jogos que terminaram com o número total de golos igual ao especificado.")
    public ResponseEntity<List<JogoDtoGet>> getJogosPorQuantidadeDeGolos(@PathVariable int num) {
        List<JogoDtoGet> jogos = jogoHandler.getJogoPorQuantidadeDeGolos(num);
        return ResponseEntity.ok(jogos);
    }

}