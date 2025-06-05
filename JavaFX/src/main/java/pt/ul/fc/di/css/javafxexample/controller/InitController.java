package pt.ul.fc.di.css.javafxexample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.model.DataModel;

public class InitController implements ControllerWithModel {

    private DataModel model;

    private Stage stage;

    @FXML
    private Button createButton;

    @FXML
    private Button listButton;


    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void createArbitro(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/add_arbitro.fxml",
                "Criar Árbitro", model, 600, 400);
    }

    @FXML
    void createJogador(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/add_jogador.fxml",
                "Criar Jogador", model, 600, 400);
    }

    @FXML
    void createEquipa(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/add_equipa.fxml",
                "Criar Equipa", model, 600, 400);
    }

    @FXML
    void createJogo(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/add_jogo.fxml",
                "Criar Jogo", model, 600, 630);
    }

    @FXML
    void createCampeonato(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/add_campeonato.fxml",
                "Criar Campeonato", model, 600, 600);
    }

    @FXML
    void listArbitros(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/list_arbitros.fxml",
                "Lista de Árbitros", model, 600, 400);
    }

    @FXML
    void listJogadores(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/list_jogadores.fxml",
                "Lista de Jogadores", model, 600, 400);
    }

    @FXML
    void listEquipas(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/list_equipas.fxml",
                "Lista de Equipas", model, 700, 370);
    }

    @FXML
    void listJogos(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/list_jogos.fxml",
                "Lista de Jogos", model, 600, 400);
    }

    @FXML
    void listCampeonatos(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/list_campeonatos.fxml",
                "Lista de Campeonatos", model, 600, 370);
    }

    @FXML
    private void arbitros(MouseEvent  event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/arbitros.fxml", "Ações sobre Árbitros", model, 600, 400);
    }

    @FXML
    private void jogadores(MouseEvent  event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/jogadores.fxml", "Ações sobre Jogadores", model, 600, 400);
    }

    @FXML
    private void equipas(MouseEvent  event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/equipas.fxml", "Ações sobre Equipas", model, 600, 400);
    }

    @FXML
    private void jogos(MouseEvent  event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/jogos.fxml", "Ações sobre Jogos", model, 600, 400);
    }

    @FXML
    private void campeonatos(MouseEvent  event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/campeonatos.fxml", "Ações sobre Campeonatos", model, 600, 400);
    }

    @FXML
    private void menu(MouseEvent  event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/init.fxml", "Menu", model, 650, 430);
    }


    @FXML
    void quit(MouseEvent event) {
        System.exit(0);
    }

    public void initModel(Stage stage, DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.stage = stage;
        this.model = model;
    }

}
