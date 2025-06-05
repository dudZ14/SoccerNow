package pt.ul.fc.di.css.javafxexample.controller;

import javafx.event.ActionEvent;
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

public class AtualizarJogadorController implements ControllerWithModel {

    private DataModel model;
    private Stage stage;
    private Jogador jogadorAtual;

    @FXML private Button btnUpdate;

    @FXML private TextField fieldNome;
    @FXML private TextField fieldEmail;
    @FXML private TextField fieldPassword;
    @FXML private ComboBox<Posicao> comboPosicao;

    @FXML private Label labelNome;
    @FXML private Label labelEmail;
    @FXML private Label labelPassword;
    @FXML private Label labelPosicao;

    @FXML
    void atualizarJogador(ActionEvent event) {
        String nome = fieldNome.getText().trim();
        String email = fieldEmail.getText().trim();
        String password = fieldPassword.getText().trim();
        Posicao posicao = comboPosicao.getValue();

        if (!validInputs(nome, email, password, posicao)) {
            return;
        }

        try {
            
        // Buscar todos os jogadores, excluindo o atual, para verificar e-mail único
        List<JogadorDtoGet> jogadoresExistentes = ApiClient.getAllJogadores().stream()
                    .filter(dto -> dto.getId() != jogadorAtual.getId())
                    .toList();
        boolean emailExisteJogadores = jogadoresExistentes.stream().anyMatch(j -> j.getEmail().equalsIgnoreCase(email));
        //verificar se nao existe nenhum arbitro com o email igual
        List<ArbitroDtoGet> arbitros = ApiClient.getAllArbitros();
        boolean emailExisteArbitros = arbitros.stream().anyMatch(a -> a.getEmail().equalsIgnoreCase(email));
        
        if (emailExisteJogadores || emailExisteArbitros) {
            Util.mostrarAvisoTemporario("❌ O e-mail tem de ser único", stage, "red");
            return;
        }

            JogadorDto dto = new JogadorDto(nome, email, password, posicao);
            ApiClient.updateJogador(jogadorAtual.getId(), dto);

            // Atualiza o modelo local
            Jogador jogadorAtualizado = new Jogador(nome, email, password, posicao);
            jogadorAtualizado.setId(jogadorAtual.getId());
            model.setCurrentJogador(jogadorAtualizado);

            unbindFields();

            labelNome.textProperty().bindBidirectional(fieldNome.textProperty());
            labelEmail.textProperty().bindBidirectional(fieldEmail.textProperty());
            labelPassword.textProperty().bindBidirectional(fieldPassword.textProperty());
            labelPosicao.setText(comboPosicao.getValue() != null ? comboPosicao.getValue().name() : "");

            Util.mostrarAvisoTemporario("✅ Jogador atualizado com sucesso", stage, "green");

        } catch (Exception e) {
            Util.mostrarAvisoTemporario("❌ " + e.getMessage(), stage, "red");

        }
    }

    public void initModel(Stage stage, DataModel model, Jogador jogador) {
        this.stage = stage;
        this.model = model;
        this.jogadorAtual = jogador;

        fieldNome.setText(jogador.getNome());
        fieldEmail.setText(jogador.getEmail());
        fieldPassword.setText(jogador.getPassword());
        Posicao posicao = Posicao.valueOf(jogador.getPosicaoPreferida());
        comboPosicao.setValue(posicao);
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
        labelPassword.textProperty().unbindBidirectional(fieldPassword.textProperty());
        labelPosicao.setText(comboPosicao.getValue() != null ? comboPosicao.getValue().name() : "");
    }

    @FXML
    public void initialize() {
        comboPosicao.getItems().setAll(Posicao.values());

        // Atualiza o labelPosicao quando o valor da ComboBox mudar
        comboPosicao.valueProperty().addListener((obs, oldVal, newVal) -> {
            labelPosicao.setText(newVal != null ? newVal.name() : "");
        });

        labelNome.setVisible(false);
        labelEmail.setVisible(false);
        labelPassword.setVisible(false);
        labelPosicao.setVisible(false);
    }

    @FXML
    void back(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/list_jogadores.fxml", "SoccerNow", model, 600, 400);
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initModel(Stage stage, DataModel model) {
        throw new UnsupportedOperationException("Unimplemented method 'initModel'");
    }
}
