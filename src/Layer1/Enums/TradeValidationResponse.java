package Layer1.Enums;

/** An enum representing responses to trade validation */
public enum TradeValidationResponse {

    /** The trade is valid */
    SUCCESS,

    /** The user has performed too many edits */
    TOO_MANY_EDITS,

    /** No trade was selected */
    NO_SELECTION,

    /** The trade is incomplete */
    INCOMPLETE
}
