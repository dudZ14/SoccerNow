package pt.ul.fc.css.soccernow.dto;

import pt.ul.fc.css.soccernow.enumerados.EstadoJogo;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
    "id",
    "local",
    "jogadoresNoJogo",
    "arbitrosNoJogo",
    "equipaCasaId",
    "equipaVisitanteId",
    "estatistica",
    "estado"
})
public class JogoDtoGet extends JogoDto {

    private EstadoJogo estado;
    private Long id;

    public JogoDtoGet() {
        super();
    }

    public JogoDtoGet(Long id, LocalDto local, Long equipaCasaId, Long equipaVisitanteId,
                      List<JogadorNoJogoDto> jogadoresNoJogo, ArbitrosNoJogoDto arbitrosNoJogo, EstadoJogo estado) {
        super( local, equipaCasaId, equipaVisitanteId, jogadoresNoJogo, arbitrosNoJogo);
        this.id = id;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstadoJogo getEstado() {
        return estado;
    }

    public void setEstado(EstadoJogo estado) {
        this.estado = estado;
    }
}
