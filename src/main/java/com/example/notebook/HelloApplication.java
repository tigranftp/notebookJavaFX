package com.example.notebook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("notebook.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 470);
        HelloController cntrl = fxmlLoader.getController();
        stage.setTitle("Notebook");
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> {
            cntrl.onClose();
        });
        stage.show();
    }

    public static void main(String[] args) {

        launch();
    }
}