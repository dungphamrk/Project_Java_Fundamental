package ra.edu.business.service.userService;

import ra.edu.business.service.AppService;

public interface UserService extends AppService {
    int login(String username, String password);
    void logout();
    String isLoggedIn();
}
