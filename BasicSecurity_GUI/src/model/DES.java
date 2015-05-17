package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class DES {
	public static void GenerateDESKey() {
		try {
			KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
			SecretKey myDesKey = keygenerator.generateKey();
			File KeyFile = new File("DESKey.key");

			// Create file to store DES key
			if (KeyFile.getParentFile() != null) {
				KeyFile.getParentFile().mkdirs();
			}
			KeyFile.createNewFile();

			// Saving the key in a file
			ObjectOutputStream KeyOS = new ObjectOutputStream(
					new FileOutputStream(KeyFile));
			KeyOS.writeObject(myDesKey);
			KeyOS.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static byte[] EncryptWithDES(String inputMessageTextFile) {
		try {
			// Create the cipher
			Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

			// Initialize the cipher for encryption
			ObjectInputStream inputStream = null;
			inputStream = new ObjectInputStream(new FileInputStream(
					"DESKey.key"));
			desCipher.init(Cipher.ENCRYPT_MODE, (Key) inputStream.readObject());
			inputStream.close();

			// sensitive information
			byte[] text;

			Scanner sc = new Scanner(new FileInputStream(inputMessageTextFile));
			String originalText = "";
			String tempText = null;
			while (sc.hasNext()) {
				tempText = sc.nextLine();
				originalText += tempText + "\n";
			}
			sc.close();

			text = Base64.getEncoder().encode(originalText.getBytes("UTF-8"));
			
			byte[] textEncrypted = desCipher.doFinal(text);
			return textEncrypted;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static String DecryptWithDES(byte[] textEncrypted, String keyPath)
	{byte[] textDecrypted = null;
		try
		{
			// Create the cipher
			Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

			// Initialize the same cipher for decryption
			ObjectInputStream inputStream = null;
			inputStream = new ObjectInputStream(new FileInputStream(
					keyPath));
			desCipher.init(Cipher.DECRYPT_MODE, (Key) inputStream.readObject());
			inputStream.close();

			// Decrypt the text
			 textDecrypted = desCipher.doFinal(textEncrypted);
			 
			 /*byte[] encryptedBytes = cipher.doFinal(plainBytes);
 
     return encryptedBytes.toString();*/

			//System.out.println("Text Decryted : " + new String(textDecrypted));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return Base64.getEncoder().encodeToString(textDecrypted);
	}
}