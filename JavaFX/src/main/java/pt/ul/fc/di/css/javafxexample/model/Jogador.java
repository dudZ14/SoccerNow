package pt.ul.fc.di.css.javafxexample.model;

import javafx.beans.property.*;
import pt.ul.fc.di.css.javafxexample.dto.JogadorDto.Posicao;

public class Jogador extends Utilizador {

    protected final StringProperty posicaoPreferida = new SimpleStringProperty();
    private final IntegerProperty numeroJogos = new SimpleIntegerProperty();
    private final IntegerProperty numeroGolos = new SimpleIntegerProperty();
    private final IntegerProperty numeroCartoes = new SimpleIntegerProperty();

    public Jogador() {
        super();
    }

    public Jogador(String nome, String email, String password, Posicao posicaoPreferida) {
        setNome(nome);
        setEmail(email);
        setPassword(password);
        setPosicaoPreferida(posicaoPreferida.name());
    }

    // Posicao Preferida
    public StringProperty posicaoPreferidaProperty() {
        return posicaoPreferida;
    }

    public String getPosicaoPreferida() {
        return posicaoPreferida.get();
    }

    public void setPosicaoPreferida(String posicaoPreferida) {
        this.posicaoPreferida.set(posicaoPreferida);
    }

    // Numero Jogos
    public IntegerProperty numeroJogosProperty() {
        return numeroJogos;
    }

    public int getNumeroJogos() {
        return numeroJogos.get();
    }

    public void setNumeroJogos(int numeroJogos) {
        this.numeroJogos.set(numeroJogos);
    }

    // Numero Golos
    public IntegerProperty numeroGolosProperty() {
        return numeroGolos;
    }

    public int getNumeroGolos() {
        return numeroGolos.get();
    }

    public void setNumeroGolos(int numeroGolos) {
        this.numeroGolos.set(numeroGolos);
    }

    // Numero Cartoes
    public IntegerProperty numeroCartoesProperty() {
        return numeroCartoes;
    }

    public int getNumeroCartoes() {
        return numeroCartoes.get();
    }

    public void setNumeroCartoes(int numeroCartoes) {
        this.numeroCartoes.set(numeroCartoes);
    }

    @Override
    public String toString() {
        return String.format(
            "%s (%s) | %d golos em %d jogos",
            getNome(),
            getPosicaoPreferida(),
            getNumeroGolos(),
            getNumeroJogos()
        );
    }
}
