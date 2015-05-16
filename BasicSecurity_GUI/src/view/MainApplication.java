package view;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApplication extends Application{

	private Stage primaryStage;
    private BorderPane initLayout;

	
	@Override
	public void start(Stage primaryStage) throws Exception {
		 this.primaryStage = primaryStage;
	        this.primaryStage.setTitle("Security");
	        
	        initLayout = new BorderPane();
	        Scene scene = new Scene(initLayout);
	        primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setHeight(700);
			primaryStage.setWidth(1000);

	        showGUI();
	    }


	private void showGUI() {
		try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApplication.class.getResource("GUI.fxml"));
            BorderPane GUI = (BorderPane) loader.load();

            // Set login into the center of root layout.
            initLayout.setCenter(GUI);
            
            
            //connect controller with main app
            GUIController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}
	
	public static void main(String[] args){
		launch(args);
	}

	

}
