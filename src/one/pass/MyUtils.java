/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package one.pass;
import java.security.spec.InvalidKeySpecException;
import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;
import java.security.SignatureException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 *
 * @author sammy
 */



public class MyUtils extends SerializableSett {
    
private static final Random RND= new Random();
    public static SecretKey KDF(String Mpwd, byte[] salt, int it_counter, String KeyDF) 
           
            throws NoSuchAlgorithmException, InvalidKeySpecException{
        
    //Generate a key from a given password and a given salt
    
    SecretKeyFactory keyFac = SecretKeyFactory.getInstance(KeyDF);
    
    char [] pwd = Mpwd.toCharArray();
    
    KeySpec pbeKeySpec = new PBEKeySpec(pwd, salt, it_counter, 128);
    
    SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
    
    SecretKey aeskey = new SecretKeySpec(pbeKey.getEncoded(), "AES");
    
    return aeskey;
    }
    

    public static byte[] encode(String string_s, SecretKey aesKey) throws NoSuchAlgorithmException
            , InvalidKeyException, SignatureException, NoSuchPaddingException, IllegalBlockSizeException, 
            BadPaddingException, FileNotFoundException, IOException {
    /* creating an AES cipher in Electronic Codebook mode, with PKCS5-style padding 
        The standard algorithm name for AES is "AES", 
        the standard name for the Electronic Codebook mode is "ECB",
        and the standard name for PKCS5-style padding is "PKCS5Padding"*/    
    
//  Creating the aesCipher 
        Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        
        // Initialize the cipher for encryption using the generated aesKey
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
        
        // The cleartext
        byte[] clearText = string_s.getBytes("ISO-8859-1");
        
        // Encrypting the cleartext
        byte[] cipherText = aesCipher.doFinal(clearText);
        
        return cipherText;
    }
    

    
      static byte[] Sign_Encoding(byte[] signer, SecretKey aesKey) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, 
            BadPaddingException {
         /* creating an AES cipher in Electronic Codebook mode, with PKCS5-style padding 
        The standard algorithm name for AES is "AES", 
        the standard name for the Electronic Codebook mode is "ECB",
        and the standard name for PKCS5-style padding is "PKCS5Padding"*/    
    
//  Creating the aesCipher 

        Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        
        // Initialize the cipher for encryption using the generated aesKey
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
        
        // Encrypting the signing
        byte[] cipherText = aesCipher.doFinal(signer);
        return cipherText;
        
    }
    
    
    public static String decode(byte[] cipherText, SecretKey aesKey)throws NoSuchAlgorithmException,
            InvalidKeyException, SignatureException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException, FileNotFoundException, IOException {
        
         /* creating an AES cipher in Electronic Codebook mode, with PKCS5-style padding 
        The standard algorithm name for AES is "AES", 
        the standard name for the Electronic Codebook mode is "ECB",
        and the standard name for PKCS5-style padding is "PKCS5Padding"*/    
    
//  Creating the aesCipher 
        Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        
        // Initializing the same cipher for decryption using the generated aesKey
        aesCipher.init(Cipher.DECRYPT_MODE, aesKey);
        
       // Decrypting the ciphertext
        byte[] decodedText = aesCipher.doFinal(cipherText);
        
        //Decoded text
        String decoded = new String(decodedText, "ISO-8859-1");        
        
        System.out.println("Decoded: " + Arrays.toString(decodedText));
        System.out.println("Ciphertext: " + Arrays.toString(cipherText));
        
       return decoded;
        
    }


  
    

    static byte[] Sign_Decoding(byte[] encSign, SecretKey aesKey) throws NoSuchAlgorithmException, 
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, 
            BadPaddingException{
    
        
         
        Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        
        // Initializing the same cipher for decryption using the generated aesKey
        aesCipher.init(Cipher.DECRYPT_MODE, aesKey);
        
         // Decrypting the ciphertext
        byte[] decipheredSign = aesCipher.doFinal(encSign);
    

       return decipheredSign;
        
    }
   
         static byte[] ret_Salt(int len){
        byte[] salt = new byte[len];
        RND.nextBytes(salt);
        return salt;    
    
    }   
    
         public void denial(JFrame f, String msg) {
        JOptionPane.showMessageDialog(f,
                msg,
                "Error Message",
                JOptionPane.ERROR_MESSAGE);
    }

    public void success(JFrame f, String msg) {
        JOptionPane.showMessageDialog(f,
                msg,
                "Confirm",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    }
    

    