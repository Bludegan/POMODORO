/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import ObjectosNegocio.Actividades;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author blude
 */
public class PendientesDAO extends BaseDAO<Actividades> {

    //Clase PendientesDAO
    DB database;
    DBCollection collection;

    public PendientesDAO() {
      this.crearConexion();
    }

    @Override
    public void agregar(Actividades actividad) {
        BasicDBObject documento = new BasicDBObject();
        documento.put("nombre", actividad.getNombre_Tarea());
        collection.insert(documento);
    }

    @Override
    public ArrayList<Actividades> consultar(){
        ArrayList<Actividades> ListaActividad = new ArrayList<>();
        DBCursor cursor = collection.find();
        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            ListaActividad.add(
                    new Actividades((String) obj.get("nombre"))
            );
        }
        return ListaActividad;
    }

    @Override
    public void crearConexion() {
           MongoClient mongo = null;
        try {
            mongo = new MongoClient("localhost", 27017);
            System.out.println("Connected to the database successfully");
            database = mongo.getDB("Pomodoro");
            collection = database.getCollection("pedientes");
        } catch (MongoException ex) {
            JOptionPane.showMessageDialog(null, "Error en la conexion" + ex.toString());

        }

    }
}
