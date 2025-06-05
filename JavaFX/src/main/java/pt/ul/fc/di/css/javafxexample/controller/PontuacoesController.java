package pt.ul.fc.di.css.javafxexample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.dto.CampeonatoDtoGet;
import pt.ul.fc.di.css.javafxexample.model.DataModel;

import java.util.Map;

public class PontuacoesController {

    private Stage stage;
    private DataModel model;
    private CampeonatoDtoGet campeonato;

    @FXML
    private TableView<Map.Entry<String, Integer>> tablePontuacoes;

    @FXML
    private TableColumn<Map.Entry<String, Integer>, String> colEquipe;

    @FXML
    private TableColumn<Map.Entry<String, Integer>, Integer> colPontos;

    @FXML
    private Label labelCampeonato; // Referência ao label para o nome do campeonato

    public void initData(Stage stage, DataModel model, CampeonatoDtoGet campeonato) {
        this.stage = stage;
        this.model = model;
        this.campeonato = campeonato;
        carregarPontuacoes();
    }

    private void carregarPontuacoes() {
        if (campeonato.getPontuacoes() == null || campeonato.getPontuacoes().isEmpty()) {
            Util.mostrarAvisoTemporario("Este campeonato não tem pontuações.", stage, "orange");
            tablePontuacoes.getItems().clear();
            return;
        }

        // Define os cellValueFactory APENAS
        colEquipe.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getKey()));
        colPontos.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getValue()).asObject());

        ObservableList<Map.Entry<String, Integer>> items =
                FXCollections.observableArrayList(campeonato.getPontuacoes().entrySet());

        tablePontuacoes.setItems(items);
        tablePontuacoes.setEditable(false);
        // Garante que não tenha uma coluna extra vazia
        tablePontuacoes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tablePontuacoes.setFixedCellSize(25);
        tablePontuacoes.prefHeightProperty().bind(
            javafx.beans.binding.Bindings.size(tablePontuacoes.getItems()).multiply(25).add(26)
        );

        // Definindo o nome do campeonato no Label
        labelCampeonato.setText(campeonato.getNome());
    }

    @FXML
    void back(ActionEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/list_campeonatos.fxml", "SoccerNow", model, 600, 370);
    }
}
