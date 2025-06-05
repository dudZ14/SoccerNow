package pt.ul.fc.di.css.javafxexample.dto;
    
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
    public enum EstadoJogo {
        POR_JOGAR,
        ENCERRADO,
        CANCELADO
    }

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

    @Override
    public String toString() {
        String localStr = (getLocal() != null) ? getLocal().getLocal() : "Local desconhecido";

        String dataStr = "Data desconhecida";
        String horaInicioStr = "";
        String horaFimStr = "";

        if (getLocal() != null) {
            if (getLocal().getDataHoraInicio() != null) {
                // Formatar a data (para mostrar só a data)
                dataStr = getLocal().getDataHoraInicio()
                        .toLocalDate()
                        .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                // Formatar hora início
                horaInicioStr = getLocal().getDataHoraInicio()
                            .toLocalTime()
                            .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
            }
            if (getLocal().getDataHoraFim() != null) {
                // Formatar hora fim
                horaFimStr = getLocal().getDataHoraFim()
                            .toLocalTime()
                            .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
            }
        }

        String intervaloHoras;
        if (!horaInicioStr.isEmpty() && !horaFimStr.isEmpty()) {
            intervaloHoras = String.format("%s às %s", horaInicioStr, horaFimStr);
        } else if (!horaInicioStr.isEmpty()) {
            intervaloHoras = horaInicioStr;
        } else {
            intervaloHoras = "Hora desconhecida";
        }

        return String.format("%s - %s (%s)", localStr, dataStr, intervaloHoras);
    }



}
