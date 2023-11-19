package com.example.ruslan_tejerina_zapico_mongodb.repositorio;

import com.example.ruslan_tejerina_zapico_mongodb.modelo.Jugador;
import com.example.ruslan_tejerina_zapico_mongodb.util.ConexionBBDD;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.Document;

//import javax.swing.text.Document;

public class JugadorCRUD {
      /*
        *    Explicación de la clase Document:
        *    En MongoDB, los datos se almacenan en documentos BSON, que son una representación binaria de JSON.
        *    Los documentos BSON tienen algunas ventajas sobre JSON, como el soporte para tipos de datos adicionales,
        *    y son utilizados por MongoDB para almacenar y enviar datos.
      */
    private MongoClient conexion;
    private MongoDatabase db;
    Document doc;
    MongoCollection<Document> collection = null; //Creamos una coleccion de tipo Document para poder insertar los datos en la base de datos

    MongoCollection<Document> counters; //Creamos un counters para poder incrementar el id de los jugadores
    /**
     * Inserta un jugador en la base de datos.
     * @param jugador El jugador a insertar.
     * @return true si el jugador se insertó correctamente, false en caso contrario.
     */

    public boolean insertarJugador(Jugador jugador){

            //Nos conectamos a la base de datos nba
            conectarse();

             //Creamos la colleción en la base de datos nba llamada jugadores al inicio del programa
             //db.createCollection("jugadores");

             //Obtenemos la coleccion jugadores de la base de datos nba
             collection = db.getCollection("jugadores");
             //Obtenemos la coleccion counters de la base de datos nba
             counters = db.getCollection("counters");


        if (counters.countDocuments() == 0) { //Si la colección está vacía, insertamos un documento con el valor inicial del contador
            Document counter = new Document("_id", "id") //Campo y valor del documento counter
                    .append("seq", 0); //Campo seq que se utiliza como contador con valor inicial 0
            counters.insertOne(counter); //Insertamos el documento en la colección counters
        }

        Document find = new Document("_id", "id"); //Se crea un documento find que se usa como filtro para buscar el documento counter
        Document update = new Document("$inc", new Document("seq", 1)); //Crea un nuevo documento update con el operador $inc para incrementar en 1, el id del nuevo jugador insertado

        /*   Aqui esta la clave: la linea de abajo busca el primer documento en la colección counters que coincide con el documento find
          *  y luego actualiza ese documento con la operación update.
          *  La operación findOneAndUpdate devuelve el documento original antes de la actualización, por lo que se almacena en result.
        */

        Document result = counters.findOneAndUpdate(find, update);
        int id = result.getInteger("seq"); //Obtenemos el valor del campo seq del documento result y lo guardamos en la variable id
            /*
            //Eliminar la colección y empezar de nuevo
            collection.drop();
            System.out.println("La coleccion se ha borrado Correctamente.\n");

             */
            /*
            //Creo una nueva coleccion
            db.createCollection("jugadores");
            System.out.println("Coleccion creada Satisfactoriamente.\n");

             */

            //Insertamos los datos en la base de datos nba en la coleccion jugadores
             doc = new Document("id", id) //Aqui es donde le pasamos el valor del id obtenido de la colección counters al nuevo documento
                    .append("nombre", jugador.getNombre())
                    .append("procedencia", jugador.getProcedencia())
                    .append("altura", jugador.getAltura())
                    .append("peso", jugador.getPeso())
                    .append("posicion", jugador.getPosicion());

        try {
            collection.insertOne(doc);
        } catch (Exception e) {
            // Si ocurre una excepción al insertar el documento, desconecta y devuelve false
            desconectarse(conexion);
            return false;
        }

        desconectarse(conexion);
        return true; //Si no ocurre ninguna excepción, devuelve true


    }

    /**
     * Este método se utiliza para listar todos los jugadores en la base de datos.
     *
     * @return Una lista observable de jugadores.
     */

