package pt.ul.fc.di.css.javafxexample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.dto.ArbitroDtoGet;
import pt.ul.fc.di.css.javafxexample.dto.ArbitrosNoJogoDto;
import pt.ul.fc.di.css.javafxexample.model.ArbitroEntry;
import pt.ul.fc.di.css.javafxexample.model.DataModel;
import pt.ul.fc.di.css.javafxexample.api.ApiClient;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ArbitroJogoController {

    @FXML private TableView<ArbitroEntry> tableArbitros;
    @FXML private TableColumn<ArbitroEntry, String> colPrincipal;
    @FXML private TableColumn<ArbitroEntry, String> colAssistente;
    @FXML private Label labelTitulo;

    private Stage stage;
    private DataModel model;

    public void initModel(Stage stage, DataModel model) {
        this.stage = stage;
        this.model = model;
    }

    public void setArbitros(ArbitrosNoJogoDto arbitrosDto) throws IOException, InterruptedException {
        List<ArbitroEntry> rows = new ArrayList<>();

        if (arbitrosDto == null) return;

        List<ArbitroDtoGet> todosArbitros = ApiClient.getAllArbitros();

        Map<Long, String> mapaIdParaNome = todosArbitros.stream()
            .collect(Collectors.toMap(
                ArbitroDtoGet::getId,
                a -> a.getNome() + (a.getTemCertificado() ? " (certificado)" : " (não certificado)")
            ));


        String nomePrincipal = mapaIdParaNome.getOrDefault(arbitrosDto.getArbitroPrincipalId(), "Desconhecido");

        List<String> nomesAssistentes = arbitrosDto.getArbitrosIds().stream()
                .filter(id -> !id.equals(arbitrosDto.getArbitroPrincipalId()))
                .map(id -> mapaIdParaNome.getOrDefault(id, "Desconhecido"))
                .collect(Collectors.toList());

        int max = Math.max(1, nomesAssistentes.size());
        for (int i = 0; i < max; i++) {
            String principal = (i == 0) ? nomePrincipal : "";
            String assistente = (i < nomesAssistentes.size()) ? nomesAssistentes.get(i) : "";
            rows.add(new ArbitroEntry(principal, assistente));
        }

        tableArbitros.getItems().setAll(rows);
        labelTitulo.setText("Árbitros do Jogo");
    }

    @FXML
    void back(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/list_jogos.fxml", "SoccerNow", model, 600, 400);
    }

    @FXML
    public void initialize() {
        colPrincipal.setCellValueFactory(new PropertyValueFactory<>("arbitroPrincipal"));
        colAssistente.setCellValueFactory(new PropertyValueFactory<>("arbitroAssistente"));
        tableArbitros.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tableArbitros.setFixedCellSize(25);
        tableArbitros.prefHeightProperty().bind(
                javafx.beans.binding.Bindings.size(tableArbitros.getItems()).multiply(25).add(25)
        );
    }
}
