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

public class AtualizarArbitroController implements ControllerWithModel {

    private DataModel model;
    private Stage stage;
    private Arbitro arbitroAtual; 


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
    void atualizarArbitro(ActionEvent event) throws IOException, InterruptedException {
        String nome = fieldNome.getText().trim();
        String email = fieldEmail.getText().trim();
        String password = fieldPassword.getText().trim();
        String anosExpStr = fieldAnosExp.getText().trim();
        boolean temCertificado = checkCertificado.isSelected();

        if (!validInputs(nome, email, password, anosExpStr)) {
            return;
        }

       
        // Buscar todos os arbitros, excluindo o atual, para verificar e-mail único
        List<ArbitroDtoGet> arbitrosExistentes = ApiClient.getAllArbitros().stream()
                    .filter(dto -> dto.getId() != arbitroAtual.getId())
                    .toList();
        boolean emailExisteArbitros = arbitrosExistentes.stream().anyMatch(a -> a.getEmail().equalsIgnoreCase(email));
        
        // verificar se nenhum jogador tem email igual
        List<JogadorDtoGet> jogadores = ApiClient.getAllJogadores();
        boolean emailExisteJogadores = jogadores.stream().anyMatch(j -> j.getEmail().equalsIgnoreCase(email));
        
        if (emailExisteJogadores || emailExisteArbitros) {
            Util.mostrarAvisoTemporario("❌ O e-mail tem de ser único", stage, "red");
            return;
        }
        int anosExp = Integer.parseInt(anosExpStr);

        ArbitroDto dto = new ArbitroDto(nome, email, password, temCertificado, anosExp);

        try {
            ApiClient.updateArbitro(arbitroAtual.getId(),dto);

            Arbitro arbitro = new Arbitro(nome, email, password, temCertificado, anosExp);
            model.setCurrentArbitro(arbitro); // You may need to add this method

            unbindFields();

            labelNome.textProperty().bindBidirectional(fieldNome.textProperty());
            labelEmail.textProperty().bindBidirectional(fieldEmail.textProperty());
            labelAnosExp.setText(String.valueOf(anosExp));
            labelCertificado.setText(temCertificado ? "Sim" : "Não");

            Util.mostrarAvisoTemporario("✅ Árbitro atualizado com sucesso", stage, "green");

        } catch (Exception e) {
            Util.mostrarAvisoTemporario("❌ " + e.getMessage(), stage, "red");
        }
    }


    public void initModel(Stage stage, DataModel model, Arbitro arbitro) {
        this.stage = stage;
        this.model = model;
        this.arbitroAtual = arbitro;

        fieldNome.setText(arbitro.getNome());
        fieldEmail.setText(arbitro.getEmail());
        fieldPassword.setText(arbitro.getPassword());
        fieldAnosExp.setText(String.valueOf(arbitro.getAnosDeExperiencia()));
        checkCertificado.setSelected(arbitro.getTemCertificado());
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
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/list_arbitros.fxml", "SoccerNow", model, 600, 400);
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