    public ObservableList<Jugador> listarJugadores(){
        conectarse();
        collection = db.getCollection("jugadores");
        ObservableList<Jugador> obs = FXCollections.observableArrayList();
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                /*
                 * Explicación de cursor:
                 * Document: es una clase de la biblioteca MongoDB Java que representa un documento BSON, que es el formato de datos utilizado por MongoDB.
                 * El cursor es un objeto que nos permite recorrer los documentos de la colección uno a uno.
                 * El método next() devuelve el siguiente documento de la colección.
                 */
                doc = cursor.next();
                Jugador jugador = new Jugador();
                jugador.setId(doc.getInteger("id")); //Obtenemos el valor del campo id del documento
                jugador.setNombre(doc.getString("nombre")); //Obtenemos el valor del campo nombre del documento
                jugador.setProcedencia(doc.getString("procedencia"));
                jugador.setAltura(doc.getInteger("altura"));
                jugador.setPeso(doc.getInteger("peso"));
                jugador.setPosicion(doc.getString("posicion"));
                obs.add(jugador); //Añadimos el objeto jugador a la lista observable
            }
        } finally {
            cursor.close();
        }

        return obs;
    }

    /**
     * Este método se utiliza para modificar un jugador en la base de datos.
     * @param jugador El jugador a modificar.
     */
    public void modificarJugador(Jugador jugador){
        conectarse();
        collection = db.getCollection("jugadores");
            doc = new Document("id", jugador.getId())
                .append("nombre", jugador.getNombre())
                .append("procedencia", jugador.getProcedencia())
                .append("altura", jugador.getAltura())
                .append("peso", jugador.getPeso())
                .append("posicion", jugador.getPosicion());
        /*
        Explicación: Esta línea actualiza un documento en la colección.
        El primer argumento para updateOne es un filtro que especifica qué documento(s) actualizar -
        en este caso, cualquier documento cuyo campo “id” coincida con jugador.getId().
        El segundo argumento es una operación de actualización que especifica cómo actualizar
        el documento - en este caso, se utiliza la operación $set para reemplazar los campos del
        documento existente con los campos del documento doc.
                                            |
                                            V
        */
        collection.updateOne(new Document("id", jugador.getId()), new Document("$set", doc));
        desconectarse(conexion);
    }

    /**
     * Este método se utiliza para eliminar un jugador de la base de datos.
     * @param jugador El jugador a eliminar.
     */
    public void eliminarJugador(Jugador jugador){
        conectarse();
        collection = db.getCollection("jugadores");
        collection.deleteOne(new Document("id", jugador.getId()));
        desconectarse(conexion);


    }

    /**
     * Este método se utiliza para buscar jugadores en la base de datos por nombre, procedencia y posición.
     * @param nombre El nombre del jugador a buscar.
     * @param procedencia La procedencia del jugador a buscar.
     * @param posicion La posicion del jugador a buscar.
     * @return Una lista observable de jugadores.
     */
    public ObservableList<Jugador> buscarJugadores(String nombre , String procedencia, String posicion){
        conectarse();
        collection = db.getCollection("jugadores");
        ObservableList<Jugador> obs = FXCollections.observableArrayList();
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {

                doc = cursor.next();
                Jugador jugador = new Jugador();
                jugador.setId(doc.getInteger("id"));
                jugador.setNombre(doc.getString("nombre"));
                jugador.setProcedencia(doc.getString("procedencia"));
                jugador.setAltura(doc.getInteger("altura"));
                jugador.setPeso(doc.getInteger("peso"));
                jugador.setPosicion(doc.getString("posicion"));
                if (jugador.getNombre().equals(nombre) || jugador.getProcedencia().equals(procedencia) || jugador.getPosicion().equals(posicion)){
                    obs.add(jugador);
                }

            }
        } finally {
            cursor.close();
        }

        return obs;
    }

    /**
     * El método connect() se utiliza para conectarse a la base de datos.
     */
    /*
     * La clase MongoDB nos ofrece el método getDatabase() que nos permite seleccionar la base de datos
     * con la que queremos trabajar. Si la base de datos no existe, se crea.
     */
    public void conectarse(){

        conexion= ConexionBBDD.conectarMongoDB();
        if (conexion != null) {
            db=conexion.getDatabase("nba");
        }
    }

    /**
     * El método disconnect() se utiliza para desconectarse de la base de datos.
     * @param conexion La conexión a la base de datos.
     */
    public void desconectarse(MongoClient conexion){

        this.conexion.close();
    }
}
