package view;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;


public class DesKey {
/*	
	KeyGenerator keyGen = KeyGenerator.getInstance("DES");
	Key key = keyGen.generateKey();

	byte[] keyAsByte = key.getEncoded();
	FileOutputStream fOS = new FileOutputStream("Key.txt");
	fOS.write(keyAsByte);
	fOS.close();
	
	System.out.println("Generated : " + keyAsByte);
	
	//uitlezen key Decrypteren 
	FileInputStream fIS = new FileInputStream("Key.txt");
	byte[] encKey = new byte[fIS.available()];
	fIS.read(encKey);
	fIS.close();
	Key keyFromFile = new SecretKeySpec(encKey, "DES");*/
	
	private Key key;
	//private byte[] keyAsByte;
	private FileOutputStream fOS;
	private String file;
	
	public DesKey() throws Exception{
		generateKey();
	}
	
	public void generateKey() throws Exception {
		KeyGenerator keyGen = KeyGenerator.getInstance("DES");
		keyGen.init(56);
		key = keyGen.generateKey();
		byte[] keyAsByte = key.getEncoded();
		saveKey(keyAsByte, "testKey.txt");
	}
	
	public void saveKey(byte[] b, String fileName) throws Exception{
		fOS = new FileOutputStream(fileName);
		fOS.write(b);
		fOS.close();
	}
	
	
	public void setFile(String fileName){
		
		this.file = fileName;
	}
	
	public static void main (String[] args) throws Exception{
		new DesKey();
	}

	
}
