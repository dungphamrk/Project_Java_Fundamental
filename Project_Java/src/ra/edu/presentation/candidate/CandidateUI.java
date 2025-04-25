package ra.edu.presentation.candidate;

import ra.edu.presentation.MainUI;
import ra.edu.presentation.RecruitmentPositionUI;

import java.util.Scanner;

import static ra.edu.presentation.ServiceProvider.userService;

public class CandidateUI {
    // Mã màu ANSI sáng
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[91m";
    private static final String GREEN = "\u001B[92m";
    private static final String CYAN = "\u001B[96m";
    private static final String MAGENTA = "\u001B[95m";
    private static final String WHITE = "\u001B[97m";

    public static void displayCandidateMenu(Scanner scanner) {
        do {
            System.out.println(MAGENTA + "=============================================");
            System.out.println("            MENU ỨNG VIÊN                   ");
            System.out.println("=============================================" + RESET);
            System.out.println(CYAN + "1. Quản lý thông tin cá nhân");
            System.out.println("2. Quản lý đăng ký công nghệ");
            System.out.println("3. Quản lý tuyển dụng");
            System.out.println("4. Đăng xuất" + RESET);
            System.out.print(MAGENTA + "Lựa chọn của bạn: " + RESET);
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        ProfileUI.displayProfileMenu(scanner);
                        break;
                    case 2:
                        CandidateTechnologyUI.displayMenu(scanner, userService.getCurrentUserId());
                        break;
                    case 3:
                        RecruitmentPositionUI.displayCandidateRecruitmentMenu(scanner);
                        break;
                    case 4:
                        userService.logout();
                        System.out.println(GREEN + "Đã đăng xuất!" + RESET);
                        MainUI.displayMainMenu(scanner);
                        return;
                    default:
                        System.out.println(RED + "Vui lòng chọn từ 1-4" + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Vui lòng nhập số từ 1-4" + RESET);
            }
            System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
            scanner.nextLine();
        } while (true);
    }
}