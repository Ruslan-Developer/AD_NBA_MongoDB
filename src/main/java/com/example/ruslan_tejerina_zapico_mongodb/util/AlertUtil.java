package com.example.ruslan_tejerina_zapico_mongodb.util;

import javafx.scene.control.Alert;

public class AlertUtil {

    public static void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setTitle("Error");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
    public static void mostrarInfo(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Informacion");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

}
