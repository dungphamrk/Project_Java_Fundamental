package ra.edu.presentation;

import ra.edu.business.service.candidateService.CandidateService;
import ra.edu.business.service.candidateService.CandidateServiceImp;

import java.util.Scanner;
import java.security.SecureRandom;

public class CandidateManagementUI {
    private static final CandidateService candidateService = new CandidateServiceImp();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
    private static final int PASSWORD_LENGTH = 12;

    public static void displayCandidateManagementMenu(Scanner scanner) {
        do {
            System.out.println("***************QUẢN LÝ ỨNG VIÊN**************");
            System.out.println("1. Hiển thị danh sách ứng viên");
            System.out.println("2. Khóa/mở khóa tài khoản ứng viên");
            System.out.println("3. Reset mật khẩu ứng viên");
            System.out.println("4. Tìm kiếm ứng viên theo tên");
            System.out.println("5. Lọc ứng viên theo kinh nghiệm");
            System.out.println("6. Lọc ứng viên theo tuổi");
            System.out.println("7. Lọc ứng viên theo giới tính");
            System.out.println("8. Lọc ứng viên theo công nghệ");
            System.out.println("9. Quay lại");
            System.out.print("Lựa chọn của bạn: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        displayCandidateList(scanner);
                        break;
                    case 2:
                        lockUnlockCandidateAccount(scanner);
                        break;
                    case 3:
                        resetCandidatePassword(scanner);
                        break;
                    case 4:
                        searchCandidateByName(scanner);
                        break;
                    case 5:
                        filterCandidatesByExperience(scanner);
                        break;
                    case 6:
                        filterCandidatesByAge(scanner);
                        break;
                    case 7:
                        filterCandidatesByGender(scanner);
                        break;
                    case 8:
                        filterCandidatesByTechnology(scanner);
                        break;
                    case 9:
                        return; // Quay lại AdminUI
                    default:
                        System.err.println("Vui lòng chọn từ 1-9");
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập số từ 1-9");
            }
        } while (true);
    }

    private static void displayCandidateList(Scanner scanner) {
        int count = candidateService.findAll(1, 5);
        if (count == 0) {
            System.out.println("Không có ứng viên nào để hiển thị.");
        }
    }

    private static void lockUnlockCandidateAccount(Scanner scanner) {
        // Hiển thị danh sách ứng viên với trạng thái
        int count = candidateService.findAll(1, Integer.MAX_VALUE); // Lấy tất cả ứng viên
        if (count == 0) {
            System.out.println("Không có ứng viên nào để khóa/mở khóa.");
            return;
        }

        System.out.println("Nhập ID ứng viên cần khóa/mở khóa:");
        try {
            int candidateId = Integer.parseInt(scanner.nextLine());

            // Xác nhận thay đổi trạng thái
            System.out.println("Bạn có chắc chắn muốn thay đổi trạng thái tài khoản (khóa -> mở khóa hoặc ngược lại)? (Y/N):");
            String confirmation = scanner.nextLine().trim().toUpperCase();
            if (!confirmation.equals("Y")) {
                System.out.println("Hủy thao tác khóa/mở khóa.");
                return;
            }

            // Gọi service để thay đổi trạng thái (logic trạng thái sẽ được xử lý ở tầng DAO)
            boolean success = candidateService.lockUnlockAccount(candidateId, false); // Tham số lockStatus không quan trọng vì sẽ đảo trạng thái
            if (!success) {
                System.out.println("Không tìm thấy ứng viên hoặc cập nhật thất bại.");
            }
        } catch (NumberFormatException e) {
            System.err.println("ID ứng viên phải là số nguyên.");
        }
    }

    private static void resetCandidatePassword(Scanner scanner) {
        System.out.println("Nhập ID ứng viên cần reset mật khẩu:");
        try {
            int candidateId = Integer.parseInt(scanner.nextLine());

            // Xác nhận reset mật khẩu
            System.out.println("Bạn có chắc chắn muốn reset mật khẩu cho ứng viên này? (Y/N):");
            String confirmation = scanner.nextLine().trim().toUpperCase();
            if (!confirmation.equals("Y")) {
                System.out.println("Hủy thao tác reset mật khẩu.");
                return;
            }

            // Tạo mật khẩu ngẫu nhiên
            String newPassword = generateRandomPassword();
            int result = candidateService.changePassword(candidateId, "", newPassword);
            if (result == 1) {
                System.out.println("Reset mật khẩu thành công. Mật khẩu mới: " + newPassword);
            } else {
                System.out.println("Reset mật khẩu thất bại, kiểm tra ID ứng viên.");
            }
        } catch (NumberFormatException e) {
            System.err.println("ID ứng viên phải là số nguyên.");
        }
    }

    private static String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }

    private static void searchCandidateByName(Scanner scanner) {
        System.out.println("Nhập tên ứng viên cần tìm kiếm:");
        String name = scanner.nextLine();
        boolean found = candidateService.searchByName(name);
        if (!found) {
            System.out.println("Không tìm thấy ứng viên với tên: " + name);
        }
    }

    private static void filterCandidatesByExperience(Scanner scanner) {
        System.out.println("Nhập số năm kinh nghiệm tối thiểu:");
        try {
            int experience = Integer.parseInt(scanner.nextLine());
            if (experience < 0) {
                System.err.println("Kinh nghiệm không thể âm.");
                return;
            }
            boolean found = candidateService.filterByExperience(experience);
            if (!found) {
                System.out.println("Không tìm thấy ứng viên với kinh nghiệm từ " + experience + " năm trở lên.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Kinh nghiệm phải là số nguyên.");
        }
    }

    private static void filterCandidatesByAge(Scanner scanner) {
        System.out.println("Nhập độ tuổi tối thiểu:");
        try {
            int age = Integer.parseInt(scanner.nextLine());
            if (age < 0) {
                System.err.println("Tuổi không thể âm.");
                return;
            }
            boolean found = candidateService.filterByAge(age);
            if (!found) {
                System.out.println("Không tìm thấy ứng viên từ " + age + " tuổi trở lên.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Tuổi phải là số nguyên.");
        }
    }

    private static void filterCandidatesByGender(Scanner scanner) {
        System.out.println("Nhập giới tính (MALE/FEMALE):");
        String gender = scanner.nextLine().toUpperCase();
        if (!gender.equals("MALE") && !gender.equals("FEMALE")) {
            System.err.println("Giới tính phải là MALE hoặc FEMALE.");
            return;
        }
        boolean found = candidateService.filterByGender(gender);
        if (!found) {
            System.out.println("Không tìm thấy ứng viên với giới tính: " + gender);
        }
    }

    private static void filterCandidatesByTechnology(Scanner scanner) {
        System.out.println("Nhập công nghệ cần lọc (ví dụ: Java, Python):");
        String technology = scanner.nextLine();
        boolean found = candidateService.filterByTechnology(technology);
        if (!found) {
            System.out.println("Không tìm thấy ứng viên với công nghệ: " + technology);
        }
    }
}