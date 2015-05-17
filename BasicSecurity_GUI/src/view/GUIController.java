package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.swing.JFileChooser;

import model.DES;
import model.Hasher;
import model.RSA;

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
		encFileArea.textProperty().addListener(new ChangeListener<Object>(){

			@Override
			public void changed(ObservableValue<? extends Object> observable,
					Object oldValue, Object newValue) {
				encFileArea.setScrollTop(Double.MAX_VALUE);
			}
			
		});
	}

	private void welcomeText() {
		try {
			BufferedReader in = new BufferedReader(new FileReader("Tutorial.txt"));
			String line;
			while ((line = in.readLine()) != null) {
				encFileArea.appendText(line + "\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
		RSA.generateKey(n1);
		RSA.generateKey(n2);
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
				File originalFile = new File(textFile.getText());
				File file1 = new File("File_1.txt");
				File file2 = new File("File_2.txt");
				File file3 = new File("File_3.txt");
				
				if (encrypt) {
					while(textFile.getText().equals("")){
						encFileArea.appendText("\n----------------------------------------------------------------------------------\n");
						encFileArea.appendText("Please provide paths to all the requested files\n");
						return;
					}
					/*
					 * DES Encryption van Message
					 * get file1.txt
					 */	
					
					System.out.println("250> Encrypt file: " + originalFile.getName());
					encFileArea.appendText("\n----------------------------------------------------------------------------------\n");
					encFileArea.appendText("Encrypting " + originalFile.getAbsolutePath() + "\n");
					
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
					DES.GenerateDESKey();
					
					//encrypt content using des key
					byte[] encFile = DES.EncryptWithDES(originalFile.toString());
					
					//write encrypted content to File_1.txt
					
					encFileArea.appendText("Writing encrypted message to " + file1.getAbsolutePath() + "\n");
					FileOutputStream fileOutputStream = new FileOutputStream(file1);
					fileOutputStream.write(encFile);
					fileOutputStream.close();
					
					
					/*
					 * Encrypt DES key using Public B
					 * get file2.txt
					 */
					encFileArea.appendText("Encrypting DES key\n");
					Scanner sc = new Scanner(new FileInputStream("DESKey.key"));
					desKeyString = sc.next();

					inputStream = null;

					//Get public key from file
					inputStream = new ObjectInputStream(new FileInputStream("B_publicKey.key"));
					final PublicKey publicKey = (PublicKey) inputStream.readObject();
					
					//Encrypt using public key
					Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
					cipher.init(Cipher.ENCRYPT_MODE, publicKey);
					byte[] encKey = cipher.doFinal(desKeyString.getBytes());

					System.out.println("304> encrypted des: " + encKey[0] + " " + encKey[1]);
					
					//Save encrypted des key to file
					encFileArea.appendText("Writing encrypted message to " + file2.getAbsolutePath() + "\n");
					FileOutputStream out = new FileOutputStream(file2); 	
					out.write(encKey);
					out.close();
					
					
					/*
					 * Hash van file (oorspronkelijke boodschap)
					 */
					encFileArea.appendText("Hashing original message\n");
					Scanner fileReader = new Scanner(originalFile);
					String fileContentString = "";
					while(fileReader.hasNext()){
						 fileContentString += fileReader.nextLine();
						 encFileArea.appendText("...");
					}
					String hashString = Hasher.getHash(fileContentString);
					System.out.println(fileContentString);
					System.out.println("316> hash A: " + hashString);
					encFileArea.appendText("\nHash: " + hashString + "\n");
					/*
					 * Encrypt hash using privateA
					 * get file3.txt
					 */
					
					//Get private key from file
					encFileArea.appendText("Encrypting Hash using RSA\n");
					inputStream = new ObjectInputStream(new FileInputStream("A_privateKey.key"));
					final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
					
					//Encrypt using private key
					cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
					cipher.init(Cipher.ENCRYPT_MODE, privateKey);
					byte[] encHash = cipher.doFinal(hashString.getBytes());
					System.out.println("330> encrypted Hash A: " + encHash[0] + " " + encHash[1]);
					
					//save hash to file
					encFileArea.appendText("Saving Encrypted hash to " + file3.getAbsolutePath() + "\n");
					out = new FileOutputStream("File_3.txt"); 	
					out.write(encHash);
					out.close();
					
				}

				if (!encrypt) {
					while(textEnc.getText().equals("") || textFile2.getText().equals("") || textFile3.getText().equals("") || textPriv.getText().equals("") || textPub.getText().equals("")){
						encFileArea.appendText("\n----------------------------------------------------------------------------------\n");
						encFileArea.appendText("Please provide paths to all the requested files\n");
						return;
					}
					/*
					 * Decrypt File2 using Private B 
					 * get DES Key
					 */
					File encryptedMessageFile = new File(textEnc.getText());
					File privateKeyFile = new File(textPriv.getText());
					File publicKeyFile = new File(textPub.getText());
					
					encFileArea.appendText("\n----------------------------------------------------------------------------------\n");
					encFileArea.appendText("Decypting " + encryptedMessageFile.getAbsolutePath() + "\n");
					
					//get private key B
					inputStream = new ObjectInputStream(new FileInputStream(privateKeyFile));
					final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
					
					//get file2.txt
					byte[] encData = new byte[(int) file2.length()];
					try {
					    new FileInputStream(file2).read(encData);
					} catch (Exception e) {
					    e.printStackTrace();
					}
					System.out.println("357> encrypted DES B: " + encData[0] + " " + encData[1]);
					encFileArea.appendText("Decypting DES key from " + file2.getAbsolutePath() + "\n");
					//decode using private key
					Cipher dipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
					dipher.init(Cipher.DECRYPT_MODE, privateKey);

					byte[] decrypted = dipher.doFinal(encData);
					System.out.println("365> Decrypted DesKey: " + decrypted[0] + " " + decrypted[1] + " " + decrypted[2]);

					/*
					 * Decrypt Message (file_1.txt) using decrypted DES key
					 * get message
					 */
					encFileArea.appendText("Decypting message using DES key\n");
					byte[] encTxtBytes = new byte[(int) file1.length()];
					try {
					    new FileInputStream(file1).read(encTxtBytes);
					} catch (Exception e) {
					    e.printStackTrace();
					}
					System.out.println("373> encrypted text: " + encTxtBytes[0] + " " + encTxtBytes[1]);
					
					byte[] decText = DES.DecryptWithDES(encTxtBytes, "DESKey.key");
					
					//write dectext to file
					FileOutputStream out = new FileOutputStream("File.txt"); 	
					out.write(decText);
					out.close();
					
					/*
					 * Berekenen Hash van boodschap
					 */
					encFileArea.appendText("Calculating hash from recieved file\n");
					FileInputStream ips = new FileInputStream("File.txt");
					Scanner fileReader = new Scanner(ips);
					String fileContentString = "";
					while(fileReader.hasNext()){
						 fileContentString += fileReader.nextLine();		
						 encFileArea.appendText("...");
					}
					
					String hash = Hasher.getHash(fileContentString);
					System.out.println(fileContentString);
					System.out.println("394> Hash B: " + hash);
					encFileArea.appendText("\nHash: " + hash + "\n");
					
					/*
					 * Decryptie hash file(file 3) met public key gebruikerA
					 * 
					 * File_3
					 */
					encFileArea.appendText("Decrypting recieved hash file " + file3.getAbsolutePath() + "\n");
					{
						//get public key A
						inputStream = new ObjectInputStream(new FileInputStream(publicKeyFile));
						final PublicKey publicKey = (PublicKey) inputStream.readObject();
						
						//get file2.txt
						byte[] encHash = new byte[(int) file3.length()];
						try {
						    new FileInputStream(file3).read(encHash);
						} catch (Exception e) {
						    e.printStackTrace();
						}
						System.out.println("407> encrypted Hash B: " + encHash[0] + " " + encHash[1]);
						
						//decode using private key
						Cipher dipher2 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
						dipher2.init(Cipher.DECRYPT_MODE, publicKey);

						byte[] decHash = dipher2.doFinal(encHash);
						System.out.println("416> Decrypted hash: " + decHash[0] + " " + decHash[1] + " " + decHash[2]);
						
						//write dectext to file
						File hashAfter = new File("HashAfter.txt");
						FileOutputStream out2 = new FileOutputStream(hashAfter); 	
						out2.write(decHash);
						out2.close();
						
					}

					/*
					 * Vergelijken verkregen hash met zelf berekende hash
					 */
					encFileArea.appendText("Checking if recieved hash equals calculated hash\n");
					ips = new FileInputStream("HashAfter.txt");
					fileReader = new Scanner(ips);
					String hashaftercontent = "";
					while(fileReader.hasNext()){
						hashaftercontent += fileReader.nextLine();						
					}
					if(hashaftercontent.equals(hash)){
						encFileArea.appendText("Hashes are equal, file transfer OK.\n");
						encFileArea.appendText("Opening File...\n");
						encFileArea.appendText("*******************************************************************************\n");
						File messageFile = new File("File.txt");
						ips = new FileInputStream(messageFile);
						fileReader = new Scanner(ips);
						while(fileReader.hasNext()){
							encFileArea.appendText(fileReader.nextLine() + "\n");
						}
						encFileArea.appendText("*******************************************************************************\n");
					}else {
						encFileArea.appendText("Hashes are not equal, please request the message again.\n");
						encFileArea.appendText("----------------------------------------------------------------------------------\n");
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setMainApp(MainApplication mainApp) {
		this.mainApp = mainApp;
	}

}
