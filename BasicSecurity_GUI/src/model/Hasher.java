package model;

import java.security.MessageDigest;

public class Hasher {
	public static String getHash(String text) throws Exception
    { 
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes());
 
        byte byteData[] = md.digest();
 
        //convert the byte to hex format
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        
        return sb.toString();
    }
}