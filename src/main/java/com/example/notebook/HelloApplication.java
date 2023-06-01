package com.example.notebook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("notebook.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        HelloController cntrl = fxmlLoader.getController();
        stage.setTitle("Notebook");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
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