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
import pt.ul.fc.di.css.javafxexample.dto.CampeonatoDtoGet;
import pt.ul.fc.di.css.javafxexample.model.Campeonato;
import pt.ul.fc.di.css.javafxexample.model.DataModel;

import java.util.List;

public class ListCampeonatoController implements ControllerWithModel {
    private Stage stage;
    private DataModel model;

    @FXML private ListView<CampeonatoDtoGet> listCampeonatos;

    @FXML private Label labelNome;
    @FXML private Label labelModalidade;
    @FXML private Label labelNumJogos;
    @FXML private Label labelNumJogosFeitos;
    @FXML private Label labelNumEquipas;

    @FXML private HBox boxNumEquipas;
    @FXML private HBox boxNome;
    @FXML private HBox boxModalidade;
    @FXML private HBox boxNumJogos;
    @FXML private HBox boxNumJogosFeitos;
    @FXML private Button btnPontuacoes;

    @FXML
    void removerCampeonato() {
        CampeonatoDtoGet selected = listCampeonatos.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Util.mostrarAvisoTemporario("Selecione um campeonato primeiro", stage, "orange");
            return;
        }

        try {
            ApiClient.deleteCampeonato(selected.getId());  // Assumindo método delete
            listCampeonatos.getItems().remove(selected);
            listCampeonatos.getSelectionModel().clearSelection();
            Util.mostrarAvisoTemporario("✅ Campeonato removido com sucesso", stage, "green");
            hideDetails();
        } catch (Exception e) {
            Util.mostrarAvisoTemporario("❌ " + e.getMessage(), stage, "red");
        }
    }

    @FXML
    void editarCampeonato() {
        CampeonatoDtoGet selected = listCampeonatos.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Util.mostrarAvisoTemporario("Selecione um campeonato primeiro", stage, "orange");
            return;
        }

        if (selected.getNumeroJogos() == selected.getNumeroJogosFeitos()) {
            Util.mostrarAvisoTemporario("Não é possível editar um campeonato que já terminou", stage, "orange");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/ul/fc/di/css/javafxexample/view/atualizar_campeonato.fxml"));
            Parent root = loader.load();

            AtualizarCampeonatoController controller = loader.getController();
            Campeonato campeonato = new Campeonato();
            campeonato.setId(selected.getId());
            campeonato.setNome(selected.getNome());
            campeonato.setModalidade(selected.getModalidade());
            campeonato.setEquipaIds(selected.getEquipaIds());
            campeonato.setJogoIds(selected.getJogoIds());

            controller.initModel(stage, model, campeonato);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/pt/ul/fc/di/css/javafxexample/view/init.css").toExternalForm());

            stage.setWidth(600);
            stage.setHeight(600);
            stage.setScene(scene);
            stage.setTitle("Editar Campeonato");
            stage.show();

        } catch (Exception e) {
            Util.mostrarAvisoTemporario("❌ " + e.getMessage(), stage, "red");
        }
    }

    @FXML
    void abrirPontuacoes() {
        CampeonatoDtoGet selected = listCampeonatos.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Util.mostrarAvisoTemporario("Selecione um campeonato primeiro", stage, "orange");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/ul/fc/di/css/javafxexample/view/pontuacoes.fxml"));
            Parent root = loader.load();

            PontuacoesController controller = loader.getController();

            // Passa os dados do campeonato para o controlador da nova janela
            controller.initData(stage, model, selected);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/pt/ul/fc/di/css/javafxexample/view/init.css").toExternalForm());

            stage.setWidth(600);
            stage.setHeight(430);
            stage.setScene(scene);
            stage.setTitle("Pontuações - " + selected.getNome());
            stage.show();

        } catch (Exception e) {
            Util.mostrarAvisoTemporario("❌ " + e.getMessage(), stage, "red");
        }
    }

    @FXML
    void abrirCancelarJogo() {
        CampeonatoDtoGet selected = listCampeonatos.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Util.mostrarAvisoTemporario("Selecione um campeonato primeiro.", stage, "orange");
            return;
        }

        if (selected.getNumeroJogos() == selected.getNumeroJogosFeitos()) {
            Util.mostrarAvisoTemporario("Não é possível cancelar um jogo de um campeonato que já terminou", stage, "orange");
            return;
        }


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/ul/fc/di/css/javafxexample/view/cancelar_jogo.fxml"));
            Parent root = loader.load();

            CancelarJogoController controller = loader.getController();
            controller.initData(stage, model, selected);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/pt/ul/fc/di/css/javafxexample/view/init.css").toExternalForm());

            stage.setWidth(500);
            stage.setHeight(400);
            stage.setScene(scene);
            stage.setTitle("Cancelar Jogo - " + selected.getNome());
            stage.show();

        } catch (Exception e) {
            Util.mostrarAvisoTemporario("❌ " + e.getMessage(), stage, "red");
        }
    }


    @FXML
    void back(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/campeonatos.fxml", "SoccerNow", model, 600, 400);
    }

    @Override
    public void initModel(Stage stage, DataModel model) {
        this.stage = stage;
        this.model = model;
        hideDetails();
        setCampeonatoList();
        setClickListener();
    }

    private void setCampeonatoList() {
        try {
            List<CampeonatoDtoGet> campeonatos = ApiClient.getAllCampeonatos();
            listCampeonatos.getItems().setAll(campeonatos);
        } catch (Exception e) {
            Util.mostrarAvisoTemporario("❌ Erro ao carregar campeonatos: " + e.getMessage(), stage, "red");
        }
    }

    private void hideDetails() {
        boxNome.setVisible(false);
        boxModalidade.setVisible(false);
        boxNumJogos.setVisible(false);
        boxNumJogosFeitos.setVisible(false);
        boxNumEquipas.setVisible(false);
    }

    private void showDetails() {
        boxNome.setVisible(true);
        boxModalidade.setVisible(true);
        boxNumJogos.setVisible(true);
        boxNumJogosFeitos.setVisible(true);
        boxNumEquipas.setVisible(true);
    }


    private void setClickListener() {
        listCampeonatos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                hideDetails();
            } else {
                labelNome.setText(newVal.getNome());
                labelModalidade.setText(newVal.getModalidade());
                labelNumJogos.setText(String.valueOf(newVal.getNumeroJogos()));
                labelNumJogosFeitos.setText(String.valueOf(newVal.getNumeroJogosFeitos()));
                labelNumEquipas.setText(String.valueOf(newVal.getEquipaIds().size()));
                showDetails();
            }
        });
    }

    @Override
    public Stage getStage() { return stage; }
    @Override
    public void setStage(Stage stage) { this.stage = stage; }
}
