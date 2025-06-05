package pt.ul.fc.di.css.javafxexample.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import pt.ul.fc.di.css.javafxexample.api.ApiClient;
import pt.ul.fc.di.css.javafxexample.dto.CampeonatoDtoGet;
import pt.ul.fc.di.css.javafxexample.model.ConquistaTableEntry;
import pt.ul.fc.di.css.javafxexample.model.DataModel;
import pt.ul.fc.di.css.javafxexample.model.Equipa;


public class ConquistasController {

    @FXML private TableView<ConquistaTableEntry> tableConquistas;
    @FXML private TableColumn<ConquistaTableEntry, String> colNome;
    @FXML private TableColumn<ConquistaTableEntry, String> colPosicao;
    @FXML private TableColumn<ConquistaTableEntry, String> colModalidade;
    @FXML private TableColumn<ConquistaTableEntry, String> colNumEquipas;

    @FXML private Label labelTitulo;


    private Equipa equipaSelecionada;

    private Stage stage;
    private DataModel model;

    public void initModel(Stage stage, DataModel model) {
        this.stage = stage;
        this.model = model;
    }

    public void setEquipa(Equipa equipa) throws IOException, InterruptedException {
        this.equipaSelecionada = equipa;

        labelTitulo.setText("Conquistas do " + equipa.getNome());

        tableConquistas.getItems().clear();

        for (String conquistaStr : equipaSelecionada.getConquistas()) {
            String[] parts = conquistaStr.split(" - ");
            if (parts.length == 2) {
                try {
                    int campeonatoId = Integer.parseInt(parts[0].trim());
                    String posicao = parts[1].trim();

                    CampeonatoDtoGet campeonato = ApiClient.getAllCampeonatos().stream()
                            .filter(c -> c.getId() == campeonatoId)
                            .findFirst()
                            .orElse(null);

                    if (campeonato != null) {
                        String nomeCamp = campeonato.getNome();
                        String modalidade = campeonato.getModalidade();
                        int numEquipas = campeonato.getEquipaIds().size();

                        tableConquistas.getItems().add(new ConquistaTableEntry(nomeCamp, modalidade, posicao,numEquipas));
                    }

                } catch (NumberFormatException e) {
                    System.err.println("Erro ao ler conquista: " + conquistaStr);
                }
            }
        }
    }



    @FXML
    void back(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/list_equipas.fxml", "SoccerNow", model, 700, 370);
    }

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colPosicao.setCellValueFactory(new PropertyValueFactory<>("posicao"));
        colModalidade.setCellValueFactory(new PropertyValueFactory<>("modalidade"));
        colNumEquipas.setCellValueFactory(new PropertyValueFactory<>("numEquipas"));

        tableConquistas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tableConquistas.setFixedCellSize(25);
        tableConquistas.prefHeightProperty().bind(
            javafx.beans.binding.Bindings.size(tableConquistas.getItems()).multiply(25).add(25)
        );
        colNome.prefWidthProperty().bind(tableConquistas.widthProperty().multiply(0.4));
        colModalidade.prefWidthProperty().bind(tableConquistas.widthProperty().multiply(0.3));
        colPosicao.prefWidthProperty().bind(tableConquistas.widthProperty().multiply(0.2));
        colNumEquipas.prefWidthProperty().bind(tableConquistas.widthProperty().multiply(0.2));

    }

}
