package pt.ul.fc.css.soccernow.entities;

import jakarta.persistence.*;

@Entity
public class Conquista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Equipa equipa;

    @ManyToOne
    private Campeonato campeonato;

    // 1 = campeão, 2 = vice, 3 = terceiro lugar
    private int posicao;

    public Conquista() {}

    public Conquista(Equipa equipa, Campeonato campeonato, int posicao) {
        this.equipa = equipa;
        this.campeonato = campeonato;
        this.posicao = posicao;
    }

    // Getters e setters
    public Long getId() {
        return id;
    }

    public Equipa getEquipa() {
        return equipa;
    }

    public void setEquipa(Equipa equipa) {
        this.equipa = equipa;
    }

    public Campeonato getCampeonato() {
        return campeonato;
    }

    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }
}
