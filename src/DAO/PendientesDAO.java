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
        Actividades Estado_Actividad = new Actividades();
        Estado_Actividad.setEstado("pendiente");
        BasicDBObject documento = new BasicDBObject();
        documento.put("nombre", actividad.getNombre_Tarea());
        documento.put("estado", Estado_Actividad.getEstado());
        collection.insert(documento);
    }

    @Override
    public ArrayList<Actividades> consultar() {
        ArrayList<Actividades> ListaActividad = new ArrayList<>();
        DBCursor cursor = collection.find();
        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            ListaActividad.add(
                new Actividades((String) obj.get("nombre"), (String) obj.get("estado"))
            );
        }
        return ListaActividad;
    }
    
    @Override
    public void modificar(Actividades actividades) {
        DBObject resultado = collection.findOne(new BasicDBObject("nombre", actividades.getNombre_Tarea()));
        if (resultado != null) {
            BasicDBObject estadoNuevo = new BasicDBObject("estado", actividades.getEstado());
            BasicDBObject operacion = new BasicDBObject("$set", estadoNuevo);
            collection.update(resultado, operacion);
        }
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
