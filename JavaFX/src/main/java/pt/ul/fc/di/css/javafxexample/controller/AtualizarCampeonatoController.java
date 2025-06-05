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
import pt.ul.fc.di.css.javafxexample.model.Campeonato;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AtualizarCampeonatoController implements ControllerWithModel {

    private DataModel model;
    private Stage stage;
    private Campeonato campeonatoAtual;

    @FXML private TextField fieldNome;
    @FXML private TextField fieldModalidade;
    @FXML private ListView<EquipaDtoGet> listEquipas;
    @FXML private ListView<JogoDtoGet> listJogos;
    @FXML private Button btnAtualizar;

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

        } catch (Exception e) {
            Util.mostrarAvisoTemporario("❌ Erro ao carregar dados", stage, "red");
        }
    }

    @FXML
    void atualizarCampeonato() throws IOException, InterruptedException {
        String nome = fieldNome.getText().trim();
        String modalidade = fieldModalidade.getText().trim();

        List<EquipaDtoGet> equipasSelecionadas = listEquipas.getSelectionModel().getSelectedItems();
        List<JogoDtoGet> jogosSelecionados = listJogos.getSelectionModel().getSelectedItems();

        if (nome.isEmpty()) {
            Util.mostrarAvisoTemporario("❌ Nome do campeonato é obrigatório", stage, "red");
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

        // Validar nome único (exceto o atual campeonato)
        List<CampeonatoDtoGet> campeonatos = ApiClient.getAllCampeonatos();
        boolean nomeExiste = campeonatos.stream()
            .filter(c -> !c.getId().equals(campeonatoAtual.getId()))
            .anyMatch(c -> c.getNome().equalsIgnoreCase(nome));

        if (nomeExiste) {
            Util.mostrarAvisoTemporario("❌ Já existe um campeonato com esse nome", stage, "red");
            return;
        }

        List<Long> equipasIds = equipasSelecionadas.stream()
                .map(EquipaDtoGet::getId)
                .collect(Collectors.toList());

        List<Long> jogosIds = jogosSelecionados.stream()
                .map(JogoDtoGet::getId)
                .collect(Collectors.toList());

        CampeonatoDto campeonatoDto = new CampeonatoDto(nome, modalidade, equipasIds, jogosIds);

        try {
            ApiClient.updateCampeonato(campeonatoAtual.getId(), campeonatoDto);
            Util.mostrarAvisoTemporario("✅ Campeonato atualizado com sucesso", stage, "green");
        } catch (Exception e) {
            Util.mostrarAvisoTemporario("❌ " + e.getMessage(), stage, "red");
        }
    }

    public void initModel(Stage stage, DataModel model, Campeonato selected) {
        this.stage = stage;
        this.model = model;
        this.campeonatoAtual = selected;

        fieldNome.setText(selected.getNome());
        fieldModalidade.setText(selected.getModalidade());

       
        List<EquipaDtoGet> itemsEquipas = listEquipas.getItems();
        List<Long> idsEquipasAtuais = selected.getEquipaIds();
        for (int i = 0; i < itemsEquipas.size(); i++) {
            if (idsEquipasAtuais.contains(itemsEquipas.get(i).getId())) {
                listEquipas.getSelectionModel().select(i);
            }
        }

        List<JogoDtoGet> itemsJogos = listJogos.getItems();
        List<Long> idsJogosAtuais = selected.getJogoIds();
        for (int i = 0; i < itemsJogos.size(); i++) {
            if (idsJogosAtuais.contains(itemsJogos.get(i).getId())) {
                listJogos.getSelectionModel().select(i);
            }
        }
    }

    @FXML
    void back(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/list_campeonatos.fxml", "SoccerNow", model, 600, 370);
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
