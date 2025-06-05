package pt.ul.fc.di.css.javafxexample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.api.ApiClient;
import pt.ul.fc.di.css.javafxexample.dto.CartaoJogadorDto;
import pt.ul.fc.di.css.javafxexample.dto.EstatisticaDto;
import pt.ul.fc.di.css.javafxexample.dto.GoloDto;
import pt.ul.fc.di.css.javafxexample.dto.JogadorDtoGet;
import pt.ul.fc.di.css.javafxexample.dto.JogadorNoJogoDto;
import pt.ul.fc.di.css.javafxexample.model.DataModel;
import pt.ul.fc.di.css.javafxexample.model.Jogo;

import java.util.ArrayList;
import java.util.List;

public class RegistarResultadoController implements ControllerWithModel {

    @FXML private VBox golosBox;
    @FXML private VBox cartoesBox;

    private Stage stage;
    private DataModel model;
    private Jogo jogoAtual;


    @FXML
    void adicionarGolo(ActionEvent event) {
        HBox goloLine = new HBox(10);
        goloLine.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        VBox camposBox = new VBox(5);
        camposBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        HBox fields = new HBox(5);

        ComboBox<JogadorNoJogoDto> jogadorCombo = new ComboBox<>();
        jogadorCombo.setItems(jogoAtual.getJogadoresNoJogo());
        jogadorCombo.setPromptText("Jogador que marcou");

        TextField minutoField = new TextField();
        minutoField.setPromptText("Minuto do golo");

        fields.getChildren().addAll(jogadorCombo, minutoField);
        camposBox.getChildren().add(fields);

        Button removeButton = new Button("X");
        removeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: red; -fx-font-weight: bold;");
        removeButton.setOnAction(e -> golosBox.getChildren().remove(goloLine));

        VBox removeBox = new VBox(removeButton);
        removeBox.setAlignment(javafx.geometry.Pos.CENTER);

        goloLine.getChildren().addAll(camposBox, removeBox);
        golosBox.getChildren().add(goloLine);
    }



    @FXML
    void adicionarCartao(ActionEvent event) {
        HBox cartaoLine = new HBox(10);
        cartaoLine.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        HBox camposBox = new HBox(5);
        camposBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        ComboBox<JogadorNoJogoDto> jogadorCombo = new ComboBox<>();
        jogadorCombo.setItems(jogoAtual.getJogadoresNoJogo());
        jogadorCombo.setPromptText("Jogador");

        ComboBox<String> tipoCartaoBox = new ComboBox<>();
        tipoCartaoBox.getItems().addAll("Amarelo", "Vermelho");
        tipoCartaoBox.setPromptText("Tipo de cartão");

        camposBox.getChildren().addAll(jogadorCombo, tipoCartaoBox);

        Button removeButton = new Button("X");
        removeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: red; -fx-font-weight: bold;");
        removeButton.setOnAction(e -> cartoesBox.getChildren().remove(cartaoLine));

        VBox removeBox = new VBox(removeButton);
        removeBox.setAlignment(javafx.geometry.Pos.CENTER);

        cartaoLine.getChildren().addAll(camposBox, removeBox);
        cartoesBox.getChildren().add(cartaoLine);
    }



    @FXML
    void registarResultado(ActionEvent event) {
        try {
            // Usa o jogoAtual do initModel
            Long jogoId = jogoAtual.getId();

            List<GoloDto> golos = new ArrayList<>();
            for (javafx.scene.Node node : golosBox.getChildren()) {
                if (node instanceof HBox hBox && hBox.getChildren().size() == 2) {
                    VBox camposBox = (VBox) hBox.getChildren().get(0);
                    HBox fields = (HBox) camposBox.getChildren().get(0);

                    @SuppressWarnings("unchecked")
                    ComboBox<JogadorNoJogoDto> jogadorCombo = (ComboBox<JogadorNoJogoDto>) fields.getChildren().get(0);
                    TextField minutoField = (TextField) fields.getChildren().get(1);

                    JogadorNoJogoDto jogador = jogadorCombo.getValue();
                    if (jogador != null) {
                        Long jogadorId = jogador.getJogadorId();
                        int minuto = Integer.parseInt(minutoField.getText().trim());
                        golos.add(new GoloDto(jogadorId, minuto));
                    }
                }
            }

            List<CartaoJogadorDto> cartoes = new ArrayList<>();
            for (javafx.scene.Node node : cartoesBox.getChildren()) {
                if (node instanceof HBox hBox && hBox.getChildren().size() == 2) {
                    HBox camposBox = (HBox) hBox.getChildren().get(0);

                    @SuppressWarnings("unchecked")
                    ComboBox<JogadorNoJogoDto> jogadorCombo = (ComboBox<JogadorNoJogoDto>) camposBox.getChildren().get(0);
                    ComboBox<?> tipoBox = (ComboBox<?>) camposBox.getChildren().get(1);

                    JogadorNoJogoDto jogador = jogadorCombo.getValue();
                    if (jogador != null && tipoBox.getValue() != null) {
                        Long jogadorId = jogador.getJogadorId();
                        String tipoCartao = tipoBox.getValue().toString().toUpperCase();
                        cartoes.add(new CartaoJogadorDto(jogadorId, tipoCartao));
                    }
                }
            }

            EstatisticaDto estatistica = new EstatisticaDto(golos, cartoes);
            ApiClient.registrarResultado(jogoId, estatistica);

            Util.mostrarAvisoTemporario("✅ Resultado registado com sucesso!", stage, "green");;
            

        } catch (Exception e) {
            Util.mostrarAvisoTemporario("❌ " + e.getMessage(), stage, "red");
        }
    }

    @FXML
    void back(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/list_jogos.fxml", "SoccerNow", model, 600, 400);
    }

    @Override
    public void initModel(Stage stage, DataModel model) {
        // Pode permanecer vazio ou lançar exceção para forçar o uso do método com Jogo
        throw new UnsupportedOperationException("Use initModel com Jogo");
    }

    public void initModel(Stage stage, DataModel model, Jogo jogo) {
        this.stage = stage;
        this.model = model;
        this.jogoAtual = jogo;
        try {
            List<JogadorDtoGet> todosJogadores = ApiClient.getAllJogadores();

            for (JogadorNoJogoDto jogadorNoJogo : jogo.getJogadoresNoJogo()) {
                for (JogadorDtoGet jogadorDto : todosJogadores) {
                    if (jogadorDto.getId().equals(jogadorNoJogo.getJogadorId())) {
                        jogadorNoJogo.setNomeJogador(jogadorDto.getNome());
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
