/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package one.pass;
import java.io.IOException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author sammy
 */

public class DataBase extends SerializableSett {
    //Database map
    
    Map<String, Id> database;
    Settings settings;

DataBase(){
    database=new HashMap<>();
}

//Storage into the database
public void postId(String id, String username, String pwd, SecretKey k) throws Exception{
    Id i_d = new Id(id, username, pwd, k);
    
    database.put(i_d .id, i_d );
}

public Id getId(String id){
    return database.get(id);
}


static DataBase load(Path path){
    
    Object obj = SerializableSett.load(path);
    
    return (DataBase) obj;
}

@Override
public String toString(){
String string_s="";
if (database == null)
    return "null database";
for(Id i_d: database.values())
    string_s=string_s+i_d;
return string_s;
}



public class Id extends SerializableSett{

    String id;
    String username;
    byte[] pwd;
    

    Id(String id, String username, String pwd, SecretKey k) throws Exception{
        this.id=id;
        this.username=username;
        this.pwd=MyUtils.encode(pwd, k);
        }

    
    @Override
    public String toString(){
        
        return this.username+this.id+Arrays.toString(this.pwd);
    }
    
 

   
}
}
