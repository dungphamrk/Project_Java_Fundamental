package ra.edu.business.dao.user;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.user.Status;
import ra.edu.business.model.user.User;

import java.io.*;
import java.sql.*;

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
            if (!file.delete()) {
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

    @Override
    public int getCurrentUserId() {
        File file = new File("login_info.txt");
        if (file.exists() && file.length() > 0) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("user_id=")) {
                        String userIdStr = line.substring("user_id=".length());
                        try {
                            return Integer.parseInt(userIdStr);
                        } catch (NumberFormatException e) {
                            System.err.println("Lỗi: user_id trong file không hợp lệ: " + userIdStr);
                            return -1;
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Lỗi khi đọc file đăng nhập: " + e.getMessage());
            }
        }
        System.err.println("Không tìm thấy thông tin đăng nhập hoặc người dùng chưa đăng nhập.");
        return -1;
    }

    private void writeLoginInfoToFile(int userId, String role) {
        String filePath = "login_info.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("user_id=" + userId);
            writer.newLine();
            writer.write("role=" + role);
            writer.newLine();
            System.out.println("Đã ghi thông tin đăng nhập vào " + filePath);
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi file đăng nhập: " + e.getMessage());
        }
    }

    @Override
    public int save(User user) {
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
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            return -1;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi khôi phục auto-commit: " + ex.getMessage());
            }
            ConnectionDB.closeConnection(conn, callStmt);
        }
        return returnCode;
    }

    @Override
    public User getUserById(int id) {
        Connection conn = null;
        CallableStatement callStmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{call sp_GetUserById(?)}");
            callStmt.setInt(1, id);
            rs = callStmt.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));
            } else {
                System.out.println("Không tìm thấy người dùng với ID: " + id);
            }
            conn.commit();
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy người dùng: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi khôi phục auto-commit hoặc đóng ResultSet: " + ex.getMessage());
            }
            ConnectionDB.closeConnection(conn, callStmt);
        }
        return user;
    }


}