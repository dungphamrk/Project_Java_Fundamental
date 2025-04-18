package ra.edu.validate;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.candidate.Gender;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class CandidateValidator {

    public static String inputName(Scanner scanner) {
        String name;
        do {
            System.out.print("Nhập tên ứng viên: ");
            name = scanner.nextLine();
            if (name.trim().isEmpty()) {
                System.out.println(" Tên không được để trống. Vui lòng nhập lại.");
            }
        } while (name.trim().isEmpty());
        return name;
    }



    public static String inputEmail(Scanner scanner) {
        String email;
        boolean checkResult = false;
        do {
            System.out.print("Nhập email: ");
            email = scanner.nextLine();
            if (!Validator.isValidEmail(email)) {
                System.out.println("Email không hợp lệ. Vui lòng nhập lại.");
            }
            // Kiểm tra tính độc nhất
            Connection conn = null;
            CallableStatement callStmt = null;
            checkResult = false;
            try {
                conn = ConnectionDB.openConnection();
                callStmt = conn.prepareCall("{call sp_CheckCandidateEmailExists(?,?)}");
                callStmt.setString(1, email);
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
                System.err.println("Email đã tồn tại vui lòng nhập lại");
            }
        } while (!Validator.isValidEmail(email) &&  !checkResult);
        return email;
    }

    public static String inputPhone(Scanner scanner) {
        String phone;
        boolean checkResult = false;
        do {
            System.out.print("Nhập số điện thoại: ");
            phone = scanner.nextLine();
            if (!Validator.isValidPhoneNumberVN(phone)) {
                System.out.println(" Số điện thoại không hợp lệ. Vui lòng nhập lại.");
            }
            // Kiểm tra tính độc nhất
            Connection conn = null;
            CallableStatement callStmt = null;
            checkResult = false;
            try {
                conn = ConnectionDB.openConnection();
                callStmt = conn.prepareCall("{call sp_CheckCandidatePhoneExists(?,?)}");
                callStmt.setString(1, phone);
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
                System.err.println("Số điện thoại đã tồn tại vui lòng nhập lại");
            }
        } while (!Validator.isValidPhoneNumberVN(phone)&&  !checkResult);
        return phone;
    }

    public static int inputExperience(Scanner scanner) {
        String expStr;
        do {
            System.out.print("Nhập số năm kinh nghiệm: ");
            expStr = scanner.nextLine();
            if (!Validator.isValidDataType(expStr, Integer.class)) {
                System.out.println(" Giá trị không hợp lệ. Vui lòng nhập số nguyên.");
            }
        } while (!Validator.isValidDataType(expStr, Integer.class));
        return Integer.parseInt(expStr);
    }

    public static Gender inputGender(Scanner scanner) {
        String genderStr;
        do {
            System.out.print("Nhập giới tính (male/female/other): ");
            genderStr = scanner.nextLine();
            if (!Validator.isValidDataType(genderStr, Gender.class)) {
                System.out.println(" Giá trị không hợp lệ. Vui lòng nhập lại.");
            }
        } while (!Validator.isValidDataType(genderStr, Gender.class));
        return Gender.valueOf(genderStr.toUpperCase());
    }

    public static LocalDate inputDob(Scanner scanner) {
        String dobStr;
        do {
            System.out.print("Nhập ngày sinh (dd/MM/yyyy): ");
            dobStr = scanner.nextLine();
            if (!Validator.isValidDataType(dobStr, LocalDate.class)) {
                System.out.println(" Ngày sinh không hợp lệ. Vui lòng nhập lại.");
            }
        } while (!Validator.isValidDataType(dobStr, LocalDate.class));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dobStr, formatter);
    }

    public static String inputDescription(Scanner scanner) {
        System.out.print("Nhập mô tả hay giới thiệu về bản thân: ");
        return scanner.nextLine();
    }


}
