package Layer3.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * an abstract class for CellMenus. Holds all the common functionally found in all CellMenus
 */
public abstract class AbstractCellMenu extends AbstractController {

    @FXML
    AnchorPane anchor;

    private final String path;

    /**
     * constructor to be inherited by subclasses
     * @param path string representing the unique path name for the menu's associated FXML file
     */
    public AbstractCellMenu(String path) {
        this.path = path;
        load();
    }

    public abstract void init();

    /**
     * loads the FXML file and sets its associated controller to this one
     */
    public void load() {
        //NOTE: don't set the fx:id controller for the related FXML file for this
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layer3/FXML/" + path + ".fxml"));
        loader.setController(this);
        try { loader.load(); }
        catch (IOException e) { e.getStackTrace();}
    }

    /**
     * modifies pointer when hovering over the cell
     */
    public void clickableIndicator() {
        anchor.setOnMouseEntered(event -> anchor.setCursor(Cursor.HAND));
        anchor.setOnMouseExited(event -> anchor.setCursor(Cursor.DEFAULT));
    }

    //this should be your outermost pane/box/etc. it'll be used to set the graphic for the ListCell
    public AnchorPane getDisplay() {
        return anchor;
    }
}
