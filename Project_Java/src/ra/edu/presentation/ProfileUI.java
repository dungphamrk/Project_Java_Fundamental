package ra.edu.presentation;

import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.service.candidateService.CandidateService;
import ra.edu.business.service.candidateService.CandidateServiceImp;

import java.util.Scanner;

public class ProfileUI {
    private static final CandidateService candidateService = new CandidateServiceImp();

    public static void displayProfileMenu(Scanner scanner) {
        do {
            System.out.println("***************PROFILE MANAGEMENT**************");
            System.out.println("1. Thay đổi thông tin cá nhân");
            System.out.println("2. Đổi mật khẩu");
            System.out.println("3. Quay lại");
            System.out.print("Lựa chọn của bạn: ");
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
                        return; // Quay lại CandidateUI
                    default:
                        System.err.println("Vui lòng chọn từ 1-3");
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập số từ 1-3");
            }
        } while (true);
    }

    private static void updateCandidateProfile(Scanner scanner) {
        int updateResult = candidateService.update(scanner);
        if (updateResult == 0) {
            System.out.println("Cập nhật thông tin cá nhân thành công!");
        } else if (updateResult == 2) {
            System.err.println("Không tìm thấy ứng viên!");
        } else {
            System.err.println("Cập nhật thông tin cá nhân thất bại.");
        }
    }

    private static void changePassword(Scanner scanner) {
        System.out.print("Nhập mật khẩu cũ: ");
        String oldPassword = scanner.nextLine();
        System.out.print("Nhập mật khẩu mới: ");
        String newPassword = scanner.nextLine();
        System.out.print("Xác nhận mật khẩu mới: ");
        String confirmPassword = scanner.nextLine();

        if (!newPassword.equals(confirmPassword)) {
            System.err.println("Mật khẩu mới và xác nhận mật khẩu không khớp!");
            return;
        }

        int userId = ServiceProvider.userService.getCurrentUserId();
        int result = candidateService.changePassword(userId, oldPassword, newPassword);
        if (result == 1) {
            System.out.println("Đổi mật khẩu thành công!");
        } else if (result == 2) {
            System.err.println("Mật khẩu cũ không đúng !");
        } else {
            System.err.println("Đổi mật khẩu thất bại!");
        }
    }
}