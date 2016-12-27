package com.template.project.utils;

import android.util.Base64;
import android.util.Log;

import org.apache.commons.lang3.RandomStringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AESBase64 {

	
	public static String encrypt(String key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
//            System.out.println("encrypted string: "
//                    + Base64.encodeBase64String(encrypted));

            return Base64.encodeToString(encrypted, Base64.NO_WRAP);
        } catch (Exception e) {
        	Log.e("AESBase64","encrypt", e );
        }

        return null;
    }

    public static String decrypt(String key, String initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decode(encrypted,Base64.NO_WRAP));

            return new String(original);
        } catch (Exception e) {
            Log.e("AESBase64","encrypt", e );
        }

        return null;
    }
    
    public static String generateIVKey(){
    	return RandomStringUtils.randomAlphanumeric(16);
    }

    public static void main(String[] args) {
    	String initVector = "bc5yq773yRo6HlpV";
    	String res = "6TxGs9?Vi%LJ!I1y";
//    	|<<|5TtDxzS36xnoIUDo2GQNFg==|>>|
    	
    	System.out.println("rancomIv: " + res.substring(16));
  
    	
        String key = "6TxGs9?Vi%LJ!I1y"; // 128 bit key
//        String initVector = "Random2n&tVector"; // 16 bytes IV
        
        String encryptedDate = encrypt(key, initVector, "A0113|^||^|2016-05-03 18:51:33.838");
        System.out.println("Encrypt: " +  encryptedDate);
     
        System.out.println("Decrypt: " +  decrypt(key, initVector, "XzPFIwoPhfLx5zuYZUezCBY6G0k3t0t+QZxuHLSpefMl2Ci1XPvwlXTP02XfEGMm"));

//        System.out.println(decrypt(key, initVector,
//                encrypt(key, initVector, "Hello World")));
    }
}
