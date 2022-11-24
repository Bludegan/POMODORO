/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;

/**
 *
 * @author blude
 */
public abstract class BaseDAO<Actividad> {

    public BaseDAO() {}
    public abstract void agregar(Actividad actividad);
    public abstract ArrayList<Actividad> consultar();
    public abstract void crearConexion();
    public abstract void modificar(Actividad actividad);
    public abstract void Eliminar(Actividad actividad);

}
