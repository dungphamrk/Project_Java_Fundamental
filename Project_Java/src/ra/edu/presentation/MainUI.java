package ra.edu.presentation;

import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.model.user.User;

import java.util.Scanner;

import static ra.edu.presentation.ServiceProvider.candidateService;
import static ra.edu.presentation.ServiceProvider.userService;

public class MainUI {

    public static void displayMainMenu(Scanner scanner) {
        String loggedInRole = userService.isLoggedIn();
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
        System.out.println("=== Đăng ký tài khoản ===");
        Candidate newCandidate  = new Candidate();
        newCandidate.inputData(scanner);
        User newUser = new User();
        newUser.inputData(scanner);
        int result2 = userService.save(newUser);
        newCandidate.setId(newUser.getId());
        System.out.println(newCandidate.getId());
        int result1 = candidateService.save(newCandidate);
        if (result1 == 0 && result2 == 0) {
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

        int result = userService.login(username, password);
        if (result == 0) {
            System.out.println("Đăng nhập thành công!");
            String role = userService.isLoggedIn();
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