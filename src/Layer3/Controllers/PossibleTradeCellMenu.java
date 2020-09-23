package Layer3.Controllers;

import Layer1.Entities.BasicUser;
import Layer1.Entities.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * a possible trade cell menu responsible for the custom design of the cells that displays possible trades
 */
public class PossibleTradeCellMenu extends AbstractCellMenu {

    @FXML
    Label itemTitle;
    @FXML
    Label partnerName;
    @FXML
    ImageView itemImage;
    @FXML
    Label itemDescription;

    private final HashMap<Item, BasicUser> possibleTrade;

    public PossibleTradeCellMenu(String path, HashMap<Item, BasicUser> possibleTrade) {
        super(path);
        this.possibleTrade = possibleTrade;
    }

    @Override
    public void init() {
        clickableIndicator();
        setDisplayInfo();
    }

    private void setDisplayInfo() {
        List<Item> item = new ArrayList<>(possibleTrade.keySet());
        itemTitle.setText(item.get(0).getName());
        itemDescription.setText(item.get(0).getDescription());
        BasicUser user = possibleTrade.get(item.get(0));

        if(user.getLendList().contains(item.get(0).getUUID())){
            partnerName.setText(json.getString("ownedBy") + user.getUsername());
        } else {
            partnerName.setText(json.getString("wishedBy") + user.getUsername());
        }

        itemImage.setImage(imageRepo.get(item.get(0).getUUID().toString()));
    }
}
