package EnterpriseBusinessRules;

public class User {
    private final int uid;
    private final String userName;
    private final String userPassword;

    public User(int uid, String userName, String userPassword) {
        this.uid = uid;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public int getUid() {
        return uid;
    }
}
