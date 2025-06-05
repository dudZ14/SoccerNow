package pt.ul.fc.css.soccernow.entities;

import jakarta.persistence.Embeddable;
import pt.ul.fc.css.soccernow.exceptions.DataHoraInvalidaException;

import java.time.LocalDateTime;

@Embeddable
public class Local {

    private String local;

    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;

    public Local() {}

    public Local(String local, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        if (dataHoraInicio == null || dataHoraFim == null) {
            throw new DataHoraInvalidaException("As datas de início e fim não podem ser nulas.");
        }

        if (!dataHoraInicio.isBefore(dataHoraFim)) {
            throw new DataHoraInvalidaException("A data/hora de início deve ser estritamente anterior à data/hora de fim.");
        }
        
        this.local = local;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    /**
     * Verifica se dois locais se intersectam no tempo e espaço.
     */
    public boolean intersetaCom(Local outro) {
        if (!this.local.equals(outro.local)) {
            return false;
        }

        return this.dataHoraInicio.isBefore(outro.dataHoraFim)
                && outro.dataHoraInicio.isBefore(this.dataHoraFim);
    }

    /**
     * Verifica se dois locais se intersectam no tempo.
     */
    public boolean horariosIntersetam(Local outro) {
        return this.dataHoraInicio.isBefore(outro.dataHoraFim)
                && outro.dataHoraInicio.isBefore(this.dataHoraFim);
    }
    
}
