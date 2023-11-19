package com.example.ruslan_tejerina_zapico_mongodb;

import com.example.ruslan_tejerina_zapico_mongodb.modelo.Jugador;
import com.example.ruslan_tejerina_zapico_mongodb.repositorio.JugadorCRUD;
import com.example.ruslan_tejerina_zapico_mongodb.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    private Button bt_buscar;

    @FXML
    private Button bt_eliminar;

    @FXML
    private Button bt_insertar;

    @FXML
    private Button bt_listar;

    @FXML
    private Button bt_modificar;

    @FXML
    private TableColumn<?, ?> col_altura;

    @FXML
    private TableColumn<?, ?> col_id;

    @FXML
    private TableColumn<?, ?> col_nombre;

    @FXML
    private TableColumn<?, ?> col_peso;

    @FXML
    private TableColumn<?, ?> col_posicion;

    @FXML
    private TableColumn<?, ?> col_procedencia;

    @FXML
    private TableView<Jugador> tabla_jugadores;

    @FXML
    private TextField tf_altura;

    @FXML
    private TextField tf_id;

    @FXML
    private TextField tf_nombre;

    @FXML
    private TextField tf_peso;

    @FXML
    private TextField tf_posicion;

    @FXML
    private TextField tf_procedencia;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JugadorCRUD jugadorCRUD = new JugadorCRUD();
        ObservableList<Jugador> items = jugadorCRUD.listarJugadores();
        tabla_jugadores.setItems(items);
        this.tabla_jugadores.refresh();

        this.col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.col_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.col_procedencia.setCellValueFactory(new PropertyValueFactory<>("procedencia"));
        this.col_altura.setCellValueFactory(new PropertyValueFactory<>("altura"));
        this.col_peso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        this.col_posicion.setCellValueFactory(new PropertyValueFactory<>("posicion"));

        /*
        * Evento para seleccionar un jugador de la tabla con doble click sobre la fila seleccionada
        * y mostrarlo en los campos de texto.
        */

        tabla_jugadores.setRowFactory(tv -> {
            TableRow<Jugador> fila = new TableRow<>();
            fila.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!fila.isEmpty())) {
                    Jugador rowData = fila.getItem();
                    tf_id.setText(String.valueOf(rowData.getId()));
                    tf_nombre.setText(rowData.getNombre());
                    tf_procedencia.setText(rowData.getProcedencia());
                    tf_altura.setText(String.valueOf(rowData.getAltura()));
                    tf_peso.setText(String.valueOf(rowData.getPeso()));
                    tf_posicion.setText(rowData.getPosicion());
                }
            });
            return fila;
        });

    }

    /**
     * Método para insertar un jugador en la base de datos.
     * @param event El evento que se produce al pulsar el botón.
     */
    @FXML
    public void agregarJugador(ActionEvent event) {
        /*
        * Comprobamos que los campos no estén vacíos.
        */

        if (tf_nombre.getText().isEmpty() || tf_procedencia.getText().isEmpty() ||
                tf_altura.getText().isEmpty() || tf_peso.getText().isEmpty() ||
                tf_posicion.getText().isEmpty()) {
            AlertUtil.mostrarError("Todos los campos menos el ID son obligatorios");
        } else if (Integer.parseInt(tf_altura.getText()) < 0 || Integer.parseInt(tf_peso.getText()) < 0) {
            AlertUtil.mostrarError("La altura y el peso no pueden ser negativos");

        } else {


            JugadorCRUD jugadorCRUD = new JugadorCRUD();
            Jugador jugador = new Jugador(
                    //Integer.parseInt(tf_id.getId()),
                    tf_nombre.getText(),
                    tf_procedencia.getText(),
                    Integer.parseInt(tf_altura.getText()),
                    Integer.parseInt(tf_peso.getText()),
                    tf_posicion.getText()
            );


            /*
                * Comprobamos si se ha insertado correctamente el jugador en la base de datos y mostramos un mensaje
                * de error o de información según el resultado de la operación.
            */
            boolean comprobacion = jugadorCRUD.insertarJugador(jugador);
            actualizarDatos();
            if (!comprobacion) {
                AlertUtil.mostrarError("Ha ocurrido un error en la inserción del jugador ");
            } else {
                actualizarDatos();
                AlertUtil.mostrarInfo("Jugador insertado correctamente");
            }

        }


    }

    /**
     * Método para listar los jugadores de la base de datos.
     * @param event El evento que se produce al pulsar el botón.
     */
    @FXML
    public void visualizarJugadores(ActionEvent event) {
        JugadorCRUD jugadorCRUD = new JugadorCRUD();
        ObservableList<Jugador> items = jugadorCRUD.listarJugadores();
        tabla_jugadores.setItems(items);
        this.tabla_jugadores.refresh();
    }

    /**
     * Método para modificar un jugador de la base de datos.
     * @param event El evento que se produce al pulsar el botón.
     */
    @FXML
    public void modificarJugador(ActionEvent event) {

        JugadorCRUD jugadorCRUD = new JugadorCRUD();
        Jugador jugador = new Jugador(

                Integer.parseInt(tf_id.getText()),
                tf_nombre.getText(),
                tf_procedencia.getText(),
                Integer.parseInt(tf_altura.getText()),
                Integer.parseInt(tf_peso.getText()),
                tf_posicion.getText()
                );

        jugadorCRUD.modificarJugador(jugador);
        JOptionPane.showMessageDialog(null, "Jugador modificado correctamente");
        actualizarDatos();
    }

    /**
     * Método para eliminar un jugador de la base de datos.
     * @param event El evento que se produce al pulsar el botón.
     */
    @FXML
    public void eliminarJugador(ActionEvent event){
        //TableView<Jugador> tabla_jugadores = new TableView<>();
        Jugador seleccionarJugador = tabla_jugadores.getSelectionModel().getSelectedItem();
        JugadorCRUD jugadorCRUD = new JugadorCRUD();
        jugadorCRUD.eliminarJugador(seleccionarJugador);

        JOptionPane.showMessageDialog(null, "Jugador eliminado correctamente");

        actualizarDatos();
    }

    /**
     * Método para buscar jugadores en la base de datos.
     * @param event El evento que se produce al pulsar el botón.
     * @return Una lista observable de objetos Jugador que coinciden con los parámetros de búsqueda.
     */
    @FXML
    public ObservableList<Jugador> buscarJugadores(ActionEvent event) {
        JugadorCRUD jugadorCRUD = new JugadorCRUD();
        ObservableList<Jugador> items = jugadorCRUD.buscarJugadores(tf_nombre.getText(), tf_procedencia.getText(), tf_posicion.getText());
        tabla_jugadores.setItems(items);
        this.tabla_jugadores.refresh();
        return items;
    }

    /**
     * Método para actualizar los datos de la tabla.
     */
    public void actualizarDatos(){
        JugadorCRUD actualizar = new JugadorCRUD();
        ObservableList<Jugador> items = actualizar.listarJugadores();
        tabla_jugadores.setItems(items);
        this.tabla_jugadores.refresh();
    }

}



