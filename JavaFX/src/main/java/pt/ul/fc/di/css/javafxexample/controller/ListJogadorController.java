package pt.ul.fc.di.css.javafxexample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.api.ApiClient;
import pt.ul.fc.di.css.javafxexample.dto.JogadorDto.Posicao;
import pt.ul.fc.di.css.javafxexample.dto.JogadorDtoGet;
import pt.ul.fc.di.css.javafxexample.model.DataModel;
import pt.ul.fc.di.css.javafxexample.model.Jogador;

import java.util.List;

public class ListJogadorController implements ControllerWithModel {

    private Stage stage;
    private DataModel model;

    @FXML private Label labelNome;
    @FXML private Label labelEmail;
    @FXML private Label labelPosicao;
    @FXML private Label labelNumJogos;
    @FXML private Label labelNumGolos;
    @FXML private Label labelNumCartoes;

    @FXML private HBox boxNome;
    @FXML private HBox boxEmail;
    @FXML private HBox boxPosicao;
    @FXML private HBox boxNumJogos;
    @FXML private HBox boxNumGolos;
    @FXML private HBox boxNumCartoes;

    @FXML private Button btnRemove;

    @FXML private ListView<Jogador> listJogadores;

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void back(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/jogadores.fxml", "SoccerNow", model, 600, 400);
    }

    @FXML
    void removerJogador() {
        Jogador selected = listJogadores.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Util.mostrarAvisoTemporario("Selecione um jogador primeiro", stage, "orange");
            return;
        }

        try {
            ApiClient.deleteJogador(selected.getId());
            model.getJogadorList().remove(selected);
            listJogadores.getSelectionModel().clearSelection();
            Util.mostrarAvisoTemporario("✅ Jogador removido com sucesso",stage,"green");
            hideDetails();
        } catch (Exception e) {
            Util.mostrarAvisoTemporario("❌ " + e.getMessage(),stage,"red");
        }
    }

    @FXML
    void editarJogador() {
        Jogador selected = listJogadores.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Util.mostrarAvisoTemporario("Selecione um jogador primeiro", stage, "orange");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/ul/fc/di/css/javafxexample/view/atualizar_jogador.fxml"));
            Parent root = loader.load();

            AtualizarJogadorController controller = loader.getController();
            controller.initModel(stage, model, selected);

            Scene scene = new Scene(root);
            // aplicar o css
            scene.getStylesheets().add(getClass().getResource("/pt/ul/fc/di/css/javafxexample/view/init.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Editar Jogador");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initModel(Stage stage, DataModel model) {
        this.stage = stage;
        this.model = model;

        hideDetails();

        setClickListener();

        setJogadorList(model);
    }

    private void setJogadorList(DataModel model) {
        try {
            List<JogadorDtoGet> jogadores = ApiClient.getAllJogadores();

            List<Jogador> jogadorList = jogadores.stream()
                    .map(dto -> {
                        Posicao posicao = Posicao.valueOf(dto.getPosicaoPreferida());
                        Jogador jogador = new Jogador(dto.getNome(), dto.getEmail(), dto.getPassword(), posicao);
                        jogador.setId(dto.getId());
                        jogador.setNumeroJogos(dto.getNumeroJogos());
                        jogador.setNumeroGolos(dto.getNumeroGolos());
                        jogador.setNumeroCartoes(dto.getNumeroCartoes());
                        return jogador;
                    })
                    .toList();

            model.setJogadorList(jogadorList);
            listJogadores.setItems(model.getJogadorList());

            listJogadores.getSelectionModel().selectedItemProperty()
                    .addListener((obs, oldSelection, newSelection) -> model.setCurrentJogador(newSelection));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideDetails() {
        boxNome.setVisible(false);
        boxEmail.setVisible(false);
        boxPosicao.setVisible(false);
        boxNumJogos.setVisible(false);
        boxNumGolos.setVisible(false);
        boxNumCartoes.setVisible(false);
    }

    private void showDetails() {
        boxNome.setVisible(true);
        boxEmail.setVisible(true);
        boxPosicao.setVisible(true);
        boxNumJogos.setVisible(true);
        boxNumGolos.setVisible(true);
        boxNumCartoes.setVisible(true);
    }

    private void setCurrentJogador(Jogador jogador) {
        if (jogador == null) {
            hideDetails();
            return;
        }

        labelNome.setText(jogador.getNome());
        labelEmail.setText(jogador.getEmail());
        labelPosicao.setText(jogador.getPosicaoPreferida());
        labelNumJogos.setText(String.valueOf(jogador.getNumeroJogos()));
        labelNumGolos.setText(String.valueOf(jogador.getNumeroGolos()));
        labelNumCartoes.setText(String.valueOf(jogador.getNumeroCartoes()));

        showDetails();
    }

    private void setClickListener() {
        listJogadores.setOnMouseClicked(event -> {
            Jogador selectedJogador = listJogadores.getSelectionModel().getSelectedItem();
            setCurrentJogador(selectedJogador);
        });
    }
}
