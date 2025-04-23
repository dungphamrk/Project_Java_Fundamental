package ra.edu.presentation.admin;

import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.service.candidateService.CandidateService;
import ra.edu.business.service.candidateService.CandidateServiceImp;

import java.util.List;
import java.util.Scanner;

public class CandidateManagementUI {
    private static final CandidateService candidateService = new CandidateServiceImp();

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
                        return;
                    default:
                        System.err.println("Vui lòng chọn từ 1-9");
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập số từ 1-9");
            }
        } while (true);
    }

    private static void displayCandidateList(Scanner scanner) {
        List<Candidate> candidates = candidateService.findAll(1, 5);
        if (candidates.isEmpty()) {
            System.out.println("Không có ứng viên nào để hiển thị.");
        } else {
            System.out.println("Danh sách ứng viên:");
            System.out.println("--------------------------------------------------");
            for (Candidate candidate : candidates) {
                System.out.println("ID: " + candidate.getId() +
                        ", Tên: " + candidate.getName() +
                        ", Email: " + candidate.getEmail());
            }
        }
    }

    private static void lockUnlockCandidateAccount(Scanner scanner) {
        List<Candidate> candidates = candidateService.findAll(1, 5);
        if (candidates.isEmpty()) {
            System.out.println("Không có ứng viên nào để khóa/mở khóa.");
            return;
        }
        displayCandidateList(scanner);

        System.out.println("Nhập ID ứng viên cần khóa/mở khóa:");
        try {
            int candidateId = Integer.parseInt(scanner.nextLine());
            System.out.println("Bạn có chắc chắn muốn thay đổi trạng thái tài khoản? (Y/N):");
            String confirmation = scanner.nextLine().trim().toUpperCase();
            if (!confirmation.equals("Y")) {
                System.out.println("Hủy thao tác khóa/mở khóa.");
                return;
            }

            boolean success = candidateService.lockUnlockAccount(candidateId);
            if (success) {
                System.out.println("Thay đổi trạng thái tài khoản thành công.");
            } else {
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
            System.out.println("Bạn có chắc chắn muốn reset mật khẩu? (Y/N):");
            String confirmation = scanner.nextLine().trim().toUpperCase();
            if (!confirmation.equals("Y")) {
                System.out.println("Hủy thao tác reset mật khẩu.");
                return;
            }

            String newPassword = candidateService.generateRandomPassword();
            int result = candidateService.resetPassword(candidateId, newPassword);
            if (result == 0) {
                System.out.println("Reset mật khẩu thành công. Mật khẩu mới: " + newPassword);
            } else if (result == 1) {
                System.err.println("Mật khẩu mới không hợp lệ!");
            } else {
                System.err.println("Reset mật khẩu thất bại, kiểm tra ID ứng viên.");
            }
        } catch (NumberFormatException e) {
            System.err.println("ID ứng viên phải là số nguyên.");
        }
    }

    private static void searchCandidateByName(Scanner scanner) {
        System.out.println("Nhập tên ứng viên cần tìm kiếm:");
        String name = scanner.nextLine();
        List<Candidate> candidates = candidateService.searchByName(name);
        if (candidates.isEmpty()) {
            System.out.println("Không tìm thấy ứng viên với tên: " + name);
        } else {
            System.out.println("Kết quả tìm kiếm ứng viên theo tên:");
            System.out.println("--------------------------------------------------");
            for (Candidate candidate : candidates) {
                System.out.println("ID: " + candidate.getId() +
                        ", Tên: " + candidate.getName() +
                        ", Email: " + candidate.getEmail());
            }
        }
    }

    private static void filterCandidatesByExperience(Scanner scanner) {
        System.out.println("Nhập số năm kinh nghiệm tối thiểu:");
        try {
            int experience = Integer.parseInt(scanner.nextLine());
            List<Candidate> candidates = candidateService.filterByExperience(experience);
            if (candidates.isEmpty()) {
                System.out.println("Không tìm thấy ứng viên với kinh nghiệm từ " + experience + " năm trở lên.");
            } else {
                System.out.println("Danh sách ứng viên có kinh nghiệm từ " + experience + " năm trở lên:");
                System.out.println("--------------------------------------------------");
                for (Candidate candidate : candidates) {
                    System.out.println("ID: " + candidate.getId() +
                            ", Tên: " + candidate.getName() +
                            ", Email: " + candidate.getEmail() +
                            ", Kinh nghiệm: " + candidate.getExperience() + " năm");
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Kinh nghiệm phải là số nguyên.");
        }
    }

    private static void filterCandidatesByAge(Scanner scanner) {
        System.out.println("Nhập độ tuổi tối thiểu:");
        try {
            int age = Integer.parseInt(scanner.nextLine());
            List<Candidate> candidates = candidateService.filterByAge(age);
            if (candidates.isEmpty()) {
                System.out.println("Không tìm thấy ứng viên từ " + age + " tuổi trở lên.");
            } else {
                System.out.println("Danh sách ứng viên từ " + age + " tuổi trở lên:");
                System.out.println("--------------------------------------------------");
                for (Candidate candidate : candidates) {
                    System.out.println("ID: " + candidate.getId() +
                            ", Tên: " + candidate.getName() +
                            ", Email: " + candidate.getEmail() +
                            ", Ngày sinh: " + (candidate.getDob() != null ? candidate.getDob() : "Chưa thiết lập"));
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Tuổi phải là số nguyên.");
        }
    }

    private static void filterCandidatesByGender(Scanner scanner) {
        System.out.println("Nhập giới tính (MALE/FEMALE):");
        String gender = scanner.nextLine();
        List<Candidate> candidates = candidateService.filterByGender(gender);
        if (candidates.isEmpty()) {
            System.out.println("Không tìm thấy ứng viên với giới tính: " + gender);
        } else {
            System.out.println("Danh sách ứng viên có giới tính " + gender + ":");
            System.out.println("--------------------------------------------------");
            for (Candidate candidate : candidates) {
                System.out.println("ID: " + candidate.getId() +
                        ", Tên: " + candidate.getName() +
                        ", Email: " + candidate.getEmail() +
                        ", Giới tính: " + candidate.getGender());
            }
        }
    }

    private static void filterCandidatesByTechnology(Scanner scanner) {
        System.out.println("Nhập công nghệ cần lọc (ví dụ: Java, Python):");
        String technology = scanner.nextLine();
        List<Candidate> candidates = candidateService.filterByTechnology(technology);
        if (candidates.isEmpty()) {
            System.out.println("Không tìm thấy ứng viên với công nghệ: " + technology);
        } else {
            System.out.println("Danh sách ứng viên sử dụng công nghệ " + technology + ":");
            System.out.println("--------------------------------------------------");
            for (Candidate candidate : candidates) {
                System.out.println("ID: " + candidate.getId() +
                        ", Tên: " + candidate.getName() +
                        ", Email: " + candidate.getEmail());
            }
        }
    }
}