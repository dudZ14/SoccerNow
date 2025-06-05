package pt.ul.fc.di.css.javafxexample.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class Equipa {

    protected final LongProperty id = new SimpleLongProperty();
    private final StringProperty nome = new SimpleStringProperty();

    private final ObservableList<Jogador> jogadores = FXCollections.observableArrayList();

    private final ObservableList<Long> historicoJogosIds = FXCollections.observableArrayList();

    private final ObservableList<String> conquistas = FXCollections.observableArrayList();

    private final IntegerProperty numeroVitorias = new SimpleIntegerProperty();


    public Equipa() {
    }

    public Equipa(String nome, List<Jogador> jogadores, List<Long> historicoJogosIds, List<String> conquistas) {
        setNome(nome);
        setJogadores(jogadores);
        setHistoricoJogosIds(historicoJogosIds);
        setConquistas(conquistas);
    }

    public LongProperty idProperty() {
        return id;
    }

    public long getId() {
        return id.get();
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public int getNumeroVitorias() {
        return numeroVitorias.get();
    }

    public void setNumeroVitorias(int vitorias) {
        this.numeroVitorias.set(vitorias);
    }

    public IntegerProperty numeroVitoriasProperty() {
        return numeroVitorias;
    }


    public StringProperty nomeProperty() {
        return nome;
    }

    public String getNome() {
        return nome.get();
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public ObservableList<Jogador> getJogadores() {
        return jogadores;
    }

    public void setJogadores(List<Jogador> jogadores) {
        this.jogadores.setAll(jogadores);
    }

    public ObservableList<Long> getHistoricoJogosIds() {
        return historicoJogosIds;
    }

    public void setHistoricoJogosIds(List<Long> historicoJogosIds) {
        this.historicoJogosIds.setAll(historicoJogosIds);
    }

    public ObservableList<String> getConquistas() {
        return conquistas;
    }

    public void setConquistas(List<String> conquistas) {
        this.conquistas.setAll(conquistas);
    }

    public int getNumeroJogadores() {
        return jogadores.size();
    }

    public int getNumeroJogos() {
        return historicoJogosIds.size();
    }

    public int getNumeroConquistas() {
        return conquistas.size();
    }

    @Override
    public String toString() {
        return String.format("%s | %d jogadores | %d vitórias", getNome(), getNumeroJogadores(), getNumeroVitorias());
    }
}
