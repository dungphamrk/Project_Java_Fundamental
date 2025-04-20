package ra.edu.business.service.userService;

import ra.edu.business.dao.user.UserDao;
import ra.edu.business.dao.user.UserDaoImp;
import ra.edu.business.model.user.User;

import java.util.Scanner;

public class UserServiceImp implements UserService {
    private final UserDao userDao;

    public UserServiceImp() {
        this.userDao = new UserDaoImp();
    }

    @Override
    public int login(String username, String password) {
        return userDao.login(username, password);
    }

    @Override
    public void logout() {
        userDao.logout();
    }

    @Override
    public String isLoggedIn() {
        return userDao.isLoggedIn();
    }

    @Override
    public int findAll(int pageNumber, int pageSize) {
        return 0;
    }

    @Override
    public int save(User user) {
        return userDao.save(user);
    }

    @Override
    public int update(Scanner scanner) {
        return 0;
    }

    @Override
    public int delete(Scanner scanner) {
        return 0;
    }
}
