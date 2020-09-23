package Layer1.Enums;

/**
 * represents the various statuses a transaction can have e
 */
public enum TransactionStatus {
    /**
     * represents a newly created transaction
     */
    NEW,
    /**
     * represents a transaction has not yet been approved by all users participating in it.
     */
    PENDING_APPROVAL,
    /**
     * represents a transaction that has been cancelled (usually by a user, or by the system after too many edits)
     */
    CANCELLED,
    /**
     * represents a transaction approved by all participants but whose meetings have not been confirmed
     */
    OPEN,
    /**
     * represents a transaction that is awaiting confirmation from participants that the initial meeting occurred
     */
    PENDING_MEETING_CONFIRMATION,
    /**
     * represents a transaction that is awaiting confirmation from participants that the return meeting occurred
     */
    PENDING_RETURN_MEETING_CONFIRMATION,
    /**
     * represents a transaction whose meetings were confirmed by all participants
     */
    COMPLETE
}
