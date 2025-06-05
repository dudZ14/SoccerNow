package pt.ul.fc.di.css.javafxexample.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.List;
import java.util.Map;

public class Campeonato {

    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty nome = new SimpleStringProperty();
    private final StringProperty modalidade = new SimpleStringProperty();

    private final ObservableList<Long> equipaIds = FXCollections.observableArrayList();
    private final ObservableList<Long> jogoIds = FXCollections.observableArrayList();

    private final IntegerProperty numeroJogos = new SimpleIntegerProperty();
    private final IntegerProperty numeroJogosFeitos = new SimpleIntegerProperty();

    private final ObservableMap<String, Integer> pontuacoes = FXCollections.observableHashMap();

    public Campeonato() {
    }

    public Campeonato(String nome, String modalidade, List<Long> equipaIds, List<Long> jogoIds,
                      int numeroJogos, int numeroJogosFeitos, Map<String, Integer> pontuacoes) {
        setNome(nome);
        setModalidade(modalidade);
        setEquipaIds(equipaIds);
        setJogoIds(jogoIds);
        setNumeroJogos(numeroJogos);
        setNumeroJogosFeitos(numeroJogosFeitos);
        setPontuacoes(pontuacoes);
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

    public StringProperty nomeProperty() {
        return nome;
    }

    public String getNome() {
        return nome.get();
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public StringProperty modalidadeProperty() {
        return modalidade;
    }

    public String getModalidade() {
        return modalidade.get();
    }

    public void setModalidade(String modalidade) {
        this.modalidade.set(modalidade);
    }

    public ObservableList<Long> getEquipaIds() {
        return equipaIds;
    }

    public void setEquipaIds(List<Long> equipaIds) {
        this.equipaIds.setAll(equipaIds);
    }

    public ObservableList<Long> getJogoIds() {
        return jogoIds;
    }

    public void setJogoIds(List<Long> jogoIds) {
        this.jogoIds.setAll(jogoIds);
    }

    public IntegerProperty numeroJogosProperty() {
        return numeroJogos;
    }

    public int getNumeroJogos() {
        return numeroJogos.get();
    }

    public void setNumeroJogos(int numeroJogos) {
        this.numeroJogos.set(numeroJogos);
    }

    public IntegerProperty numeroJogosFeitosProperty() {
        return numeroJogosFeitos;
    }

    public int getNumeroJogosFeitos() {
        return numeroJogosFeitos.get();
    }

    public void setNumeroJogosFeitos(int numeroJogosFeitos) {
        this.numeroJogosFeitos.set(numeroJogosFeitos);
    }

    public ObservableMap<String, Integer> getPontuacoes() {
        return pontuacoes;
    }

    public void setPontuacoes(Map<String, Integer> pontuacoes) {
        this.pontuacoes.clear();
        if (pontuacoes != null) {
            this.pontuacoes.putAll(pontuacoes);
        }
    }

    @Override
    public String toString() {
        return String.format("%s (%s) | %d jogos | %d realizados", getNome(), getModalidade(), getNumeroJogos(), getNumeroJogosFeitos());
    }
}
