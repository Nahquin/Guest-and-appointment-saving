package vendeg_es_idopont_tarolo;

/**
 *
 * @author Bottyán Deli Dániel
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Vendeg_es_idopont_tarolo extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setResizable(false);
        stage.setTitle("NAPTÁR");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        DB ab = new DB();
        launch(args);
    }
    
}
