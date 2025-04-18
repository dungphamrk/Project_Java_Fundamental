package ra.edu.validate;
import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.technology.Status;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;

public class TechnologyValidator {

    public static String inputName(Scanner scanner) {
        String name;
        boolean checkResult = false;
        do {
            System.out.print("Nhập tên công nghệ: ");
            name = scanner.nextLine();
            if (name.trim().isEmpty()) {
                System.out.println("Tên công nghệ không được để trống. Vui lòng nhập lại.");
                continue;
            }
            // Kiểm tra tính độc nhất
            Connection conn = null;
            CallableStatement callStmt = null;
            try {
                conn = ConnectionDB.openConnection();
                callStmt = conn.prepareCall("{call sp_CheckTechnologyNameExists(?,?)}");
                callStmt.setString(1, name);
                callStmt.registerOutParameter(2, Types.BOOLEAN);
                callStmt.execute();
                checkResult = callStmt.getBoolean(2);
                if (checkResult) {
                    System.err.println("Tên công nghệ đã tồn tại, vui lòng nhập lại.");
                }
            } catch (SQLException e) {
                System.err.println("Lỗi SQL khi kiểm tra tên công nghệ: " + e.getMessage());
            } finally {
                ConnectionDB.closeConnection(conn, callStmt);
            }
        } while (name.trim().isEmpty() || checkResult);
        return name;
    }

    public static Status inputStatus(Scanner scanner) {
        String statusStr;
        do {
            System.out.print("Nhập trạng thái (active/inactive): ");
            statusStr = scanner.nextLine().toUpperCase();
            if (!Validator.isValidDataType(statusStr, Status.class)) {
                System.out.println("Trạng thái không hợp lệ. Vui lòng nhập lại.");
            }
        } while (!Validator.isValidDataType(statusStr, Status.class));
        return Status.valueOf(statusStr);
    }
}