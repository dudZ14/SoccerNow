package pt.ul.fc.css.soccernow.entities;

import jakarta.persistence.*;

@Entity
public class Golo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "jogador_id")
    private Jogador jogador;

    @ManyToOne
    @JoinColumn(name = "equipa_id")
    private Equipa equipa; // Alterado de String para Equipa

    @Column(nullable = false)
    private int minuto;

    @ManyToOne
    @JoinColumn(name = "estatistica_id")
    private Estatistica estatistica;

    @Version
    private int version; 

    public Golo() {}

    public Golo(Jogador jogador, Equipa equipa, int minuto) {
        this.jogador = jogador;
        this.equipa = equipa;
        this.minuto = minuto;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }

    public Equipa getEquipa() {
        return equipa;
    }

    public void setEquipa(Equipa equipa) {
        this.equipa = equipa;
    }

    public int getMinuto() {
        return minuto;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }

    public Estatistica getEstatistica() {
        return estatistica;
    }

    public void setEstatistica(Estatistica estatistica) {
        this.estatistica = estatistica;
    }

    @Override
    public String toString() {
        return "Golo{" +
                "id=" + id +
                ", jogador=" + (jogador != null ? jogador.getNome() : "null") +
                ", equipa=" + (equipa != null ? equipa.getNome() : "null") +
                ", minuto=" + minuto +
                '}';
    }
}