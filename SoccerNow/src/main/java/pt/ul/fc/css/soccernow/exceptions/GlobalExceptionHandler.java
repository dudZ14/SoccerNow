package pt.ul.fc.css.soccernow.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NomeVazioException.class)
    public ResponseEntity<String> handleNomeVazio(NomeVazioException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(EmailInvalidoException.class)
    public ResponseEntity<String> handleEmailInvalido(EmailInvalidoException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(EquipaComJogoMarcadoException.class)
    public ResponseEntity<String> handleEquipaComJogoMarcado(EquipaComJogoMarcadoException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    
    @ExceptionHandler(JogadorComJogoMarcadoException.class)
    public ResponseEntity<String> handleJogadorComJogoMarcado(JogadorComJogoMarcadoException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    
    @ExceptionHandler(ArbitroComJogoMarcadoException.class)
    public ResponseEntity<String> handleArbitroComJogoMarcado(ArbitroComJogoMarcadoException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    
    @ExceptionHandler(DataHoraInvalidaException.class)
    public ResponseEntity<String> handleDataHoraInvalida(DataHoraInvalidaException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(JogoConflitanteException.class)
    public ResponseEntity<String> handleJogoConflitante(JogoConflitanteException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(EquipaConflitanteException.class)
    public ResponseEntity<String> handleEquipaConflitante(EquipaConflitanteException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(ArbitroConflitanteException.class)
    public ResponseEntity<String> handleArbitroConflitante(ArbitroConflitanteException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(EquipasIguaisException.class)
    public ResponseEntity<String> handleEquipasIguais(EquipasIguaisException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }


    @ExceptionHandler(PosicaoInvalidaException.class)
    public ResponseEntity<String> handlePosicaoInvalida(PosicaoInvalidaException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }


    @ExceptionHandler(JogadorInvalidoException.class)
    public ResponseEntity<String> handleJogadorInvalido(JogadorInvalidoException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(NumeroJogadoresInvalidoException.class)
    public ResponseEntity<String> handleNumeroJogadoresInvalido(NumeroJogadoresInvalidoException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(NumeroGuardaRedesInvalidoException.class)
    public ResponseEntity<String> handleNumeroGuardaRedesInvalido(NumeroGuardaRedesInvalidoException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(JogadorEmAmbasEquipasException.class)
    public ResponseEntity<String> handleJogadorEmAmbasEquipas(JogadorEmAmbasEquipasException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(CampeonatoInvalidoException.class)
    public ResponseEntity<String> handleCampeonatoInvalido(CampeonatoInvalidoException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    
    @ExceptionHandler(ImpossivelRegistarException.class)
    public ResponseEntity<String> handleImpossivelRegistar(ImpossivelRegistarException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }

}
