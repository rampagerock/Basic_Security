package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Formatter;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.swing.JFileChooser;

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
		public void changed(ObservableValue<? extends Toggle> ov, Toggle t,
				Toggle t1) {
			// TODO Auto-generated method stub
			System.out.println("Observable : " + ov);
			System.out.println("Old Value : " + t);
			System.out.println("New Value : " + t1);
			RadioButton rb = (RadioButton) t1.getToggleGroup()
					.getSelectedToggle();
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
		System.out.println(name);
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
			 * (FileNotFoundException e2) { // TODO Auto-generated catch block
			 * e2.printStackTrace(); } catch (IOException e1) { // TODO
			 * Auto-generated catch block e1.printStackTrace(); }
			 */

		}

	}

	class SubmitHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {

			ObjectInputStream inputStream;
			byte[] cipherText = null;
			String text = null;
			String plainText = null;
			try {

				if (encrypt) {

					/*
					 * DES Encryption van file!
					 * 
					 *==> File_1
					 */
					File file = new File(textFile.getText());
					System.out.println(file.getName());
					model.DES.GenerateDESKey();
					byte[] encFile = model.DES.EncryptWithDES(file.toString());
					FileOutputStream fOS = new FileOutputStream("File_1.txt");
					fOS.write(encFile);
					fOS.close();
					/*
					 * RSA encryptie van de DES Key met de public key van
					 * ontvanger
					 * 
					 * File_2
					 */
					Scanner sc = new Scanner(new FileInputStream("DESKey.key"));
					text = sc.next();
					System.out.println(Base64.getEncoder().encodeToString(
							text.getBytes()));
					inputStream = null;

					// Encrypt the string using the public key
					inputStream = new ObjectInputStream(new FileInputStream(
							"public.key"));
					final PublicKey publicKey = (PublicKey) inputStream
							.readObject();
					// cipherText = model.RSA.encrypt(text, publicKey);
					Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
					cipher.init(Cipher.ENCRYPT_MODE, publicKey);
					String encKey = Base64.getEncoder().encodeToString(
							cipher.doFinal(text.getBytes()));
					System.out.println("Enc des : " + encKey);

					FileOutputStream out = new FileOutputStream("File_2.key"); // wegschrijven
																				// geencrypteerde
																				// des
																				// sleutel
					ObjectOutputStream oOUT = new ObjectOutputStream(out);
					oOUT.writeObject(encKey);
					oOUT.close();
					/*
					 * Hash van file (oorspronkelijke boodschap)
					 */
						
					/*
					 * Encryptie hash met private key van de gebruiker
					 * 
					 * FIle_3
					 */

				}

				if (!encrypt) {
					/*
					 * 
					 * Decryptie van de DES Key
					 * 
					 * Hier moet de geencrypteerde sleutel worden uitgelezen en
					 * worden gedecrypteerd
					 * 
					 * File_2
					 */
					inputStream = new ObjectInputStream(new FileInputStream(
					/* model.Encrypt.PRIVATE_KEY_FILE */"private.key"));
					final PrivateKey privateKey = (PrivateKey) inputStream
							.readObject();
					inputStream = new ObjectInputStream(new FileInputStream(
							"File_2.key"));
					String enc = (String) inputStream.readObject();
					byte[] encBytes = enc.getBytes();
					Cipher dipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
					dipher.init(Cipher.DECRYPT_MODE, privateKey);
					byte[] decrypted = new byte[1024];
							decrypted = dipher.doFinal(Base64.getDecoder()
							.decode(encBytes));
					System.out.println("Dec des : "
							+ Base64.getEncoder().encodeToString(decrypted));
					/*
					 * plainText = model.RSA.decrypt(cipherText, privateKey);
					 */

					/*
					 * 
					 * Decryptie boodschap met DES Key
					 * 
					 * File_1
					 */
					Scanner sc = new Scanner(new FileInputStream("File_1.txt"));
					String encTxt = "";
					while (sc.hasNext()) {
						encTxt += sc.nextLine();
					}
					//encryptie: byte[] text = Base64.getEncoder().encode(originalText.getBytes("UTF-8"));
					System.out.println(encTxt);
					System.out.println(encTxt.getBytes("UTF-8"));
					System.out.println(Base64.getDecoder().decode(encTxt));//ERROR
					System.out.println(Base64.getDecoder().decode(encTxt.getBytes(/*"UTF-8"*/)));
					byte[] encTxtBytes = Base64.getDecoder().decode(encTxt.getBytes(/*"UTF-8"*/));//ERROR

					String decText = model.DES.DecryptWithDES(encTxtBytes, "DESKey.key");

					FileOutputStream fOS = new FileOutputStream("originText");
					Formatter form = new Formatter(fOS);
					form.format(decText);
					form.close();
					/*
					 * 
					 * Berekenen Hash van boodschap
					 */

					/*
					 * Decryptie hash file(file 3) met public key gebruiker
					 * 
					 * File_3
					 */

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
		// TODO Auto-generated method stub
		this.mainApp = mainApp;
	}

}
