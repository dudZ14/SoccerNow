package pt.ul.fc.css.soccernow.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Estatistica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "estatistica", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Golo> golos = new ArrayList<>();

    @OneToMany(mappedBy = "estatistica", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartaoJogador> cartoes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "equipa_vencedora_id")
    private Equipa equipaVencedora; // Alterado de String para Equipa

    @Column
    private String resultado; // Ex: "2-1"


    @Version
    private int version; 

    public Estatistica() {}

    public Estatistica(List<Golo> golos, List<CartaoJogador> cartoes, Equipa equipaVencedora) {
        this.golos = golos != null ? golos : new ArrayList<>();
        this.cartoes = cartoes != null ? cartoes : new ArrayList<>();
        this.equipaVencedora = equipaVencedora;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Golo> getGolos() {
        return golos;
    }

    public void setGolos(List<Golo> golos) {
        this.golos = golos;
    }

    public List<CartaoJogador> getCartoes() {
        return cartoes;
    }

    public void setCartoes(List<CartaoJogador> cartoes) {
        this.cartoes = cartoes;
    }

    public Equipa getEquipaVencedora() {
        return equipaVencedora;
    }

    public void setEquipaVencedora(Equipa equipaVencedora) {
        this.equipaVencedora = equipaVencedora;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }


    
}