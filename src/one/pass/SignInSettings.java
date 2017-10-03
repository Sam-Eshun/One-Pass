/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package one.pass;

import javax.crypto.SecretKey;
import java.security.PublicKey;

import java.nio.file.Path;
/**
 *
 * @author sammy
 */

//parametrs to link the database

public class SignInSettings extends SerializableSett{
  
   byte[] salt;
    
    int it_counter;
        
    PublicKey PublicKey;
    
    static SignInSettings load(Path path){
        
    Object obj = SerializableSett.load(path);
    
    return (SignInSettings) obj;
}
}
    