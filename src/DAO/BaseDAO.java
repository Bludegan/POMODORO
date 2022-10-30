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
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
/**
 *
 * @author blude
 */
public abstract class BaseDAO<Actividad> {

    public BaseDAO() {
        
        
    }
    
//    private  final String SERVER= "localhost";
//    private  final int PORT= 27017;
//    private  final String DB= "Pomodoro";
//         protected MongoDatabase getDatabase(){
//         try {
//            CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
//            CodecRegistry codecRegistry = 
//                    fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
//                    pojoCodecRegistry);
//            ConnectionString cadenaConexion = new ConnectionString("mongodb://"+SERVER+"/"+PORT);
//            MongoClientSettings clientSettings = MongoClientSettings.builder()
//                    .applyConnectionString(cadenaConexion)
//                    .codecRegistry(codecRegistry)
//                    .build();
//            MongoClient clienteMongo = MongoClients.create(clientSettings);
//            MongoDatabase baseDatos = clienteMongo.getDatabase(DB);
//            return  baseDatos;
//        } catch (Exception ex) {
//            System.err.println(ex);
//            throw ex;
//        }
//    }
    
     public abstract void agregar(Actividad actividad);
     public abstract ArrayList<Actividad> consultar();
     public abstract void crearConexion();

}
