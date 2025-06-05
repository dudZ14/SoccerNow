package pt.ul.fc.css.soccernow.dto;

import pt.ul.fc.css.soccernow.enumerados.EstadoJogo;
import java.time.LocalDateTime;
import java.util.List;

public class JogoVista {
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String equipaCasa;
    private String equipaVisitante;
    private String arbitroPrincipal;
    private String arbitrosAssistentes;
    private EstadoJogo estado;
    private List<String> jogadores;
    private String local; 

    public JogoVista(LocalDateTime dataInicio, LocalDateTime dataFim, String equipaCasa, String equipaVisitante,
                     String arbitroPrincipal, String arbitrosAssistentes, List<String> jogadores, EstadoJogo estado, String local) {
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.equipaCasa = equipaCasa;
        this.equipaVisitante = equipaVisitante;
        this.arbitroPrincipal = arbitroPrincipal;
        this.arbitrosAssistentes = arbitrosAssistentes;
        this.jogadores = jogadores;
        this.estado = estado;
        this.local = local;
    }

    // Getters (para Thymeleaf)
    public LocalDateTime getDataInicio() { return dataInicio; }
    public LocalDateTime getDataFim() { return dataFim; }
    public String getEquipaCasa() { return equipaCasa; }
    public String getEquipaVisitante() { return equipaVisitante; }
    public String getArbitroPrincipal() { return arbitroPrincipal; }
    public String getArbitrosAssistentes() { return arbitrosAssistentes; }
    public String getLocal() {
        return local;
    }
    public List<String> getJogadores() {
        return jogadores;
    }

    public void setJogadores(List<String> jogadores) {
        this.jogadores = jogadores;
    }
    public EstadoJogo getEstado() { return estado; }
}
