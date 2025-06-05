package pt.ul.fc.di.css.javafxexample.model;


import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ul.fc.di.css.javafxexample.dto.ArbitrosNoJogoDto;
import pt.ul.fc.di.css.javafxexample.dto.JogoDtoGet.EstadoJogo;
import pt.ul.fc.di.css.javafxexample.dto.JogadorNoJogoDto;
import pt.ul.fc.di.css.javafxexample.dto.LocalDto;

public class Jogo {

    private final LongProperty id = new SimpleLongProperty();
    private final ObjectProperty<LocalDto> local = new SimpleObjectProperty<>();
    private final LongProperty equipaCasaId = new SimpleLongProperty();
    private final LongProperty equipaVisitanteId = new SimpleLongProperty();
    private final ListProperty<JogadorNoJogoDto> jogadoresNoJogo =
        new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<ArbitrosNoJogoDto> arbitrosNoJogo = new SimpleObjectProperty<>();
    private final ObjectProperty<EstadoJogo> estado = new SimpleObjectProperty<>();

    // Constructors
    public Jogo() {}

    public Jogo(Long id, LocalDto local, Long equipaCasaId, Long equipaVisitanteId,
                ObservableList<JogadorNoJogoDto> jogadoresNoJogo, ArbitrosNoJogoDto arbitrosNoJogo,
                EstadoJogo estado) {
        this.id.set(id);
        this.local.set(local);
        this.equipaCasaId.set(equipaCasaId);
        this.equipaVisitanteId.set(equipaVisitanteId);
        this.jogadoresNoJogo.set(jogadoresNoJogo);
        this.arbitrosNoJogo.set(arbitrosNoJogo);
        this.estado.set(estado);
    }

    // ID
    public LongProperty idProperty() {
        return id;
    }

    public long getId() {
        return id.get();
    }

    public void setId(long id) {
        this.id.set(id);
    }

    // Local
    public ObjectProperty<LocalDto> localProperty() {
        return local;
    }

    public LocalDto getLocal() {
        return local.get();
    }

    public void setLocal(LocalDto local) {
        this.local.set(local);
    }

    // Equipa Casa ID
    public LongProperty equipaCasaIdProperty() {
        return equipaCasaId;
    }

    public long getEquipaCasaId() {
        return equipaCasaId.get();
    }

    public void setEquipaCasaId(long id) {
        this.equipaCasaId.set(id);
    }

    // Equipa Visitante ID
    public LongProperty equipaVisitanteIdProperty() {
        return equipaVisitanteId;
    }

    public long getEquipaVisitanteId() {
        return equipaVisitanteId.get();
    }

    public void setEquipaVisitanteId(long id) {
        this.equipaVisitanteId.set(id);
    }

    // Jogadores no Jogo
    public ListProperty<JogadorNoJogoDto> jogadoresNoJogoProperty() {
        return jogadoresNoJogo;
    }

    public ObservableList<JogadorNoJogoDto> getJogadoresNoJogo() {
        return jogadoresNoJogo.get();
    }

    public void setJogadoresNoJogo(ObservableList<JogadorNoJogoDto> jogadores) {
        this.jogadoresNoJogo.set(jogadores);
    }

    // Árbitros no Jogo
    public ObjectProperty<ArbitrosNoJogoDto> arbitrosNoJogoProperty() {
        return arbitrosNoJogo;
    }

    public ArbitrosNoJogoDto getArbitrosNoJogo() {
        return arbitrosNoJogo.get();
    }

    public void setArbitrosNoJogo(ArbitrosNoJogoDto arbitros) {
        this.arbitrosNoJogo.set(arbitros);
    }

    // Estado
    public ObjectProperty<EstadoJogo> estadoProperty() {
        return estado;
    }

    public EstadoJogo getEstado() {
        return estado.get();
    }

    public void setEstado(EstadoJogo estado) {
        this.estado.set(estado);
    }

    public String getData() {
        if (getLocal() != null && getLocal().getDataHoraInicio() != null) {
            return getLocal().getDataHoraInicio()
                    .toLocalDate()
                    .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return "Data desconhecida";
    }

    public String getHoras() {
        if (getLocal() != null) {
            String horaInicio = "";
            String horaFim = "";
            if (getLocal().getDataHoraInicio() != null) {
                horaInicio = getLocal().getDataHoraInicio()
                        .toLocalTime()
                        .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
            }
            if (getLocal().getDataHoraFim() != null) {
                horaFim = getLocal().getDataHoraFim()
                        .toLocalTime()
                        .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
            }
            if (!horaInicio.isEmpty() && !horaFim.isEmpty()) {
                return horaInicio + " às " + horaFim;
            } else if (!horaInicio.isEmpty()) {
                return horaInicio;
            }
        }
        return "Hora desconhecida";
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
