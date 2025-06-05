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
import pt.ul.fc.di.css.javafxexample.dto.ArbitroDtoGet;
import pt.ul.fc.di.css.javafxexample.model.Arbitro;
import pt.ul.fc.di.css.javafxexample.model.DataModel;

import java.util.List;

public class ListArbitroController implements ControllerWithModel {

    private Stage stage;
    private DataModel model;

    @FXML private MenuItem itemBack;

    @FXML private Label labelNome;
    @FXML private Label labelEmail;
    @FXML private Label labelCertificado;
    @FXML private Label labelAnosExp;
    @FXML private Label labelNumJogos;
    @FXML private Label labelNumCartoes;

    @FXML private HBox boxNome;
    @FXML private HBox boxEmail;
    @FXML private HBox boxAnosExp;
    @FXML private HBox boxCertificado;
    @FXML private HBox boxNumJogos;
    @FXML private HBox boxNumCartoes;
    @FXML private Button btnRemove;


    @FXML private ListView<Arbitro> listArbitros;

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
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/arbitros.fxml", "SoccerNow", model, 600, 400);
    }

    @FXML
    void removerArbitro() {
        Arbitro selected = listArbitros.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Util.mostrarAvisoTemporario("Selecione um árbitro primeiro", stage, "orange");
            return;
        }

        try {
            ApiClient.deleteArbitro(selected.getId());

            model.getArbitroList().remove(selected);
            listArbitros.getSelectionModel().clearSelection();
            Util.mostrarAvisoTemporario("✅ Árbitro removido com sucesso",stage,"green");
            hideDetails();
        } catch (Exception e) {
            Util.mostrarAvisoTemporario("❌ " + e.getMessage(),stage,"red");
        }

    }

    @FXML
    void editarArbitro() {
        Arbitro selected = listArbitros.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Util.mostrarAvisoTemporario("Selecione um árbitro primeiro", stage, "orange");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/ul/fc/di/css/javafxexample/view/atualizar_arbitro.fxml"));
            Parent root = loader.load();

            AtualizarArbitroController controller = loader.getController();
            controller.initModel(stage, model, selected);

            Scene scene = new Scene(root);
            //aplicar o css
            scene.getStylesheets().add(getClass().getResource("/pt/ul/fc/di/css/javafxexample/view/init.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Editar Árbitro");
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

        setArbitroList(model);
    }


    private void setArbitroList(DataModel model) {
        try {
            List<ArbitroDtoGet> arbitros = ApiClient.getAllArbitros();

            List<Arbitro> arbitroList = arbitros.stream()
                    .map(dto -> {
                            Arbitro arbitro = new Arbitro(dto.getNome(), dto.getEmail(), dto.getPassword(),
                                                        dto.getTemCertificado(), dto.getAnosDeExperiencia());
                            arbitro.setId(dto.getId());
                            arbitro.setNumeroJogos(dto.getNumeroJogos());
                            arbitro.setNumeroCartoesMostrados(dto.getNumeroCartoesMostrados());
                            return arbitro;
                        })

                    .toList();

            model.setArbitroList(arbitroList); 
            listArbitros.setItems(model.getArbitroList());

            listArbitros.getSelectionModel().selectedItemProperty()
                    .addListener((obs, oldSelection, newSelection) -> model.setCurrentArbitro(newSelection));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void hideDetails() {
        boxNome.setVisible(false);
        boxEmail.setVisible(false);
        boxAnosExp.setVisible(false);
        boxCertificado.setVisible(false);
        boxNumJogos.setVisible(false);
        boxNumCartoes.setVisible(false);
    }

    private void showDetails() {
        boxNome.setVisible(true);
        boxEmail.setVisible(true);
        boxAnosExp.setVisible(true);
        boxCertificado.setVisible(true);
        boxNumJogos.setVisible(true);
        boxNumCartoes.setVisible(true);
    }

    private void setCurrentArbitro(Arbitro arbitro) {
        if (arbitro == null) {
            hideDetails();
            return;
        }

        labelNome.setText(arbitro.getNome());
        labelEmail.setText(arbitro.getEmail());
        labelAnosExp.setText(String.valueOf(arbitro.getAnosDeExperiencia()));
        labelCertificado.setText(arbitro.getTemCertificado() ? "Sim" : "Não");
        labelNumJogos.setText(String.valueOf(arbitro.getNumeroJogos()));
        labelNumCartoes.setText(String.valueOf(arbitro.getNumeroCartoesMostrados()));


        showDetails();
    }


    private void setClickListener() {
        listArbitros.setOnMouseClicked(event -> {
            Arbitro selectedArbitro = listArbitros.getSelectionModel().getSelectedItem();
            setCurrentArbitro(selectedArbitro);
        });
    }


}
