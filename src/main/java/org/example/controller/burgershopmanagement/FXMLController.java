package org.example.controller.burgershopmanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;

public class FXMLController extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/controller/burgershopmanagementsystem/Burger-FXML.fxml")));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(" MOS Burger Shop Management");
        stage.setMinHeight(410);
        stage.setMinWidth(610);
        stage.show();
    }
}
