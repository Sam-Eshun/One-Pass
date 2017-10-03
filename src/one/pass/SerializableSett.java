/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package one.pass;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.io.ObjectOutputStream;
import java.io.Serializable;
/**
 *
 * @author sammy
 */


class SerializableSett implements Serializable {

    //performing the serialization using the parameter path
     
    void save(Path path){
        try {
            ObjectOutputStream ObjOutStr = new ObjectOutputStream(Files.newOutputStream(path, 
                    StandardOpenOption.CREATE));
            
            ObjOutStr .writeObject(this);
            ObjOutStr .close();
        }
        catch (IOException ioe) {
               System.out.println("Deserialization failed:: " + ioe.getMessage());
        }    
}


    //performing the deserialization using the parameter path
    
    static Object load(Path path){
        Object k = null;
        try {
            ObjectInputStream ObjInpStr = new ObjectInputStream(Files.newInputStream(path));
            k = ObjInpStr.readObject(); 
            ObjInpStr.close();
            } 

        catch (ClassNotFoundException ce) {
            System.out.println("Unable to declare Database: " + ce.getMessage());
            } 
        catch (IOException ie) {
            System.out.println("Deserialization failed: " + ie.getMessage());
          }

        return k;
      }
    }
    


    
