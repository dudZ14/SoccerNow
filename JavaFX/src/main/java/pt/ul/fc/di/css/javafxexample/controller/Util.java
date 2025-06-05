package pt.ul.fc.di.css.javafxexample.controller;
import pt.ul.fc.di.css.javafxexample.model.DataModel;

import java.io.IOException;

//pop up

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;

public class Util {

    public static void switchScene(Stage stage, String fxmlPath, String title, DataModel model,double width, double height) {
        try {
            FXMLLoader loader = new FXMLLoader(Util.class.getResource(fxmlPath));
            Scene newScene = new Scene(loader.load());

            newScene.getStylesheets().add(Util.class.getResource("/pt/ul/fc/di/css/javafxexample/view/init.css").toExternalForm());

            ControllerWithModel controller = loader.getController();
            controller.initModel(stage, model);

            stage.getIcons().add(new Image(Util.class.getResourceAsStream("/pt/ul/fc/di/css/javafxexample/view/icon.jpg")));
            stage.setWidth(width);
            stage.setHeight(height);
            stage.setTitle(title);
            stage.setScene(newScene);
            stage.show();

        } catch (IOException | ClassCastException e) {
            e.printStackTrace();
        }
    }

    //mostra mensagem de pop up personalizada
    public static void mostrarAvisoTemporario(String mensagem, Stage stage, String cor) {
        Label aviso = new Label(mensagem);
        aviso.setStyle("-fx-background-color: " + cor + "; -fx-text-fill: white; -fx-padding: 10px; -fx-background-radius: 5;");
        aviso.setFont(new Font("Arial", 14));
        aviso.setAlignment(Pos.CENTER);
        aviso.setOpacity(0.8); 

        StackPane root = (StackPane) stage.getScene().getRoot();
        root.getChildren().add(aviso);
        StackPane.setAlignment(aviso, Pos.CENTER);

        PauseTransition pause = new PauseTransition(Duration.seconds(1));

        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), aviso);
        fade.setFromValue(0.8);
        fade.setToValue(0.0);

        fade.setOnFinished(e -> root.getChildren().remove(aviso));

        SequentialTransition seq = new SequentialTransition(pause, fade);
        seq.play();
    }
}
