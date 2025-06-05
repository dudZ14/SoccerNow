package pt.ul.fc.css.soccernow.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pt.ul.fc.css.soccernow.dto.ArbitroDtoGet;
import pt.ul.fc.css.soccernow.dto.JogadorDtoGet;
import pt.ul.fc.css.soccernow.entities.Arbitro;
import pt.ul.fc.css.soccernow.entities.Equipa;
import pt.ul.fc.css.soccernow.entities.Jogador;
import pt.ul.fc.css.soccernow.entities.Utilizador;
import pt.ul.fc.css.soccernow.handlers.UtilizadorHandler;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/utilizadores")
public class UtilizadorController {

    @Autowired
    private UtilizadorHandler utilizadorHandler;

    @GetMapping
    @Operation(summary = "Obter todos os utilizadores (árbitros e jogadores)", description = "Retorna uma lista com todos os utilizadores, diferenciando entre jogadores e árbitros.")
    public ResponseEntity<List<Object>> getAllUtilizadores() {
        List<Utilizador> utilizadores = utilizadorHandler.getAllUtilizadores();
        List<Object> dtos = utilizadores.stream()
                .map(utilizador -> {
                    if (utilizador instanceof Jogador jogador) {
                        return new JogadorDtoGet(
                            jogador.getId(),
                            jogador.getNome(),
                            jogador.getEmail(),
                            jogador.getPassword(),
                            jogador.getCertificado(),
                            jogador.getEquipas() != null ? 
                                jogador.getEquipas().stream()
                                    .map(Equipa::getNome)
                                    .collect(Collectors.joining(", "))  // Junta os nomes das equipas com vírgula
                                : "Sem equipa",
                            jogador.getGolos() != null ? jogador.getGolos().size() : 0,
                            jogador.getCartoes() != null ? jogador.getCartoes().size() : 0,
                            jogador.getNumJogos(),
                            jogador.getPosicaoPreferida()
                        );

                    } else if (utilizador instanceof Arbitro arbitro) {
                        return new ArbitroDtoGet(
                            arbitro.getId(),
                            arbitro.getNome(),
                            arbitro.getEmail(),
                            arbitro.getPassword(),
                            arbitro.getCertificado(),
                            arbitro.getAnosDeExperiencia(),
                            arbitro.getNumJogos(),
                            arbitro.getNumCartoesMostrados()
                        );
                    }
                    return null;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }


}