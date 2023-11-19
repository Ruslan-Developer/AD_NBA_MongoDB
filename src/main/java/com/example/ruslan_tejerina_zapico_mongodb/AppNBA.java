package com.example.ruslan_tejerina_zapico_mongodb;

import com.example.ruslan_tejerina_zapico_mongodb.modelo.Jugador;
import com.example.ruslan_tejerina_zapico_mongodb.repositorio.JugadorCRUD;
import com.example.ruslan_tejerina_zapico_mongodb.util.ConexionBBDD;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppNBA extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppNBA.class.getResource("menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 800);
        stage.setTitle("Menú principal NBA");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        //ConexionBBDD.conectarMongoDB();


        launch();
    }
}