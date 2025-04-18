package ra.edu.validate;

import ra.edu.business.config.ConnectionDB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;

public class UserValidator {
    public static String inputUsername(Scanner scanner) {
        String username;
        boolean checkResult;
        do {
            System.out.print("Nhập tên đăng nhập: ");
            username = scanner.nextLine();
            if (username.trim().isEmpty()) {
                System.out.println(" Tên đăng nhập không được để trống. Vui lòng nhập lại.");
            }

            // Kiểm tra tính độc nhất
            Connection conn = null;
            CallableStatement callStmt = null;
            checkResult = false;
            try {
                conn = ConnectionDB.openConnection();
                callStmt = conn.prepareCall("{call sp_CheckUsernameExists(?,?)}");
                callStmt.setString(1, username);
                callStmt.registerOutParameter(2, Types.BOOLEAN);
                callStmt.execute();
                checkResult = callStmt.getBoolean(2);
            } catch (SQLException e) {
                System.err.println("Có lỗi SQL khi kiểm tra tính hợp lệ " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Lỗi khác khi kiểm tra: " + e.getMessage());
            } finally {
                ConnectionDB.closeConnection(conn, callStmt);
            }
            if(checkResult) {
                System.err.println("Username đã tồn tại vui lòng nhập lại");
            }
        } while (username.trim().isEmpty() && !checkResult);
        return username;
    }
    public static String inputPassword(Scanner scanner) {
        String password;
        do {
            System.out.print("Nhập mật khẩu (ít nhất 6 ký tự): ");
            password = scanner.nextLine();
            if (!Validator.isValidPassword(password)) {
                System.out.println(" Mật khẩu không hợp lệ. Vui lòng nhập lại.");
            }
        } while (!Validator.isValidPassword(password));
        return password;
    }
}
