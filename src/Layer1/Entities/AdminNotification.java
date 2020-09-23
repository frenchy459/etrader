package Layer1.Entities;

import Layer1.Enums.AdminNotificationType;

import java.io.Serializable;
import java.util.UUID;

/** Represents an admin notification entity class.*/
public class AdminNotification implements Serializable {

    private String message;
    private final String username;
    private final UUID MID;
    private final AdminNotificationType notificationType;
    private final UUID unapprovedItem;

    /** Creates an admin notification entity class instance.
     *
     * @param username A string representing the username.
     * @param type A NotificationType object representing the type of the admin notification.
     */
    public AdminNotification(String username, AdminNotificationType type, String message) {
        this.MID = UUID.randomUUID();
        this.username = username;
        notificationType = type;
        setMessage(message);
        this.unapprovedItem = null;
    }

    /**Creates an admin notification entity class instance.
     *
     * @param username A string representing the username.
     * @param type A NotificationType object representing the type of the admin notification.
     * @param item A Item object representing the item in the admin notification.
     */
    public AdminNotification(String username, AdminNotificationType type, UUID item, String message) {
        this.MID = UUID.randomUUID();
        this.username = username;
        notificationType = type;
        setMessage(message);
        unapprovedItem = item;
    }

    /** Sets the message with respect to various AdminNotificationType.
     *
     */
    private void setMessage(String message) {
        if (notificationType.equals(AdminNotificationType.FREEZE))
            this.message = username + message;
        else if (notificationType.equals(AdminNotificationType.UNFREEZE))
            this.message = username + message;
        else // AdminNotificationType.ITEM_REQUEST
            this.message = username + message;

    }

    /** Gets the MID of the admin notification.
     *
     * @return A UUID object representing the MID of the admin notification.
     */
    public UUID getMID() {
        return MID;
    }

    /** Gets the message of the admin notification.
     *
     * @return A String representing the message of the admin notification.
     */
    public String getMessage() {
        return message;
    }

    /** Gets the notification type of the admin notification.
     *
     * @return An AdminNotificationType object representing the notification type of the admin notification.
     */
    public AdminNotificationType getNotificationType() {
        return notificationType;
    }

    /** Gets the username of the admin notification.
     *
     * @return A string representing the username of the admin notification.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return The unapproved item value.
     */
    public UUID getUnapprovedItemUUID() {
        return this.unapprovedItem;
    }
}
