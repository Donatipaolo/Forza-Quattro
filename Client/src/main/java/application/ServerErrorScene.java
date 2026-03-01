package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

public class ServerErrorScene {
    
    private Scene scene;

    public ServerErrorScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("models/server_error.fxml"));
            Parent root = loader.load();
            this.scene = new Scene(root, 1000, 750);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Scene getScene() {
        return scene;
    }
}