package fxJoukkue;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import joukkue.Joukkue;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author jojohyva ja Pchuchat
 * @version 26.1.2021
 *
 */
public class JoukkueMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
		    
            final FXMLLoader ldr = new FXMLLoader(getClass().getResource("JoukkueGUIView.fxml"));
            final Pane root = (Pane)ldr.load();
            final JoukkueGUIController joukkueCtrl = (JoukkueGUIController)ldr.getController();
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("joukkue.css").toExternalForm());
			primaryStage.setScene(scene);
		
			 primaryStage.setOnCloseRequest((event) -> {
                 if ( !joukkueCtrl.voikoSulkea() ) event.consume();
             }); 
			
			 Joukkue joukkue = new Joukkue();
			 joukkueCtrl.setJoukkue(joukkue);
			 
			 
			primaryStage.show();
			
			if (!joukkueCtrl.avaa()) Platform.exit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
