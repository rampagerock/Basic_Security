package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

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
	private TextArea encFileArea;
	
	@FXML
	private RadioButton encRb;
	@FXML
	private RadioButton decRb;
	
	private ToggleGroup rbGroup;
	
	private boolean encrypt;
	
	public GUIController(){
		
	}
	
	public void initialize(){
		rbGroup = new ToggleGroup();
		encRb.setToggleGroup(rbGroup);
		decRb.setToggleGroup(rbGroup);
		rbGroup.selectedToggleProperty().addListener(new OnToggleHandler());
	//	SubmitHandler handler = new SubmitHandler();
	//	submit.setOnAction(handler);
		
		changeEnc.setOnAction(this::openFile);
		changeFile2.setOnAction(this::openFile);
		changeFile3.setOnAction(this::openFile);
		changePriv.setOnAction(this::openFile);
		changePub.setOnAction(this::openFile);
		changeFile.setOnAction(this::openFile);
		
		welcomeText();
	}
	
	private void welcomeText(){
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					"Tutorial.txt"));
			String line;
			while((line = in.readLine()) != null){
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

	private class OnToggleHandler implements ChangeListener<Toggle>{

		@Override
		public void changed(ObservableValue<? extends Toggle> ov,
				Toggle t, Toggle t1) {
			// TODO Auto-generated method stub
			System.out.println("Observable : " + ov);
			System.out.println("Old Value : " + t);
			System.out.println("New Value : " + t1);
			RadioButton rb = (RadioButton)t1.getToggleGroup().getSelectedToggle();
			switch(rb.getId()){
			case "encRb" : encrypt = true;
				break;
			case "decRb" : encrypt = false;
				break;
			}
			
			if(encrypt){
				disableDecrypt();
				enableEncrypt();
			}else
			{
				disableEncrypt();
				enableDecrypt();
			}
			
		}
		
	}	

	public void disableEncrypt(){
		changeFile.setDisable(true);
		textFile.setDisable(true);
	}
	
	public void disableDecrypt(){
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
	
	public void enableEncrypt(){
		changeFile.setDisable(false);
		textFile.setDisable(false);
	}
	
	public void enableDecrypt(){
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
	
	private void openFile(ActionEvent event){
		Button name = (Button)event.getSource();
		System.out.println(name);
		JFileChooser chooser = new JFileChooser();
		int returnValue = chooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			switch(name.getId()){
			case "changeEnc": textEnc.setText(selectedFile.getName().toString());
				break;
			case "changeFile2": textFile2.setText(selectedFile.getName().toString());
				break;
			case "changeFile3": textFile3.setText(selectedFile.getName().toString());
				break;
			case "changePriv": textPriv.setText(selectedFile.getName().toString());
				break;
			case "changePub" : textPub.setText(selectedFile.getName().toString());
				break;
			case "changeFile" : textFile.setText(selectedFile.getName().toString());
				break;
			default: System.out.println("Something went wrong!");
			}
			/*
			 * encFileArea.clear();
			 * try {
				BufferedReader in = new BufferedReader(new FileReader(
						selectedFile));
				String line = null;
				while ((line = in.readLine()) != null) {
					encFileArea.appendText(line + "\n");
				}
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/

		}
		class SubmitHandler implements EventHandler<ActionEvent>{

			@Override
			public void handle(ActionEvent event) {
		
		try {

		      // Check if the pair of keys are present else generate those.
		      if (!model.Encrypt.areKeysPresent()) {
		        // Method generates a pair of keys using the RSA algorithm and stores it
		        // in their respective files
		       model.Encrypt.generateKey();
		      }

		      final String originalText = "Text to be encrypted ";
		      ObjectInputStream inputStream = null;

		      // Encrypt the string using the public key
		      inputStream = new ObjectInputStream(new FileInputStream(model.Encrypt.PUBLIC_KEY_FILE));
		      final PublicKey publicKey = (PublicKey) inputStream.readObject();
		      final byte[] cipherText = model.Encrypt.encrypt(originalText, publicKey);

		      // Decrypt the cipher text using the private key.
		      inputStream = new ObjectInputStream(new FileInputStream(model.Encrypt.PRIVATE_KEY_FILE));
		      final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
		      final String plainText = model.Encrypt.decrypt(cipherText, privateKey);

		      // Printing the Original, Encrypted and Decrypted Text
		      System.out.println("Original: " + originalText);
		      System.out.println("Encrypted: " +cipherText.toString());
		      System.out.println("Decrypted: " + plainText);

		    } catch (Exception e) {
		      e.printStackTrace();
		    }
			}
		}
	}

	public void setMainApp(MainApplication mainApp) {
		// TODO Auto-generated method stub
		this.mainApp = mainApp;
	}

}