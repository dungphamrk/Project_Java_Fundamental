package ra.edu.validate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class RecruitmentPositionValidator {

    // Kiểm tra và nhập tên vị trí tuyển dụng
    public static String inputName(Scanner scanner) {
        System.out.println("Nhập tên vị trí tuyển dụng:");
        while (true) {
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.err.println("Tên vị trí tuyển dụng không được để trống. Vui lòng nhập lại:");
                continue;
            }
            if (name.length() < 3 || name.length() > 100) {
                System.err.println("Tên vị trí tuyển dụng phải từ 3 đến 100 ký tự. Vui lòng nhập lại:");
                continue;
            }
            return name;
        }
    }

    // Kiểm tra và nhập mô tả
    public static String inputDescription(Scanner scanner) {
        System.out.println("Nhập mô tả:");
        while (true) {
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                System.err.println("Mô tả không được để trống. Vui lòng nhập lại:");
                continue;
            }
            return description;
        }
    }

    // Kiểm tra và nhập lương tối thiểu
    public static double inputMinSalary(Scanner scanner) {
        System.out.println("Nhập lương tối thiểu:");
        while (true) {
            String value = scanner.nextLine().trim();
            if (!Validator.isValidDataType(value, Double.class)) {
                System.err.println("Lương tối thiểu phải là số thực. Vui lòng nhập lại:");
                continue;
            }
            double minSalary = Double.parseDouble(value);
            if (minSalary < 0) {
                System.err.println("Lương tối thiểu không được âm. Vui lòng nhập lại:");
                continue;
            }
            return minSalary;
        }
    }

    // Kiểm tra và nhập lương tối đa
    public static double inputMaxSalary(Scanner scanner, double minSalary) {
        System.out.println("Nhập lương tối đa:");
        while (true) {
            String value = scanner.nextLine().trim();
            if (!Validator.isValidDataType(value, Double.class)) {
                System.err.println("Lương tối đa phải là số thực. Vui lòng nhập lại:");
                continue;
            }
            double maxSalary = Double.parseDouble(value);
            if (maxSalary < 0) {
                System.err.println("Lương tối đa không được âm. Vui lòng nhập lại:");
                continue;
            }
            if (maxSalary < minSalary) {
                System.err.println("Lương tối đa phải lớn hơn hoặc bằng lương tối thiểu (" + minSalary + "). Vui lòng nhập lại:");
                continue;
            }
            return maxSalary;
        }
    }

    // Kiểm tra và nhập kinh nghiệm tối thiểu
    public static int inputMinExperience(Scanner scanner) {
        System.out.println("Nhập kinh nghiệm tối thiểu (năm):");
        while (true) {
            String value = scanner.nextLine().trim();
            if (!Validator.isValidDataType(value, Integer.class)) {
                System.err.println("Kinh nghiệm tối thiểu phải là số nguyên. Vui lòng nhập lại:");
                continue;
            }
            int minExperience = Integer.parseInt(value);
            if (minExperience < 0) {
                System.err.println("Kinh nghiệm tối thiểu không được âm. Vui lòng nhập lại:");
                continue;
            }
            return minExperience;
        }
    }

    public static LocalDate inputExpiredDate(Scanner scanner) {
        System.out.println("Nhập ngày hết hạn (dd/MM/yyyy):");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        while (true) {
            String expiredDateStr = scanner.nextLine().trim();
            try {
                LocalDate expiredDate = LocalDate.parse(expiredDateStr, formatter);
                LocalDate currentDate = LocalDate.now();
                if (expiredDate.isBefore(currentDate)) {
                    System.err.println("Ngày hết hạn phải sau ngày hiện tại (" + currentDate.format(formatter) + "). Vui lòng nhập lại:");
                    continue;
                }
                return expiredDate;
            } catch (DateTimeParseException e) {
                System.err.println("Định dạng ngày không hợp lệ (phải là dd/MM/yyyy). Vui lòng nhập lại:");
            }
        }
    }
}