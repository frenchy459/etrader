package InterfaceAdapters;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/*
 * Few things to note:
 * - this prototype was built off of the original TraderPrototype from phase 1
 * - I didn't touch the UserManager class aside from removing whatever methods were not relevant anymore
 * - The User entity was left unchanged
 * - all Presenters and related interfaces have been removed
 * - when loading and FXML file JavaFX always calls the default (0 arg) constructor of its respective controller,
 *   so initialization methods will be necessary for every controller to pass necessary objects around
 */
public class Main extends Application {

    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage primaryStage){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("LandingMenu.fxml"));
            primaryStage.setTitle("Trader");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
        catch (IOException e){e.getStackTrace();}
    }
}
