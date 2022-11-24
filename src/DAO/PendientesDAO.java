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
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import org.bson.types.ObjectId;

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
        documento.put("estado", actividad.getEstado());
        documento.put("fechaterminacion", actividad.getFechaterminacion());
        collection.insert(documento);
    }

    @Override
    public ArrayList<Actividades> consultar() {
        ArrayList<Actividades> ListaActividad = new ArrayList<>();
        DBCursor cursor = collection.find();
        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            ListaActividad.add(
                    new Actividades((ObjectId) obj.get("_id"), (String) obj.get("nombre"), (String) obj.get("estado"), (Date) obj.get("fechaterminacion"))
            );
        }
        return ListaActividad;
    }

    @Override
    public void modificar(Actividades actividades) {
        DBObject resultado = collection.findOne(new BasicDBObject("_id", actividades.id));
        if (resultado != null) {
            BasicDBObject nuevosCampos = new BasicDBObject(); // "estado", actividades.getEstado()
            nuevosCampos.append("nombre", actividades.getNombre_Tarea());
            nuevosCampos.append("estado", actividades.getEstado());
            nuevosCampos.append("fechaterminacion", actividades.getFechaterminacion());
            BasicDBObject operacion = new BasicDBObject("$set", nuevosCampos);
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

    @Override
    public void Eliminar(Actividades actividad) {
        DBObject buscar = (DBObject) collection.findOne(new BasicDBObject("nombre", actividad.getNombre_Tarea()));
        if (buscar != null) {
            collection.remove(buscar);
        }
    }
}
