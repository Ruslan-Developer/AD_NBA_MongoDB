package com.example.ruslan_tejerina_zapico_mongodb.modelo;

/**
 * Clase Jugador con sus atributos, constructores, getters y setters y toString
 * para mostrar los datos de los jugadores en la tabla de la interfaz gráfica
 */
public class Jugador {
    /**
     * Atributos de la clase Jugador
     */
    private int id;
    private String nombre;
    private String procedencia;
    private int altura;
    private int peso;
    private String posicion;

    /**
     * Constructor vacio
     */
    public Jugador() {

    }

    /**
     * Constructor con todos los atributos
     * @param id
     * @param nombre
     * @param procedencia
     * @param altura
     * @param peso
     * @param posicion
     */
    public Jugador(int id, String nombre, String procedencia, int altura, int peso, String posicion) {
        this.id = id;
        this.nombre = nombre;
        this.procedencia = procedencia;
        this.altura = altura;
        this.peso = peso;
        this.posicion = posicion;
    }

    /**
     * Constructor sin id
     * @param nombre
     * @param procedencia
     * @param altura
     * @param peso
     * @param posicion
     */
    public Jugador(String nombre, String procedencia, int altura, int peso, String posicion) {
        this.nombre = nombre;
        this.procedencia = procedencia;
        this.altura = altura;
        this.peso = peso;
        this.posicion = posicion;
    }

    /**
     * Getters y setters de los atributos
     * @return Devuelve el valor de los atributos
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProcedencia() {
        return procedencia;
    }

    public void setProcedencia(String procedencia) {
        this.procedencia = procedencia;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    /**
     * Método toString para mostrar los datos de los jugadores en la tabla de la interfaz gráfica
     * @return Devuelve los datos de los jugadores
     */

    @Override
    public String toString() {
        return "Nombre: "+nombre + "\n "+"Procedencia: " + procedencia + "\n "+" Altura: " + altura+ " cm " + "\n "+" Peso: " + peso + "\n "+ "Posición: " + posicion;
    }
}
