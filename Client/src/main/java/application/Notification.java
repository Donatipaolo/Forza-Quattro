package application;


import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Notification {

    public static void showPopUp(Scene scene, String message) {
        Platform.runLater(() -> {
            // 1. Usiamo Parent invece di StackPane per evitare il ClassCastException
            Parent root = scene.getRoot();

            if (!(root instanceof Pane)) {
                System.err.println("Il root della scena non supporta l'aggiunta dinamica di nodi!");
                return;
            }

            Pane container = (Pane) root;

            // 2. Creazione grafica del toast
            Text text = new Text(message);
            text.setStyle("-fx-fill: white; -fx-font-size: 14px;");

            Rectangle rect = new Rectangle();
            rect.setArcWidth(20);
            rect.setArcHeight(20);
            rect.setFill(Color.rgb(50, 50, 50, 0.9));
            
            // Adattiamo il rettangolo al testo
            rect.setWidth(text.getLayoutBounds().getWidth() + 40);
            rect.setHeight(40);

            StackPane toast = new StackPane(rect, text);
            toast.setMouseTransparent(true);

            // 3. Posizionamento manuale (per renderlo compatibile con BorderPane)
            // Lo mettiamo al centro in basso rispetto alla larghezza della finestra
            toast.layoutXProperty().bind(scene.widthProperty().divide(2).subtract(toast.widthProperty().divide(2)));
            toast.layoutYProperty().bind(scene.heightProperty().subtract(60));

            // 4. Aggiunta e Animazione
            container.getChildren().add(toast);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), toast);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            PauseTransition stay = new PauseTransition(Duration.seconds(2));
            
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), toast);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> container.getChildren().remove(toast));

            fadeIn.play();
            fadeIn.setOnFinished(e -> {
                stay.play();
                stay.setOnFinished(e2 -> fadeOut.play());
            });
        });
    }
}