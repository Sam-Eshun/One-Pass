package one.pass;


import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.KeyPair;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.security.Signature;
import java.security.SignatureException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;


public class Settings extends SerializableSett {
   
    SignInSettings d;
    byte[] v;
    
 
          
        Settings(String N_db, byte[] salt, int it_counter) throws Exception{
       
        d=new SignInSettings();
        d.salt=salt;
        d.it_counter=it_counter;
    }
        


    
public void set_v(String N_db, SecretKey MasterKey) throws Exception{
    
    Signature s = Signature.getInstance("DSA");
   
    KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
    
    KeyPair dsakp=kpg.genKeyPair();
    
    d.PublicKey = dsakp.getPublic();
    
    s.initSign(dsakp.getPrivate());
    
    Path p = Paths.get(N_db+"_sett.ser");
    
    d.save(p);
    
    byte[] dhpkb=Files.readAllBytes(p);
    
    s.update(dhpkb);
    byte[] dhsb=s.sign();
    
    // Performing the hash using PBKDF2WithHmacSHA256
    
    
    
    v=MyUtils.Sign_Encoding(dhsb, MasterKey);
    }
    

public boolean ret_V(String N_db, SecretKey MasterKey) throws Exception{
    
    // Decoding to enable key verification using PBKDF2WithHmacSHA256
    
    byte[] sign = MyUtils.Sign_Decoding(v, MasterKey);
   
       
    // getting the pair key to sign the generated public key with the private key
    Signature s = Signature.getInstance("DSA");
    
    s.initVerify(d.PublicKey);
    
    Path p = Paths.get(N_db+"_sett.ser");
    
    byte[] dhpkb=Files.readAllBytes(p);
    
    s.update(dhpkb);
    
    return s.verify(sign);
    }

   

@Override
    public String toString(){
        String string_s;
        
       string_s= Arrays.toString(d.salt);
       
        string_s=string_s+d.it_counter;
        

        return string_s;
    
    }
    
    
    
   
}


