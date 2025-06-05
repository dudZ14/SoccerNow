package pt.ul.fc.di.css.javafxexample;

import javafx.application.Application;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.controller.Util;
import pt.ul.fc.di.css.javafxexample.model.DataModel;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        String prefix = "/pt/ul/fc/di/css/javafxexample/view/";
        DataModel model = new DataModel();

        Util.switchScene(primaryStage, prefix + "login.fxml", "Login", model, 600, 430);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
