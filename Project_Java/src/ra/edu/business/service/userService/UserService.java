package ra.edu.business.service.userService;

import ra.edu.business.model.user.User;
import ra.edu.business.service.AppService;

public interface UserService{
    int login(String username, String password);
    void logout();
    String isLoggedIn();
    int getCurrentUserId();
    int save(User user);
}