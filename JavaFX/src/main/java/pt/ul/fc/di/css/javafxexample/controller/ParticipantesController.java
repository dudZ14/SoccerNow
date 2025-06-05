package pt.ul.fc.di.css.javafxexample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import pt.ul.fc.di.css.javafxexample.dto.JogadorNoJogoDto;
import pt.ul.fc.di.css.javafxexample.model.DataModel;

import java.util.List;

public class ParticipantesController {

    @FXML private TableView<JogadorNoJogoDto> tableParticipantes;
    @FXML private TableColumn<JogadorNoJogoDto, String> colNome;
    @FXML private TableColumn<JogadorNoJogoDto, String> colPosicao;
    @FXML private TableColumn<JogadorNoJogoDto, String> colEquipa;
    @FXML private Label labelTitulo;

    private Stage stage;
    private DataModel model;

    public void initModel(Stage stage, DataModel model) {
        this.stage = stage;
        this.model = model;
    }

    public void setJogadores(List<JogadorNoJogoDto> jogadores) {
        tableParticipantes.getItems().setAll(jogadores);
        labelTitulo.setText("Jogadores do Jogo");
    }

    @FXML
    void back(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/list_jogos.fxml", "SoccerNow", model, 600, 400);
    }

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nomeJogador"));
        colEquipa.setCellValueFactory(new PropertyValueFactory<>("nomeEquipa"));
        colPosicao.setCellValueFactory(new PropertyValueFactory<>("posicao"));
        tableParticipantes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tableParticipantes.setFixedCellSize(25);
        tableParticipantes.prefHeightProperty().bind(
            javafx.beans.binding.Bindings.size(tableParticipantes.getItems()).multiply(25).add(25)
        );
    }
}
