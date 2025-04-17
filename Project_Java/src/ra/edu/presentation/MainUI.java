package ra.edu.presentation;

import ra.edu.business.model.Candidate;
import ra.edu.business.service.candidateService.CandidateService;
import ra.edu.business.service.candidateService.CandidateServiceImp;

import java.util.Scanner;

public class MainUI {
    private static final CandidateService candidateService = new CandidateServiceImp();

    public static void displayMainMenu(Scanner scanner) {
        // Kiểm tra trạng thái đăng nhập
        String loggedInRole = candidateService.isLoggedIn();
        if (loggedInRole != null) {
            if (loggedInRole.equalsIgnoreCase("CANDIDATE")) {
                CandidateUI.displayCandidateMenu(scanner);
            } else if (loggedInRole.equalsIgnoreCase("ADMIN")) {
                AdminUI.displayAdminMenu(scanner);
            }
            return;
        }

        do {
            System.out.println("***************RECRUITMENT SYSTEM**************");
            System.out.println("1. Đăng ký tài khoản");
            System.out.println("2. Đăng nhập");
            System.out.println("3. Thoát");
            System.out.print("Lựa chọn của bạn: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        register(scanner);
                        break;
                    case 2:
                        login(scanner);
                        break;
                    case 3:
                        System.out.println("Thoát chương trình.");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.err.println("Vui lòng chọn từ 1-3");
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập số từ 1-3");
            }
        } while (true);
    }

    private static void register(Scanner scanner) {
        Candidate candidate = new Candidate();
        System.out.println("=== Đăng ký tài khoản ===");
            candidate.inputData(scanner);
        int result = candidateService.register(candidate);
        if (result == 0) {
            System.out.println("Đăng ký thành công!");
        } else {
            System.out.println("Đăng ký thất bại. Vui lòng kiểm tra lại thông tin.");
        }
    }

    private static void login(Scanner scanner) {
        System.out.println("=== Đăng nhập ===");
        System.out.print("Nhập tên đăng nhập: ");
        String username = scanner.nextLine();
        System.out.print("Nhập mật khẩu: ");
        String password = scanner.nextLine();

        int result = candidateService.login(username, password);
        if (result == 0) {
            System.out.println("Đăng nhập thành công!");
            String role = candidateService.isLoggedIn();
            if (role != null) {
                if (role.equalsIgnoreCase("CANDIDATE")) {
                    CandidateUI.displayCandidateMenu(scanner);
                } else if (role.equalsIgnoreCase("ADMIN")) {
                    AdminUI.displayAdminMenu(scanner);
                }
            }
        } else {
            System.out.println("Đăng nhập thất bại. Tên đăng nhập hoặc mật khẩu không đúng.");
        }
    }
}