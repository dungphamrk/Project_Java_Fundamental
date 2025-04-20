package ra.edu.business.service.userService;

import ra.edu.business.model.user.User;
import ra.edu.business.service.AppService;

public interface UserService extends AppService<User> {
    int login(String username, String password);
    void logout();
    String isLoggedIn();
}
