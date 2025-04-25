package ra.edu.presentation;

import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.model.user.User;
import ra.edu.presentation.admin.AdminUI;
import ra.edu.presentation.candidate.CandidateUI;

import java.util.Scanner;

import static ra.edu.presentation.ServiceProvider.candidateService;
import static ra.edu.presentation.ServiceProvider.userService;

public class MainUI {
    // Mã màu ANSI sáng
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[91m";    // Đỏ sáng
    private static final String GREEN = "\u001B[92m";  // Xanh lá sáng
    private static final String YELLOW = "\u001B[93m"; // Vàng sáng
    private static final String CYAN = "\u001B[96m";   // Xanh lam sáng
    private static final String MAGENTA = "\u001B[95m"; // Tím sáng
    private static final String WHITE = "\u001B[97m";  // Trắng sáng

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
            System.out.println(MAGENTA + "=============================================");
            System.out.println("       HỆ THỐNG QUẢN LÝ TUYỂN DỤNG         ");
            System.out.println("=============================================" + RESET);
            System.out.println(CYAN + "1. Đăng ký tài khoản");
            System.out.println("2. Đăng nhập");
            System.out.println("3. Thoát" + RESET);
            System.out.print(MAGENTA + "Lựa chọn của bạn: " + RESET);
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
                        System.out.println(GREEN + "Thoát chương trình." + RESET);
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println(RED + "Vui lòng chọn từ 1-3" + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Vui lòng nhập số từ 1-3" + RESET);
            }
        } while (true);
    }

    private static void register(Scanner scanner) {
        System.out.println(MAGENTA + "=== ĐĂNG KÝ TÀI KHOẢN ===" + RESET);
        Candidate newCandidate = new Candidate();
        newCandidate.inputData(scanner);
        User newUser = new User();
        newUser.inputData(scanner);
        int result2 = userService.save(newUser);
        newCandidate.setId(newUser.getId());
        int result1 = candidateService.save(newCandidate);
        if (result1 == 0 && result2 == 0) {
            System.out.println(GREEN + "Đăng ký thành công!" + RESET);
        } else {
            System.out.println(RED + "Đăng ký thất bại. Vui lòng kiểm tra lại thông tin." + RESET);
        }
        System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
        scanner.nextLine();
    }

    private static void login(Scanner scanner) {
        System.out.println(MAGENTA + "=== ĐĂNG NHẬP ===" + RESET);
        System.out.print(WHITE + "Nhập tên đăng nhập: " + RESET);
        String username = scanner.nextLine();
        System.out.print(WHITE + "Nhập mật khẩu: " + RESET);
        String password = scanner.nextLine();

        int result = userService.login(username, password);
        if (result == 0) {
            System.out.println(GREEN + "Đăng nhập thành công!" + RESET);
            String role = userService.isLoggedIn();
            if (role != null) {
                if (role.equalsIgnoreCase("CANDIDATE")) {
                    CandidateUI.displayCandidateMenu(scanner);
                } else if (role.equalsIgnoreCase("ADMIN")) {
                    AdminUI.displayAdminMenu(scanner);
                }
            }
        } else {
            System.out.println(RED + "Đăng nhập thất bại. Tên đăng nhập hoặc mật khẩu không đúng." + RESET);
        }
        System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
        scanner.nextLine();
    }
}