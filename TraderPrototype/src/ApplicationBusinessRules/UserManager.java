package ApplicationBusinessRules;

import EnterpriseBusinessRules.User;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private final List<User> userList = new ArrayList<>();

    public UserManager() {
        User admin = new User(0, "admin", "12345");
        User chris = new User(1, "chris", "12345");
        this.userList.add(admin);
        this.userList.add(chris);
    }

    public boolean validateLoggedInUser(String userName, String userPassword) {
        boolean validationSuccessful = false;
        for (User currentUser : userList) {
            if (currentUser.getUserName().equals(userName)) {
                validationSuccessful = currentUser.getUserPassword().equals(userPassword);
            }
        }
        return validationSuccessful;
    }
    public boolean registerUsernameExists(String userName) {
        for (User user : userList) {
            if (user.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public void registerUser(String userName, String userPassword) {
        User newUser = new User(this.userList.size(), userName, userPassword);
        this.userList.add(newUser);
    }
}
