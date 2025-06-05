package pt.ul.fc.css.soccernow.entities;

import jakarta.persistence.*;
import pt.ul.fc.css.soccernow.enumerados.Posicao;

@Entity
public class JogadorNoJogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "jogador_id", nullable = false)
    private Jogador jogador;

    @ManyToOne
    @JoinColumn(name = "jogo_id", nullable = false)
    private Jogo jogo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Posicao posicao = Posicao.DEFESA;   //valor default

    @ManyToOne
    @JoinColumn(name = "equipa_id", nullable = false)
    private Equipa equipa; // redundante, mas útil para consulta

    @Version
    private int version; 

    public JogadorNoJogo() {}

    public JogadorNoJogo(Jogador jogador, Jogo jogo, Posicao posicao, Equipa equipa) {
        this.jogador = jogador;
        this.jogo = jogo;
        this.posicao = posicao;
        this.equipa = equipa;
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

    public Jogo getJogo() {
        return jogo;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }

    public Posicao getPosicao() {
        return posicao;
    }

    public void setPosicao(Posicao posicao) {
        this.posicao = posicao;
    }

    public Equipa getEquipa() {
        return equipa;
    }

    public void setEquipa(Equipa equipa) {
        this.equipa = equipa;
    }
}