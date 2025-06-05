package pt.ul.fc.di.css.javafxexample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.api.ApiClient;
import pt.ul.fc.di.css.javafxexample.dto.EquipaDto;
import pt.ul.fc.di.css.javafxexample.dto.EquipaDtoGet;
import pt.ul.fc.di.css.javafxexample.dto.JogadorDtoGet;
import pt.ul.fc.di.css.javafxexample.model.DataModel;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AddEquipaController implements ControllerWithModel {

    private DataModel model;
    private Stage stage;

    @FXML private TextField fieldNome;
    @FXML private ListView<JogadorDtoGet> listJogadores;
    @FXML private Button btnAdd;

    @FXML
    public void initialize() {
        try {
            List<JogadorDtoGet> jogadores = ApiClient.getAllJogadores();
            listJogadores.getItems().setAll(jogadores);
            listJogadores.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        } catch (Exception e) {
            Util.mostrarAvisoTemporario("❌ Erro ao carregar jogadores: ", stage, "red");
        }
    }

    @FXML
    void addEquipa() throws IOException, InterruptedException {
        String nome = fieldNome.getText().trim();
        List<JogadorDtoGet> selecionados = listJogadores.getSelectionModel().getSelectedItems();

        if (nome.isEmpty()) {
            Util.mostrarAvisoTemporario("❌ Nome da equipa é obrigatório ", stage, "red");
            return;
        }

        if (selecionados.isEmpty()) {
            Util.mostrarAvisoTemporario("❌ Selecione ao menos um jogador ", stage, "red");
            return;
        }

        List<EquipaDtoGet> equipas = ApiClient.getAllEquipas();
        boolean nomeExiste = equipas.stream()
            .anyMatch(e -> e.getNome().equalsIgnoreCase(nome));

        if (nomeExiste) {
            Util.mostrarAvisoTemporario("❌ Já existe uma equipa com esse nome ", stage, "red");
            return;
        }

        List<Long> jogadoresIds = selecionados.stream()
                .map(JogadorDtoGet::getId)
                .collect(Collectors.toList());

        EquipaDto equipaDto = new EquipaDto(nome, jogadoresIds);

        try {
            ApiClient.createEquipa(equipaDto);
            Util.mostrarAvisoTemporario("✅ Equipa criada com sucesso ", stage, "green");
            fieldNome.clear();
            listJogadores.getSelectionModel().clearSelection();
        } catch (Exception e) {
            Util.mostrarAvisoTemporario("❌ Erro: " + e.getMessage(), stage, "red");
        }
    }

    @FXML
    void back(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/equipas.fxml", "SoccerNow", model, 600, 400);
    }

    @Override
    public void initModel(Stage stage, DataModel model) {
        if (this.model != null) throw new IllegalStateException("Model já foi inicializado");
        this.model = model;
        this.stage = stage;
    }

    @Override
    public Stage getStage() { return stage; }

    @Override
    public void setStage(Stage stage) { this.stage = stage; }
}
