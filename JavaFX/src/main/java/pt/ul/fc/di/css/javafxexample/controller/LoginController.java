package pt.ul.fc.di.css.javafxexample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.model.DataModel;

public class LoginController implements ControllerWithModel {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final String initFXML = "/pt/ul/fc/di/css/javafxexample/view/init.fxml";

    private Stage stage;
    private DataModel model;

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty()) {
            Util.mostrarAvisoTemporario("❌ Email é obrigatório", stage, "red");
        } else if (!email.contains("@")) {
            Util.mostrarAvisoTemporario("❌ Email tem de ter @", stage, "red");
        } else if (password.isEmpty()) {
            Util.mostrarAvisoTemporario("❌ Password é obrigatória", stage, "red");
        } else {
            Util.switchScene(stage, initFXML, "SoccerNow", model, 650, 430);
        }
    }


    @Override
    public void initModel(Stage stage, DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.stage = stage;
        this.model = model;
    }

    @Override
    public Stage getStage() {
        return this.stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
