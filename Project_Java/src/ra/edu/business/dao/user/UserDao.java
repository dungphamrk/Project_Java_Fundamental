package ra.edu.business.dao.user;

import ra.edu.business.dao.AppDao;

public interface UserDao  extends AppDao {
    int login(String username,String password);
    void logout();
    String isLoggedIn();
}
