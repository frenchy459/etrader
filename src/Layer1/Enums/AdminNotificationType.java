package Layer1.Enums;

/**
 * The enum Admin notification type represents the different types of admin notifications.
 */
public enum AdminNotificationType {

    /**
     * Item request admin notification type represents when a BasicUser is requesting for their new item
     * to be approved.
     */
    ITEM_REQUEST,

    /**
     * Unfreeze admin notification type represents when a BasicUser wants to have their account unfrozen.
     */
    UNFREEZE,

    /**
     * Freeze admin notification type represents when a system tells the admin that a BasicUser should be frozen.
     */
    FREEZE
}
