package view;

import java.nio.file.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Formatter;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.swing.JFileChooser;

import model.Hasher;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class GUIController {

	private MainApplication mainApp;

	@FXML
	private Button submit;
	@FXML
	private Button changeEnc;
	@FXML
	private Button changeFile2;
	@FXML
	private Button changeFile3;
	@FXML
	private Button changePriv;
	@FXML
	private Button changePub;
	@FXML
	private Button changeFile;
	@FXML
	private TextField textEnc;
	@FXML
	private TextField textFile2;
	@FXML
	private TextField textFile3;
	@FXML
	private TextField textPriv;
	@FXML
	private TextField textPub;
	@FXML
	private TextField textFile;
	@FXML
	private TextField nameField;
	@FXML
	private TextField nameFieldCor;
	@FXML
	private Button genBtn;
	@FXML
	private TextArea encFileArea;

	@FXML
	private RadioButton encRb;
	@FXML
	private RadioButton decRb;

	private ToggleGroup rbGroup;

	private boolean encrypt;

	public GUIController() {

	}

	public void initialize() {
		rbGroup = new ToggleGroup();
		encRb.setToggleGroup(rbGroup);
		decRb.setToggleGroup(rbGroup);
		rbGroup.selectedToggleProperty().addListener(new OnToggleHandler());
		SubmitHandler handler = new SubmitHandler();
		submit.setOnAction(handler);

		changeEnc.setOnAction(this::openFile);
		changeFile2.setOnAction(this::openFile);
		changeFile3.setOnAction(this::openFile);
		changePriv.setOnAction(this::openFile);
		changePub.setOnAction(this::openFile);
		changeFile.setOnAction(this::openFile);
		genBtn.setOnAction(this::generate);

		welcomeText();
	}

	private void welcomeText() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					"Tutorial.txt"));
			String line;
			while ((line = in.readLine()) != null) {
				encFileArea.appendText(line + "\n");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class OnToggleHandler implements ChangeListener<Toggle> {

		@Override
		public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
			RadioButton rb = (RadioButton) t1.getToggleGroup().getSelectedToggle();
			switch (rb.getId()) {
				case "encRb":
					encrypt = true;
					break;
				case "decRb":
					encrypt = false;
					break;
			}

			if (encrypt) {
				disableDecrypt();
				enableEncrypt();
			} else {
				disableEncrypt();
				enableDecrypt();
			}

		}

	}

	public void generate(ActionEvent Event) {
		String n1 = nameField.getText();
		String n2 = nameFieldCor.getText();
		model.RSA.generateKey(n1);
		model.RSA.generateKey(n2);
		// model.DES.GenerateDESKey();
	}

	public void disableEncrypt() {
		changeFile.setDisable(true);
		textFile.setDisable(true);
	}

	public void disableDecrypt() {
		changeEnc.setDisable(true);
		changeFile2.setDisable(true);
		changeFile3.setDisable(true);
		changePriv.setDisable(true);
		changePub.setDisable(true);

		textEnc.setDisable(true);
		textFile2.setDisable(true);
		textFile3.setDisable(true);
		textPriv.setDisable(true);
		textPub.setDisable(true);
	}

	public void enableEncrypt() {
		changeFile.setDisable(false);
		textFile.setDisable(false);
	}

	public void enableDecrypt() {
		changeEnc.setDisable(false);
		changeFile2.setDisable(false);
		changeFile3.setDisable(false);
		changePriv.setDisable(false);
		changePub.setDisable(false);

		textEnc.setDisable(false);
		textFile2.setDisable(false);
		textFile3.setDisable(false);
		textPriv.setDisable(false);
		textPub.setDisable(false);
	}

	private void openFile(ActionEvent event) {
		Button name = (Button) event.getSource();
		JFileChooser chooser = new JFileChooser();
		int returnValue = chooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			switch (name.getId()) {
			case "changeEnc": // Geencrypteerde file voor decryptie
				textEnc.setText(selectedFile.toString());
				break;
			case "changeFile2": // geencrypteerde des key
				textFile2.setText(selectedFile.toString());
				break;
			case "changeFile3": // geencrypteerde hash van originele boodschap
				textFile3.setText(selectedFile.toString());
				break;
			case "changePriv": // private key
				textPriv.setText(selectedFile.toString());
				break;
			case "changePub": // public key
				textPub.setText(selectedFile.toString());
				break;
			case "changeFile": // Encrypteren boodschap
				textFile.setText(selectedFile.toString());
				break;
			default:
				System.out.println("Something went wrong!");
			}
			/*
			 * encFileArea.clear(); try { BufferedReader in = new
			 * BufferedReader(new FileReader( selectedFile)); String line =
			 * null; while ((line = in.readLine()) != null) {
			 * encFileArea.appendText(line + "\n"); } } catch
			 * (FileNotFoundException e2) { 
			 * e2.printStackTrace(); } catch (IOException e1) { 
			 */

		}

	}

	class SubmitHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {

			ObjectInputStream inputStream;
			byte[] cipherText = null;
			String desKeyString = null;
			String plainText = null;
			try {
				if (encrypt) {
					/*
					 * DES Encryption van Message
					 * get file1.txt
					 */
					File originalFile = new File(textFile.getText());
					System.out.println("250> Encrypt file: " + originalFile.getName());
					//read file content				
					{//Test code
						Scanner fileReader = new Scanner(originalFile);
						String fileContentString = "";
						while(fileReader.hasNext()){
							 fileContentString += fileReader.nextLine();						
						}
						System.out.println("259> File content: " + fileContentString);
					}
					//genereate des key
					model.DES.GenerateDESKey();
					
					//encrypt content using des key
					byte[] encFile = model.DES.EncryptWithDES(originalFile.toString());
					
					//write encrypted content to File_1.txt
					FileOutputStream fileOutputStream = new FileOutputStream("File_1.txt");
					fileOutputStream.write(encFile);
					fileOutputStream.close();				
					
					/*
					 * Encrypt DES key using Public B
					 * get file2.txt
					 */
					Scanner sc = new Scanner(new FileInputStream("DESKey.key"));
					desKeyString = sc.next();
					
					System.out.println("290> desKey(Object): " + desKeyString);

					inputStream = null;

					//Get public key from file
					inputStream = new ObjectInputStream(new FileInputStream("B_publicKey.key"));
					final PublicKey publicKey = (PublicKey) inputStream.readObject();
					
					//Encrypt using public key
					Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
					cipher.init(Cipher.ENCRYPT_MODE, publicKey);
					//String encKey = Base64.getEncoder().encodeToString(cipher.doFinal(desKeyString.getBytes()));
					byte[] encKey = cipher.doFinal(desKeyString.getBytes());

					System.out.println("304>: encKey first byte " + encKey[0]);
					
					//Save encrypted des key to file
					FileOutputStream out = new FileOutputStream("File_2.txt"); 	
					out.write(encKey);
					out.close();
					
					/*
					 * Hash van file (oorspronkelijke boodschap)
					 */
					Scanner fileReader = new Scanner(originalFile);
					String fileContentString = "";
					while(fileReader.hasNext()){
						 fileContentString += fileReader.nextLine();						
					}
					String hashString = Hasher.getHash(fileContentString);
					System.out.println(fileContentString);
					System.out.println("316> Hash1: " + hashString);
					
					/*
					 * Encrypt hash using privateA
					 * get file3.txt
					 */
					//Get private key from file
					inputStream = new ObjectInputStream(new FileInputStream("A_privateKey.key"));
					final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
					
					//Encrypt using private key
					cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
					cipher.init(Cipher.ENCRYPT_MODE, privateKey);
					byte[] encHash = cipher.doFinal(desKeyString.getBytes());
					System.out.println("330> encHash:" + encHash);

					//save hash to file
					out = new FileOutputStream("File_3.txt"); 	
					out.write(encHash);
					out.close();

				}

				if (!encrypt) {
					/*
					 * Decrypt File2 using Private B 
					 * get DES Key
					 */
					
					//get private key B
					inputStream = new ObjectInputStream(new FileInputStream("B_privateKey.key"));
					final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
					
					//get file2.txt
					File file = new File("File_2.txt");
					byte[] encData = new byte[(int) file.length()];
					try {
					    new FileInputStream(file).read(encData);
					} catch (Exception e) {
					    e.printStackTrace();
					}
					System.out.println("357>: encData first byte " + encData[0]);
					
					//decode using private key
					Cipher dipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
					dipher.init(Cipher.DECRYPT_MODE, privateKey);

					byte[] decrypted = dipher.doFinal(encData);
					System.out.println("365> DesKey first 2 bytes:" + decrypted[0] + " " + decrypted[1]);

					/*
					 * Decrypt Message (file_1.txt) using decrypted DES key
					 * get message
					 */
					file = new File("File_1.txt");
					byte[] encTxtBytes = new byte[(int) file.length()];
					try {
					    new FileInputStream(file).read(encTxtBytes);
					} catch (Exception e) {
					    e.printStackTrace();
					}
					System.out.println("373> encrypted text: " + encTxtBytes);
					
					byte[] decText = model.DES.DecryptWithDES(encTxtBytes, "DESKey.key");
					
					//write dectext to file
					FileOutputStream out = new FileOutputStream("File.txt"); 	
					out.write(decText);
					out.close();
					
					/*
					 * 
					 * Berekenen Hash van boodschap
					 */
					FileInputStream ips = new FileInputStream("File.txt");
					Scanner fileReader = new Scanner(ips);
					String fileContentString = "";
					while(fileReader.hasNext()){
						 fileContentString += fileReader.nextLine();						
					}
					
					
					String hash = Hasher.getHash(fileContentString);
					System.out.println(fileContentString);
					System.out.println("394> 2nd hash: " + hash);
					
					/*
					 * Decryptie hash file(file 3) met public key gebruiker
					 * 
					 * File_3
					 */
					{
						file = new File("File_3.txt");
						byte[] decrHashBytes = new byte[(int) file.length()];
						try {
						    new FileInputStream(file).read(decrHashBytes);
						} catch (Exception e) {
						    e.printStackTrace();
						}
						System.out.println("407> decrypted hash: " + decrHashBytes);
						
						//get private key B
						inputStream = new ObjectInputStream(new FileInputStream("B_publicKey.key"));
						final PublicKey publicKeyB = (PublicKey) inputStream.readObject();
						System.out.println(publicKeyB);
						//magic happens
						//byte[] decHash = model.RSA.decrypt(decrHashBytes, publicKeyB);
						byte[] decHash = null;
						{
							try {
								// get an RSA cipher object and print the provider
								final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

								// decrypt the text using the public key
								cipher.init(Cipher.DECRYPT_MODE, publicKeyB);
								decHash = cipher.doFinal(decrHashBytes);

							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
						new String(decHash);
						//byte[] decHash = model.DES.DecryptWithDES(decrHashBytes, "B_publicKey.key");//publickeyvnB
						
						//write dectext to file
						FileOutputStream out2 = new FileOutputStream("HashAfter.txt"); 	
						out2.write(decHash);
						out2.close();
					}

					/*
					 * Vergelijken verkregen hash met zelf berekende hash
					 */

				}

				/*
				 * // final String originalText = "Text to be encrypted ";
				 * Scanner sc = new Scanner(new FileInputStream("Key.txt"));
				 * String text = sc.next(); ObjectInputStream inputStream =
				 * null;
				 * 
				 * // Encrypt the string using the public key inputStream = new
				 * ObjectInputStream(new
				 * FileInputStream(model.Encrypt.PUBLIC_KEY_FILE)); final
				 * PublicKey publicKey = (PublicKey) inputStream.readObject();
				 * final byte[] cipherText = model.Encrypt.encrypt(text,
				 * publicKey);
				 */

				/*
				 * // Decrypt the cipher text using the private key. inputStream
				 * = new ObjectInputStream(new FileInputStream( model. Encrypt .
				 * PRIVATE_KEY_FILE "")); final PrivateKey privateKey =
				 * (PrivateKey) inputStream .readObject(); final String
				 * plainText = model.Encrypt.decrypt(cipherText, privateKey);
				 */
				// Printing the Original, Encrypted and Decrypted Text
				/*
				 * System.out.println("Original: " + text);
				 * System.out.println("Encrypted: " + cipherText.toString());
				 * System.out.println("Decrypted: " + plainText);
				 */

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setMainApp(MainApplication mainApp) {
		this.mainApp = mainApp;
	}

}
