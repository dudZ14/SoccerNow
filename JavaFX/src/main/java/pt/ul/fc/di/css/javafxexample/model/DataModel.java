package pt.ul.fc.di.css.javafxexample.model;

import java.util.List;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataModel {

    // Lista de Árbitros
    private final ObservableList<Arbitro> arbitroList = FXCollections.observableArrayList(
        arbitro -> new Observable[] {
            arbitro.nomeProperty(),
            arbitro.emailProperty(),
            arbitro.passwordProperty(),
            arbitro.temCertificadoProperty(),
            arbitro.anosDeExperienciaProperty()
        }
    );

    public ObservableList<Arbitro> getArbitroList() {
        return arbitroList;
    }

    public void setArbitroList(List<Arbitro> arbitros) {
        arbitroList.setAll(arbitros);
    }

    private final ObjectProperty<Arbitro> currentArbitro = new SimpleObjectProperty<>(null);

    public ObjectProperty<Arbitro> currentArbitroProperty() {
        return currentArbitro;
    }

    public Arbitro getCurrentArbitro() {
        return currentArbitro.get();
    }

    public void setCurrentArbitro(Arbitro arbitro) {
        currentArbitro.set(arbitro);
    }

    // Lista de Jogadores
    private final ObservableList<Jogador> jogadorList = FXCollections.observableArrayList(
        jogador -> new Observable[] {
            jogador.nomeProperty(),
            jogador.emailProperty(),
            jogador.passwordProperty(),
            jogador.posicaoPreferidaProperty()
        }
    );

    public ObservableList<Jogador> getJogadorList() {
        return jogadorList;
    }

    public void setJogadorList(List<Jogador> jogadores) {
        jogadorList.setAll(jogadores);
    }

    private final ObjectProperty<Jogador> currentJogador = new SimpleObjectProperty<>(null);

    public ObjectProperty<Jogador> currentJogadorProperty() {
        return currentJogador;
    }

    public Jogador getCurrentJogador() {
        return currentJogador.get();
    }

    public void setCurrentJogador(Jogador jogador) {
        currentJogador.set(jogador);
    }

    // Lista de Equipas
    private final ObservableList<Equipa> equipaList = FXCollections.observableArrayList(
        equipa -> new Observable[] {
            equipa.nomeProperty()
        }
    );

    public ObservableList<Equipa> getEquipaList() {
        return equipaList;
    }

    public void setEquipaList(List<Equipa> equipas) {
        equipaList.setAll(equipas);
    }

    private final ObjectProperty<Equipa> currentEquipa = new SimpleObjectProperty<>(null);

    public ObjectProperty<Equipa> currentEquipaProperty() {
        return currentEquipa;
    }

    public Equipa getCurrentEquipa() {
        return currentEquipa.get();
    }

    public void setCurrentEquipa(Equipa equipa) {
        currentEquipa.set(equipa);
    }

    // Lista de Jogos
    private final ObservableList<Jogo> jogoList = FXCollections.observableArrayList(
        jogo -> new Observable[] {
            jogo.localProperty(),
            jogo.equipaCasaIdProperty(),
            jogo.equipaVisitanteIdProperty(),
            jogo.estadoProperty()
        }
    );

    public ObservableList<Jogo> getJogoList() {
        return jogoList;
    }

    public void setJogoList(List<Jogo> jogos) {
        jogoList.setAll(jogos);
    }

    private final ObjectProperty<Jogo> currentJogo = new SimpleObjectProperty<>(null);

    public ObjectProperty<Jogo> currentJogoProperty() {
        return currentJogo;
    }

    public Jogo getCurrentJogo() {
        return currentJogo.get();
    }

    public void setCurrentJogo(Jogo jogo) {
        currentJogo.set(jogo);
    }

    // Lista de Campeonatos
    private final ObservableList<Campeonato> campeonatoList = FXCollections.observableArrayList(
        campeonato -> new Observable[] {
            campeonato.nomeProperty(),
            campeonato.modalidadeProperty(),
            campeonato.numeroJogosProperty(),
            campeonato.numeroJogosFeitosProperty()
        }
    );

    public ObservableList<Campeonato> getCampeonatoList() {
        return campeonatoList;
    }

    public void setCampeonatoList(List<Campeonato> campeonatos) {
        campeonatoList.setAll(campeonatos);
    }

    private final ObjectProperty<Campeonato> currentCampeonato = new SimpleObjectProperty<>(null);

    public ObjectProperty<Campeonato> currentCampeonatoProperty() {
        return currentCampeonato;
    }

    public Campeonato getCurrentCampeonato() {
        return currentCampeonato.get();
    }

    public void setCurrentCampeonato(Campeonato campeonato) {
        currentCampeonato.set(campeonato);
    }

}
