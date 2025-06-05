package pt.ul.fc.di.css.javafxexample.dto;
import java.time.LocalDateTime;

public class LocalDto {

    private String local;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;

    public LocalDto() {}

    public LocalDto(String local, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
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
}
