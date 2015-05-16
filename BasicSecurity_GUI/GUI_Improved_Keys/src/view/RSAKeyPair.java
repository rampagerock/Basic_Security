package view;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;


public class RSAKeyPair {
	
	private String user;
	private int users;
			/*
				
			//RSA KeyPair
				KeyPairGenerator keyPGen = KeyPairGenerator.getInstance("RSA");
				keyPGen.initialize(512);
				KeyPair keyP = keyPGen.genKeyPair();
				Key pubKey = keyP.getPublic();
				Key privKey = keyP.getPrivate();
				
				
				KeyFactory fact = KeyFactory.getInstance("RSA");
				RSAPublicKeySpec pub = fact.getKeySpec(keyP.getPublic(),
				  RSAPublicKeySpec.class);
				RSAPrivateKeySpec priv = fact.getKeySpec(keyP.getPrivate(),
				  RSAPrivateKeySpec.class);
			
				//wegschrijven RSA keys naar File (public.key en private.key)
				saveToFile("public.txt", pub.getModulus(),
				  pub.getPublicExponent());
				saveToFile("private.txt", priv.getModulus(),
				  priv.getPrivateExponent());
			}
			public static void saveToFile(String fileName,
					  BigInteger mod, BigInteger exp) throws IOException {
					  ObjectOutputStream oout = new ObjectOutputStream(
					    new BufferedOutputStream(new FileOutputStream(fileName)));
					  try {
					    oout.writeObject(mod);
					    oout.writeObject(exp);
					  } catch (Exception e) {
					    throw new IOException("Unexpected error", e);
					  } finally {
					    oout.close();
					  }
					}*/
	
	private Key key;
	private ObjectOutputStream oOUT;
	
	public RSAKeyPair(int users) throws Exception{
		this.users = users;
		generateKeyPair(users);
	}
	
	public void generateKeyPair(int aantal) throws Exception{
		
		for(int i = 0; i<users; i++){
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(1024);
		KeyPair keyP = keyGen.genKeyPair();
		Key pubKey = keyP.getPublic();
		Key privKey = keyP.getPrivate();
		
		KeyFactory fact = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec pub = fact.getKeySpec(keyP.getPublic(), RSAPublicKeySpec.class);
		RSAPrivateKeySpec priv = fact.getKeySpec(keyP.getPrivate(), RSAPrivateKeySpec.class);
		saveKeys(user + i + "_PubKey.txt", pub.getModulus(), pub.getPublicExponent());
		saveKeys(user + i +"_PrivKey.txt", priv.getModulus(), priv.getPrivateExponent());
		}
		
		System.out.println("Keys have been created");
	}
	
	public void saveKeys(String fileName,
			  BigInteger mod, BigInteger exp) throws FileNotFoundException, IOException{
		ObjectOutputStream oout = new ObjectOutputStream(
			    new BufferedOutputStream(new FileOutputStream(fileName)));
			  try {
			    oout.writeObject(mod);
			    oout.writeObject(exp);
			  } catch (Exception e) {
			    throw new IOException("Unexpected error", e);
			  } finally {
			    oout.close();
			  }
	}
	
	public void setUser(String name){
		this.user = name;
	}
	
/*	public static void main(String[] args) throws Exception{
		new  RSAKeyPair(2);
	}*/
	
}

