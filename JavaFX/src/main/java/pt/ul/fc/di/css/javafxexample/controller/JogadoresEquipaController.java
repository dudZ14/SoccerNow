package pt.ul.fc.di.css.javafxexample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import pt.ul.fc.di.css.javafxexample.model.DataModel;
import pt.ul.fc.di.css.javafxexample.model.Equipa;
import pt.ul.fc.di.css.javafxexample.model.Jogador;

import java.util.List;

public class JogadoresEquipaController {

    @FXML private TableView<Jogador> tableJogadores;
    @FXML private TableColumn<Jogador, String> colNome;
    @FXML private TableColumn<Jogador, String> colEmail;
    @FXML private TableColumn<Jogador, String> colPosicao;
    @FXML private TableColumn<Jogador, Integer> colGolos;
    @FXML private TableColumn<Jogador, Integer> colCartoes;
    @FXML private Label labelTitulo;

    private Stage stage;
    private DataModel model;
    private Equipa equipaSelecionada;

    public void initModel(Stage stage, DataModel model) {
        this.stage = stage;
        this.model = model;
    }

    public void setEquipa(Equipa equipa) {
        this.equipaSelecionada = equipa;
    }

    public void setJogadores(List<Jogador> jogadores) {
        tableJogadores.getItems().setAll(jogadores);
        labelTitulo.setText("Jogadores do " + equipaSelecionada.getNome());
    }

    @FXML
    void back(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/list_equipas.fxml", "SoccerNow", model, 700, 370);
    }

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPosicao.setCellValueFactory(new PropertyValueFactory<>("posicaoPreferida"));
        colGolos.setCellValueFactory(new PropertyValueFactory<>("numeroGolos"));
        colCartoes.setCellValueFactory(new PropertyValueFactory<>("numeroCartoes"));
        tableJogadores.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tableJogadores.setFixedCellSize(25);
        tableJogadores.prefHeightProperty().bind(
            javafx.beans.binding.Bindings.size(tableJogadores.getItems()).multiply(25).add(25) 
    );



    }
}
