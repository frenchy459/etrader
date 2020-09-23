
package Layer3;

import Layer3.Controllers.AbstractController;
import Layer3.Controllers.Trader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.security.KeyStoreException;

/**
 * The main class where program execution starts
 */
public class Main extends Application{
    /**
     * The main function where program execution starts
     *
     * @param args a list of strings of arguments
     */
    public static void main(String[] args) {
        AbstractController.setJson();
        launch(args);
        System.exit(0);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource(AbstractController.getJson("landingMenuFXML")));
            primaryStage.setTitle("Trader");
            primaryStage.setScene(new Scene(loader.load()));
            Trader trader = new Trader();
            loader.setController(trader);
            primaryStage.setOnCloseRequest(event -> trader.saveAll());
            primaryStage.show();
        }
        catch (IOException | KeyStoreException e){e.getStackTrace();}
    }
}
