package pt.ul.fc.di.css.javafxexample.controller;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.api.ApiClient;
import pt.ul.fc.di.css.javafxexample.dto.ArbitroDto;
import pt.ul.fc.di.css.javafxexample.dto.ArbitroDtoGet;
import pt.ul.fc.di.css.javafxexample.dto.JogadorDtoGet;
import pt.ul.fc.di.css.javafxexample.model.Arbitro;
import pt.ul.fc.di.css.javafxexample.model.DataModel;

public class AddArbitroController implements ControllerWithModel {

    private DataModel model;
    private Stage stage;

    @FXML private Button btnAdd;
    @FXML private MenuItem btnBack;

    @FXML private TextField fieldNome;
    @FXML private TextField fieldEmail;
    @FXML private TextField fieldPassword;
    @FXML private TextField fieldAnosExp;
    @FXML private CheckBox checkCertificado;

    @FXML private Label labelNome;
    @FXML private Label labelEmail;
    @FXML private Label labelAnosExp;
    @FXML private Label labelCertificado;

    @FXML
    void addArbitro(ActionEvent event) throws IOException, InterruptedException {
        labelNome.setVisible(false);
        labelEmail.setVisible(false);
        labelAnosExp.setVisible(false);
        labelCertificado.setVisible(false);

        String nome = fieldNome.getText().trim();
        String email = fieldEmail.getText().trim();
        String password = fieldPassword.getText().trim();
        String anosExpStr = fieldAnosExp.getText().trim();
        boolean temCertificado = checkCertificado.isSelected();

        if (!validInputs(nome, email, password, anosExpStr)) {
            return;
        }

        // obter lista de arbitros e jogadores para verificar se o e-mail já existe
        List<JogadorDtoGet> jogadores = ApiClient.getAllJogadores();
        boolean emailExisteJogadores = jogadores.stream().anyMatch(j -> j.getEmail().equalsIgnoreCase(email));
        List<ArbitroDtoGet> arbitros = ApiClient.getAllArbitros();
        boolean emailExisteArbitros = arbitros.stream().anyMatch(a -> a.getEmail().equalsIgnoreCase(email));
        
        if (emailExisteJogadores || emailExisteArbitros) {
            Util.mostrarAvisoTemporario("❌ O e-mail tem de ser único", stage, "red");
            return;
        }

        if (anosExpStr.isEmpty()) {
            Util.mostrarAvisoTemporario("❌ Os anos de experiência são obrigatórios", stage, "red");
            return;
        }

        int anosExp = Integer.parseInt(anosExpStr);

        ArbitroDto dto = new ArbitroDto(nome, email, password, temCertificado, anosExp);

        try {
            ApiClient.createArbitro(dto);

            Arbitro arbitro = new Arbitro(nome, email, password, temCertificado, anosExp);
            model.setCurrentArbitro(arbitro); // You may need to add this method

            unbindFields();

            labelNome.textProperty().bindBidirectional(fieldNome.textProperty());
            labelEmail.textProperty().bindBidirectional(fieldEmail.textProperty());
            labelAnosExp.setText(String.valueOf(anosExp));
            labelCertificado.setText(temCertificado ? "Sim" : "Não");

            Util.mostrarAvisoTemporario("✅ Árbitro adicionado com sucesso ", stage, "green");

        } catch (Exception e) {
            Util.mostrarAvisoTemporario("❌ Erro: " + e.getMessage(), stage, "red");
            e.printStackTrace();
        }
    }

    private boolean validInputs(String nome, String email, String password, String anosExp) {

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

        if (anosExp.isEmpty()) {
            Util.mostrarAvisoTemporario("❌ Anos de experiência são obrigatórios", stage, "red");
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
        labelNome.setVisible(false);
        labelEmail.setVisible(false);
        labelAnosExp.setVisible(false);
        labelCertificado.setVisible(false);
    }

    @FXML
    void back(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/arbitros.fxml", "SoccerNow", model, 600, 400);
    }

    public void initModel(Stage stage, DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
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
