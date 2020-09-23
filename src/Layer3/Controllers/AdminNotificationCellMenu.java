package Layer3.Controllers;

import Layer1.Entities.AdminNotification;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * An admin Notification cell menu
 */
public class AdminNotificationCellMenu extends AbstractCellMenu {

    @FXML
    Label notificationType;
    @FXML
    Label notificationDescription;

    private final AdminNotification adminNotification;

    /**
     * Creates an admin notification cell menu object
     */
    public AdminNotificationCellMenu(String path, AdminNotification adminNotification) {
        super(path);
        this.adminNotification = adminNotification;
    }

    @Override
    public void init() {
        clickableIndicator();
        setNotificationInfo(adminNotification);
    }

    private void setNotificationInfo(AdminNotification adminNotification){
        notificationType.setText(adminNotification.getNotificationType().toString());
        notificationDescription.setText(adminNotification.getMessage());
    }
}
