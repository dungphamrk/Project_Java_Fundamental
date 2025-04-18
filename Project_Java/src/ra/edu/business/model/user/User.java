package ra.edu.business.model.user;

import ra.edu.business.model.Inputable;
import ra.edu.validate.UserValidator;

import java.util.Scanner;

public class User implements Inputable {
    private int id;
    private String username;
    private String password;
    private UserRole role;
    private Status status;

    public User() {
    }

    public User(int id, String username, String password, UserRole role, Status status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public void inputData(Scanner scanner) {
        this.username = UserValidator.inputUsername(scanner);
        this.password = UserValidator.inputPassword(scanner);
        this.role = UserRole.CANDIDATE;
        this.status = Status.ACTIVE;
    }
}
