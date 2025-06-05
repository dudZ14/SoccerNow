package pt.ul.fc.di.css.javafxexample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.api.ApiClient;
import pt.ul.fc.di.css.javafxexample.dto.CampeonatoDto;
import pt.ul.fc.di.css.javafxexample.dto.CampeonatoDtoGet;
import pt.ul.fc.di.css.javafxexample.dto.EquipaDtoGet;
import pt.ul.fc.di.css.javafxexample.dto.JogoDtoGet;
import pt.ul.fc.di.css.javafxexample.model.DataModel;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AddCampeonatoController implements ControllerWithModel {

    private DataModel model;
    private Stage stage;

    @FXML private TextField fieldNome;
    @FXML private TextField fieldModalidade;
    @FXML private ListView<EquipaDtoGet> listEquipas;
    @FXML private ListView<JogoDtoGet> listJogos;
    @FXML private Button btnAdd;

    @FXML
    public void initialize() {
        try {
            List<EquipaDtoGet> equipas = ApiClient.getAllEquipas();
            listEquipas.getItems().setAll(equipas);
            listEquipas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            List<JogoDtoGet> jogos = ApiClient.getAllJogos();
            List<JogoDtoGet> jogosPorJogar = jogos.stream()
                .filter(j -> j.getEstado() == JogoDtoGet.EstadoJogo.POR_JOGAR)
                .collect(Collectors.toList());

            listJogos.getItems().setAll(jogosPorJogar);
            listJogos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            Map<Long, String> equipaIdNomeMap = equipas.stream()
            .collect(Collectors.toMap(EquipaDtoGet::getId, EquipaDtoGet::getNome));
            
            // Definir cell factory para mostrar string personalizada
            listJogos.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(JogoDtoGet jogo, boolean empty) {
                    super.updateItem(jogo, empty);
                    if (empty || jogo == null) {
                        setText(null);
                    } else {
                        String base = jogo.toString();
                        String casa = equipaIdNomeMap.getOrDefault(jogo.getEquipaCasaId(), "Casa Desconhecida");
                        String fora = equipaIdNomeMap.getOrDefault(jogo.getEquipaVisitanteId(), "Fora Desconhecida");
                        setText(base + " - " + casa + " vs " + fora);
                    }
                }
            });

        } catch (Exception e) {
            Util.mostrarAvisoTemporario("❌ Erro ao carregar dados: " + e.getMessage(), stage, "red");
        }
    }

    @FXML
    void addCampeonato() throws IOException, InterruptedException {
        String nome = fieldNome.getText().trim();
        String modalidade = fieldModalidade.getText().trim();
        List<EquipaDtoGet> equipasSelecionadas = listEquipas.getSelectionModel().getSelectedItems();
        List<JogoDtoGet> jogosSelecionados = listJogos.getSelectionModel().getSelectedItems();

        if (nome.isEmpty()) {
            Util.mostrarAvisoTemporario("❌ Nome é obrigatório", stage, "red");
            return;
        }

        List<CampeonatoDtoGet> campeonatos = ApiClient.getAllCampeonatos();
        boolean nomeExiste = campeonatos.stream()
            .anyMatch(e -> e.getNome().equalsIgnoreCase(nome));

        if (nomeExiste) {
            Util.mostrarAvisoTemporario("❌ Já existe uma campeonato com esse nome ", stage, "red");
            return;
        }

        if (modalidade.isEmpty()) {
            Util.mostrarAvisoTemporario("❌ Modalidade é obrigatória", stage, "red");
            return;
        }

        if (equipasSelecionadas.size() < 8) {
            Util.mostrarAvisoTemporario("❌ Um campeonato deve ter pelo menos 8 equipas", stage, "red");
            return;
        }

        int n = equipasSelecionadas.size();
        int jogosNecessarios = (n * (n - 1)) / 2;

        if (jogosSelecionados.size() < jogosNecessarios) {
            Util.mostrarAvisoTemporario(
                String.format("❌ É preciso ter %d jogos, para que todas as equipas joguem entre si", jogosNecessarios),
                stage,
                "red"
            );
            return;
        } 


        List<Long> equipaIds = equipasSelecionadas.stream()
                .map(EquipaDtoGet::getId)
                .collect(Collectors.toList());

        List<Long> jogoIds = jogosSelecionados.stream()
                .map(JogoDtoGet::getId)
                .collect(Collectors.toList());

        CampeonatoDto campeonatoDto = new CampeonatoDto(nome, modalidade, equipaIds, jogoIds);

        try {
            ApiClient.createCampeonato(campeonatoDto);
            Util.mostrarAvisoTemporario("✅ Campeonato criado com sucesso", stage, "green");
            fieldNome.clear();
            fieldModalidade.clear();
            listEquipas.getSelectionModel().clearSelection();
            listJogos.getSelectionModel().clearSelection();
        } catch (Exception e) {
            Util.mostrarAvisoTemporario("❌ Erro: " + e.getMessage(), stage, "red");
        }
    }

    @FXML
    void back(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/campeonatos.fxml", "SoccerNow", model, 600, 400);
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
