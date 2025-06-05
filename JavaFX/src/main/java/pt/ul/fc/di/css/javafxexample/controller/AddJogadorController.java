package pt.ul.fc.di.css.javafxexample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.api.ApiClient;
import pt.ul.fc.di.css.javafxexample.dto.ArbitroDtoGet;
import pt.ul.fc.di.css.javafxexample.dto.JogadorDto;
import pt.ul.fc.di.css.javafxexample.dto.JogadorDto.Posicao;
import pt.ul.fc.di.css.javafxexample.dto.JogadorDtoGet;
import pt.ul.fc.di.css.javafxexample.model.DataModel;
import pt.ul.fc.di.css.javafxexample.model.Jogador;

import java.util.List;

public class AddJogadorController implements ControllerWithModel {

    private DataModel model;
    private Stage stage;

    @FXML private TextField fieldNome;
    @FXML private TextField fieldEmail;
    @FXML private TextField fieldPassword;
    @FXML private ComboBox<Posicao> comboPosicao;


    @FXML private Button btnAdd;
    @FXML private Label labelNome;
    @FXML private Label labelEmail;
    @FXML private Label labelPosicao;

    @FXML
    void addJogador() {
        labelNome.setVisible(false);
        labelEmail.setVisible(false);
        labelPosicao.setVisible(false);

        String nome = fieldNome.getText().trim();
        String email = fieldEmail.getText().trim();
        String password = fieldPassword.getText().trim();
        Posicao posicao = comboPosicao.getValue();


        if (!validInputs(nome, email, password, posicao)) {
            return;
        }

        try {
            // obter lista de arbitros e jogadores para verificar se o e-mail já existe
            List<JogadorDtoGet> jogadores = ApiClient.getAllJogadores();
            boolean emailExisteJogadores = jogadores.stream().anyMatch(j -> j.getEmail().equalsIgnoreCase(email));
            List<ArbitroDtoGet> arbitros = ApiClient.getAllArbitros();
            boolean emailExisteArbitros = arbitros.stream().anyMatch(a -> a.getEmail().equalsIgnoreCase(email));
            
            if (emailExisteJogadores || emailExisteArbitros) {
                Util.mostrarAvisoTemporario("❌ O e-mail tem de ser único", stage, "red");
                return;
            }

            JogadorDto dto = new JogadorDto(nome, email, password, posicao);

            ApiClient.createJogador(dto);

            Jogador jogador = new Jogador(nome, email, password, posicao);
            model.setCurrentJogador(jogador);

            unbindFields();
            labelNome.textProperty().bindBidirectional(fieldNome.textProperty());
            labelEmail.textProperty().bindBidirectional(fieldEmail.textProperty());
            labelPosicao.setText(comboPosicao.getValue() != null ? comboPosicao.getValue().name() : "");

            Util.mostrarAvisoTemporario("✅ Jogador adicionado com sucesso ", stage, "green");

        } catch (Exception e) {
            Util.mostrarAvisoTemporario("❌ Erro: " + e.getMessage(), stage, "red");
            e.printStackTrace();
        }
    }

    private boolean validInputs(String nome, String email, String password, Posicao posicao) {

        if (nome.isEmpty()) {
            Util.mostrarAvisoTemporario("❌ Nome é obrigatório", stage, "red");
            return false;
        }

        if (email.isEmpty()) {
            Util.mostrarAvisoTemporario("❌ Email é obrigatório", stage, "red");
            return false;
        }

        if (!email.contains("@")) {
            Util.mostrarAvisoTemporario("❌ Email deve ter @", stage, "red");
            return false;
        }

        if (password.isEmpty()) {
            Util.mostrarAvisoTemporario("❌ Password é obrigatória", stage, "red");
            return false;
        }

        if (posicao == null) {
            Util.mostrarAvisoTemporario("❌ Posição é obrigatória", stage, "red");
            return false;
        }

        return true;
    }

    private void unbindFields() {
        labelNome.textProperty().unbindBidirectional(fieldNome.textProperty());
        labelEmail.textProperty().unbindBidirectional(fieldEmail.textProperty());
    }

    @FXML
    public void initialize() {
        comboPosicao.getItems().setAll(Posicao.values());

        comboPosicao.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                labelPosicao.setVisible(false);
                labelPosicao.setText(newVal.name());
            }
        });


        labelNome.setVisible(false);
        labelEmail.setVisible(false);
        labelPosicao.setVisible(false);
    }

    @FXML
    void back(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/jogadores.fxml", "SoccerNow", model, 600, 400);
    }

    @Override
    public void initModel(Stage stage, DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model já foi inicializado");
        }
        this.stage = stage;
        this.model = model;
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
