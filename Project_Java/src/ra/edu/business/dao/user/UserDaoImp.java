package ra.edu.business.dao.user;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.user.User;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class UserDaoImp implements UserDao {
    @Override
    public int login(String username, String password) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;

        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_Login(?,?,?,?,?)}");
            callStmt.setString(1, username);
            callStmt.setString(2, password);
            callStmt.registerOutParameter(3, Types.INTEGER); // userId
            callStmt.registerOutParameter(4, Types.VARCHAR); // role
            callStmt.registerOutParameter(5, Types.INTEGER); // returnCode
            callStmt.execute();
            returnCode = callStmt.getInt(5);

            if (returnCode == 0) {
                int userId = callStmt.getInt(3);
                String role = callStmt.getString(4);
                writeLoginInfoToFile(userId, role); // Ghi ra file
            }

        } catch (SQLException e) {
            System.err.println("Có lỗi SQL khi đăng nhập: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi khác khi đăng nhập: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
        return returnCode;
    }

    @Override
    public void logout() {
        File file = new File("login_info.txt");
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Đăng xuất thành công. File thông tin đăng nhập đã được xóa.");
            } else {
                System.err.println("Không thể xóa file đăng nhập.");
            }
        } else {
            System.out.println("Không có file đăng nhập để xóa.");
        }
    }

    @Override
    public String isLoggedIn() {
        File file = new File("login_info.txt");
        if (file.exists() && file.length() > 0) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("role=")) {
                        return line.substring("role=".length());
                    }
                }
            } catch (IOException e) {
                System.err.println("Lỗi khi đọc file đăng nhập: " + e.getMessage());
            }
        }
        return null;
    }

    private void writeLoginInfoToFile(int userId, String role) {
        String filePath = "login_info.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("user_id=" + userId);
            writer.newLine();
            writer.write("role=" + role);
            writer.newLine();
            System.out.println("Đăng nhập thành công " + filePath);
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi file đăng nhập: " + e.getMessage());
        }
    }

    @Override
    public int findAll(int pageNumber,int pageSize) {
        return 0;
    }

    @Override
    public int save(User user ) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);

            callStmt = conn.prepareCall("{call sp_RegisterUser(?,?,?,?)}");
            callStmt.setString(1, user.getUsername());
            callStmt.setString(2, user.getPassword());
            callStmt.registerOutParameter(3, Types.INTEGER);
            callStmt.registerOutParameter(4, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(3);
            user.setId(callStmt.getInt(4));
            conn.commit();
        } catch (SQLException e) {
            System.err.println("Có lỗi SQL khi đăng ký: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi khác khi đăng ký: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
        return returnCode;
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

