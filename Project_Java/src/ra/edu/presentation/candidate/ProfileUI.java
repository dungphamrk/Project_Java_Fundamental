package ra.edu.presentation.candidate;

import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.service.candidateService.CandidateService;
import ra.edu.business.service.candidateService.CandidateServiceImp;
import ra.edu.presentation.ServiceProvider;

import java.util.Scanner;

public class ProfileUI {
    private static final CandidateService candidateService = new CandidateServiceImp();

    public static void displayProfileMenu(Scanner scanner) {
        do {
            System.out.println("***************PROFILE MANAGEMENT**************");
            System.out.println("1. Cập nhật thông tin cá nhân");
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
        int userId = ServiceProvider.userService.getCurrentUserId();
        Candidate candidate = candidateService.getCandidateById(userId);
        if (candidate == null) {
            System.err.println("Không tìm thấy thông tin ứng viên!");
            return;
        }

        // Hiển thị thông tin hiện tại
        System.out.println("Thông tin cá nhân hiện tại:");
        System.out.println("1. Tên: " + candidate.getName());
        System.out.println("2. Email: " + candidate.getEmail());
        System.out.println("3. Số điện thoại: " + candidate.getPhone());
        System.out.println("4. Kinh nghiệm: " + candidate.getExperience() + " năm");
        System.out.println("5. Giới tính: " + candidate.getGender());
        System.out.println("6. Ngày sinh: " + (candidate.getDob() != null ? candidate.getDob() : "Chưa thiết lập"));
        System.out.println("7. Mô tả: " + candidate.getDescription());

        // Thu thập lựa chọn
        System.out.println("Chọn thông tin muốn cập nhật (1-7, nhập 0 để thoát): ");
        try {
            int fieldChoice = Integer.parseInt(scanner.nextLine());
            if (fieldChoice == 0) return;

            String newValue;
            switch (fieldChoice) {
                case 1:
                    System.out.print("Nhập tên mới: ");
                    newValue = scanner.nextLine();
                    break;
                case 2:
                    System.out.print("Nhập email mới: ");
                    newValue = scanner.nextLine();
                    break;
                case 3:
                    System.out.print("Nhập số điện thoại mới: ");
                    newValue = scanner.nextLine();
                    break;
                case 4:
                    System.out.print("Nhập số năm kinh nghiệm mới: ");
                    newValue = scanner.nextLine();
                    break;
                case 5:
                    System.out.print("Nhập giới tính mới (MALE/FEMALE): ");
                    newValue = scanner.nextLine();
                    break;
                case 6:
                    System.out.print("Nhập ngày sinh mới (YYYY-MM-DD): ");
                    newValue = scanner.nextLine();
                    break;
                case 7:
                    System.out.print("Nhập mô tả mới: ");
                    newValue = scanner.nextLine();
                    break;
                default:
                    System.err.println("Lựa chọn không hợp lệ!");
                    return;
            }

            int updateResult = candidateService.updateField(userId, String.valueOf(fieldChoice), newValue);
            switch (updateResult) {
                case 0:
                    System.out.println("Cập nhật thông tin thành công!");
                    break;
                case 1:
                    System.err.println("Giá trị rỗng hoặc không hợp lệ!");
                    break;
                case 2:
                    System.err.println("Email không hợp lệ!");
                    break;
                case 3:
                    System.err.println("Kinh nghiệm phải là số!");
                    break;
                case 4:
                    System.err.println("Giới tính phải là MALE hoặc FEMALE!");
                    break;
                case 5:
                    System.err.println("Ngày sinh không đúng định dạng (YYYY-MM-DD)!");
                    break;
                default:
                    System.err.println("Cập nhật thông tin thất bại!");
            }
        } catch (NumberFormatException e) {
            System.err.println("Vui lòng nhập số từ 0-7");
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
        switch (result) {
            case 1:
                System.out.println("Đổi mật khẩu thành công!");
                break;
            case 2:
                System.err.println("Mật khẩu cũ không đúng!");
                break;
            case 3:
                System.err.println("Mật khẩu mới không hợp lệ (ít nhất 6 ký tự)!");
                break;
            default:
                System.err.println("Đổi mật khẩu thất bại!");
        }
    }
}