package pt.ul.fc.css.soccernow.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import pt.ul.fc.css.soccernow.enumerados.Posicao;

@Entity
@DiscriminatorValue("Jogador")
public class Jogador extends Utilizador {

    @OneToMany(mappedBy = "jogador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartaoJogador> cartoes = new ArrayList<>();

    @OneToMany(mappedBy = "jogador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Golo> golos = new ArrayList<>(); 

    @ManyToMany 
    @JoinTable(
        name = "jogador_equipa",
        joinColumns = @JoinColumn(name = "jogador_id"),
        inverseJoinColumns = @JoinColumn(name = "equipa_id")
    )
    private List<Equipa> equipas = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Posicao posicaoPreferida;

    public Jogador() {}

    public Jogador(String nome, String email, String password, boolean temCertificado, Posicao posicaoPreferida) {
        super(nome, email, password, temCertificado);
        this.posicaoPreferida = posicaoPreferida;
    }

    public List<CartaoJogador> getCartoes() {
        return cartoes;
    }

    public void setCartoes(List<CartaoJogador> cartoes) {
        this.cartoes = cartoes;
    }

    public void addCartao(CartaoJogador cartao) {
        cartoes.add(cartao);
        cartao.setJogador(this);
    }

    public void removeCartao(CartaoJogador cartao) {
        cartoes.remove(cartao);
        cartao.setJogador(null);
    }

    public List<Golo> getGolos() {
        return golos;
    }

    public void setGolos(List<Golo> golos) {
        this.golos = golos;
    }

    public void addGolo(Golo golo) {
        golos.add(golo);
        golo.setJogador(this);
    }

    public void removeGolo(Golo golo) {
        golos.remove(golo);
        golo.setJogador(null);
    }

    public List<Equipa> getEquipas() {
        return equipas;
    }
    
    public void setEquipas(List<Equipa> equipas) {
        this.equipas = equipas;
    }

    public void setEquipa(int posicao, Equipa novaEquipa) {
        for (int i = 0; i < equipas.size(); i++) {
            if (i == posicao) {
                // Substitui a equipa na posição i pela nova equipa
                equipas.set(i, novaEquipa);
            }
        }
    }
    
    
    public void addEquipa(Equipa equipa) {
        if (!this.equipas.contains(equipa)) {
            this.equipas.add(equipa);
        }
        if (equipa.getJogadores() != null && !equipa.getJogadores().contains(this)) {
            equipa.getJogadores().add(this);
        }
    }
    
    public void removeEquipa(Equipa equipa) {
        if (equipas.contains(equipa)) {
            equipas.remove(equipa);
            equipa.getJogadores().remove(this);
        }
    }
    

    public Posicao getPosicaoPreferida() {
        return posicaoPreferida;
    }

    public void setPosicaoPreferida(Posicao posicaoPreferida) {
        this.posicaoPreferida = posicaoPreferida;
    }

    @Override
    public String toString() {
        return "Jogador{" +
                "nome='" + getNome() + '\'' +
                ", cartoes=" + cartoes.size() +
                ", golos=" + golos.size() +
                '}';
    }
}
