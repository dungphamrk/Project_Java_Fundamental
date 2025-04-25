package ra.edu.presentation.candidate;

import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.service.candidateService.CandidateService;
import ra.edu.business.service.candidateService.CandidateServiceImp;
import ra.edu.presentation.ServiceProvider;
import ra.edu.validate.CandidateValidator;

import java.util.Scanner;

import static ra.edu.presentation.ServiceProvider.candidateService;

public class ProfileUI {
    // Mã màu ANSI sáng
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[91m";
    private static final String GREEN = "\u001B[92m";
    private static final String YELLOW = "\u001B[93m";
    private static final String CYAN = "\u001B[96m";
    private static final String MAGENTA = "\u001B[95m";
    private static final String WHITE = "\u001B[97m";


    public static void displayProfileMenu(Scanner scanner) {
        do {
            System.out.println(MAGENTA + "=============================================");
            System.out.println("         QUẢN LÝ THÔNG TIN CÁ NHÂN         ");
            System.out.println("=============================================" + RESET);
            System.out.println(CYAN + "1. Cập nhật thông tin cá nhân");
            System.out.println("2. Đổi mật khẩu");
            System.out.println("3. Quay lại" + RESET);
            System.out.print(MAGENTA + "Lựa chọn của bạn: " + RESET);
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        updateCandidateProfile(scanner);
                        break;
                    case 2:
                        changePassword(scanner);
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println(RED + "Vui lòng chọn từ 1-3" + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Vui lòng nhập số từ 1-3" + RESET);
            }
            System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
            scanner.nextLine();
        } while (true);
    }

    private static void updateCandidateProfile(Scanner scanner) {
        int userId = ServiceProvider.userService.getCurrentUserId();
        Candidate candidate = candidateService.getCandidateById(userId);
        if (candidate == null) {
            System.out.println(RED + "Không tìm thấy thông tin ứng viên!" + RESET);
            System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
            scanner.nextLine();
            return;
        }

        System.out.println(MAGENTA + "=== THÔNG TIN CÁ NHÂN HIỆN TẠI ===" + RESET);
        System.out.println(YELLOW + "+----+-----------------------------+");
        System.out.println("| STT| Thông tin                   |");
        System.out.println("+----+-----------------------------+" + RESET);
        System.out.printf(WHITE + "| %-2d | %-27s |%n", 1, "Tên: " + candidate.getName());
        System.out.printf("| %-2d | %-27s |%n", 2, "Email: " + candidate.getEmail());
        System.out.printf("| %-2d | %-27s |%n", 3, "Số điện thoại: " + candidate.getPhone());
        System.out.printf("| %-2d | %-27s |%n", 4, "Kinh nghiệm: " + candidate.getExperience() + " năm");
        System.out.printf("| %-2d | %-27s |%n", 5, "Giới tính: " + candidate.getGender());
        System.out.printf("| %-2d | %-27s |%n", 6, "Ngày sinh: " + (candidate.getDob() != null ? candidate.getDob() : "Chưa thiết lập"));
        System.out.printf("| %-2d | %-27s |%n", 7, "Mô tả: " + (candidate.getDescription() != null ? candidate.getDescription() : "Chưa thiết lập"));
        System.out.println(YELLOW + "+----+-----------------------------+" + RESET);

        System.out.print(MAGENTA + "Chọn thông tin muốn cập nhật (1-7, nhập 0 để thoát): " + RESET);
        try {
            int fieldChoice = Integer.parseInt(scanner.nextLine());
            if (fieldChoice == 0) return;

            String newValue;
            System.out.print(WHITE + "Nhập giá trị mới: " + RESET);
            switch (fieldChoice) {
                case 1:
                    newValue = CandidateValidator.inputName(scanner);
                    break;
                case 2:
                    newValue = CandidateValidator.inputEmail(scanner);
                    break;
                case 3:
                    newValue = CandidateValidator.inputPhone(scanner);
                    break;
                case 4:
                    newValue = String.valueOf(CandidateValidator.inputExperience(scanner));
                    break;
                case 5:
                    newValue = String.valueOf(CandidateValidator.inputGender(scanner));
                    break;
                case 6:
                    newValue = String.valueOf(CandidateValidator.inputDob(scanner));
                    break;
                case 7:
                    newValue = CandidateValidator.inputDescription(scanner);
                    break;
                default:
                    System.out.println(RED + "Lựa chọn không hợp lệ!" + RESET);
                    return;
            }
            candidateService.updateField(userId, String.valueOf(fieldChoice), newValue);
        } catch (NumberFormatException e) {
            System.out.println(RED + "Vui lòng nhập số từ 0-7" + RESET);
        }
    }

    private static void changePassword(Scanner scanner) {
        System.out.println(MAGENTA + "=== ĐỔI MẬT KHẨU ===" + RESET);
        System.out.println(WHITE + "Nhập số điện thoại để xác thực: " + RESET);
        String phone = scanner.nextLine();
        System.out.print(WHITE + "Nhập mật khẩu cũ: " + RESET);
        String oldPassword = scanner.nextLine();
        System.out.print(WHITE + "Nhập mật khẩu mới: " + RESET);
        String newPassword = scanner.nextLine();
        System.out.print(WHITE + "Xác nhận mật khẩu mới: " + RESET);
        String confirmPassword = scanner.nextLine();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println(RED + "Mật khẩu mới và xác nhận mật khẩu không khớp!" + RESET);
            System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
            scanner.nextLine();
            return;
        }

        int userId = ServiceProvider.userService.getCurrentUserId();
        int result = candidateService.changePassword(userId, oldPassword, newPassword,phone);
        switch (result) {
            case 1:
                System.out.println(GREEN + "Đổi mật khẩu thành công!" + RESET);
                break;
            case 2:
                System.out.println(RED + "Mật khẩu cũ không đúng!" + RESET);
                break;
            case 3:
                System.out.println(RED + "Mật khẩu mới không hợp lệ (ít nhất 6 ký tự)!" + RESET);
                break;
            default:
                System.out.println(RED + "Đổi mật khẩu thất bại!" + RESET);
        }
    }
}