package pt.ul.fc.css.soccernow.entities;

import jakarta.persistence.*;
import pt.ul.fc.css.soccernow.enumerados.TipoCartao;


@Entity
public class CartaoJogador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "jogador_id", nullable = false)
    private Jogador jogador;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCartao tipoCartao;

    @ManyToOne
    @JoinColumn(name = "estatistica_id")
    private Estatistica estatistica;

    @Version
    private int version; 

    public CartaoJogador() {}

    public CartaoJogador(Jogador jogador, TipoCartao tipoCartao) {
        this.jogador = jogador;
        this.tipoCartao = tipoCartao;
    }

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

    public TipoCartao getTipoCartao() {
        return tipoCartao;
    }

    public void setTipoCartao(TipoCartao tipoCartao) {
        this.tipoCartao = tipoCartao;
    }

    public void setEstatistica(Estatistica estatistica) {
        this.estatistica = estatistica;
    }

    public Estatistica getEstatistica() {
        return estatistica;
    }
}