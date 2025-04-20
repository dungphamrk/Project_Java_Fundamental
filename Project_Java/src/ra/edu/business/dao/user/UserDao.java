package ra.edu.business.dao.user;

import ra.edu.business.dao.AppDao;
import ra.edu.business.model.user.User;

public interface UserDao  extends AppDao<User> {
    int login(String username,String password);
    void logout();
    String isLoggedIn();
}
