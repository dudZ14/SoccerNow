package pt.ul.fc.css.soccernow.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import pt.ul.fc.css.soccernow.enumerados.EstadoJogo;

@Entity
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Local local;

    @OneToOne
    @JoinColumn(name = "equipa_casa_id")
    private Equipa equipaCasa;

    @OneToOne
    @JoinColumn(name = "equipa_fora_id")
    private Equipa equipaFora;

    @OneToMany(mappedBy = "jogo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JogadorNoJogo> jogadoresParticipantes = new ArrayList<>();

    @Version
    private int version; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoJogo estado = EstadoJogo.POR_JOGAR;
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "estatistica_id")
    private Estatistica estatistica;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "arbitros_no_jogo_id")
    private ArbitrosNoJogo arbitrosNoJogo; 

    @ManyToOne
    @JoinColumn(name = "campeonato_id")
    private Campeonato campeonato;
    
    public Jogo() {}

    public Jogo(Local local, Equipa equipeCasa, Equipa equipaFora, List<JogadorNoJogo> jogadoresParticipantes, ArbitrosNoJogo arbitrosNoJogo) {
        this.local = local;
        this.equipaCasa = equipeCasa;
        this.equipaFora = equipaFora;
        this.jogadoresParticipantes = jogadoresParticipantes;
        this.arbitrosNoJogo = arbitrosNoJogo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public Equipa getEquipaCasa() {
        return equipaCasa;
    }

    public void setEquipaCasa(Equipa equipeCasa) {
        this.equipaCasa = equipeCasa;
    }

    public Equipa getEquipaVisitante() {
        return equipaFora;
    }

    public void setEquipaVisitante(Equipa equipaFora) {
        this.equipaFora = equipaFora;
    }

    public EstadoJogo getEstado() {
        return estado;
    }

    public void setEstado(EstadoJogo estado) {
        this.estado = estado;
    }


    public Estatistica getEstatistica() {
        return estatistica;
    }

    public void setEstatistica(Estatistica estatistica) {
        this.estatistica = estatistica;
    }

    public List<JogadorNoJogo> getJogadoresParticipantes() {
        return jogadoresParticipantes;
    }

    public void setJogadoresParticipantes(List<JogadorNoJogo> jogadoresParticipantes) {
        this.jogadoresParticipantes = jogadoresParticipantes;
    }

    public ArbitrosNoJogo getArbitrosNoJogo() {
        return arbitrosNoJogo;
    }

    public void setArbitrosNoJogo(ArbitrosNoJogo arbitrosNoJogo) {
        this.arbitrosNoJogo = arbitrosNoJogo;
    }

    public Campeonato getCampeonato() {
        return campeonato;
    }

    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
    }

    @Override
    public String toString() {
        return "Jogo{" +
                "id=" + id +
                ", local=" + local +
                ", equipaCasa=" + (equipaCasa != null ? equipaCasa.getNome() : "Sem equipa") +
                ", equipaFora=" + (equipaFora != null ? equipaFora.getNome() : "Sem equipa") +
                ", estado=" + estado +
                ", jogadoresParticipantes=" + jogadoresParticipantes.size() +
                '}';
    }
}