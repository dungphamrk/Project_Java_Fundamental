package ra.edu.presentation.admin;

import ra.edu.presentation.ApplicationUI;
import ra.edu.presentation.MainUI;
import ra.edu.presentation.RecruitmentPositionUI;

import java.util.Scanner;

import static ra.edu.presentation.ServiceProvider.userService;

public class AdminUI {
    // Mã màu ANSI sáng
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[91m";
    private static final String GREEN = "\u001B[92m";
    private static final String CYAN = "\u001B[96m";
    private static final String MAGENTA = "\u001B[95m";
    private static final String WHITE = "\u001B[97m";

    public static void displayAdminMenu(Scanner scanner) {
        do {
            System.out.println(MAGENTA + "=============================================");
            System.out.println("             MENU ADMIN                     ");
            System.out.println("=============================================" + RESET);
            System.out.println(CYAN + "1. Quản lý công nghệ");
            System.out.println("2. Quản lý ứng viên");
            System.out.println("3. Quản lý vị trí tuyển dụng");
            System.out.println("4. Quản lý đơn ứng tuyển");
            System.out.println("5. Đăng xuất" + RESET);
            System.out.print(MAGENTA + "Lựa chọn của bạn: " + RESET);
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        TechnologyUI.displayTechnologyMenu(scanner);
                        break;
                    case 2:
                        CandidateManagementUI.displayCandidateManagementMenu(scanner);
                        break;
                    case 3:
                        RecruitmentPositionUI.displayAdminRecruitmentPositionMenu(scanner);
                        break;
                    case 4:
                        ApplicationUI.displayAdminApplicationMenu(scanner);
                        break;
                    case 5:
                        userService.logout();
                        System.out.println(GREEN + "Đã đăng xuất!" + RESET);
                        MainUI.displayMainMenu(scanner);
                        return;
                    default:
                        System.out.println(RED + "Vui lòng chọn từ 1-5" + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Vui lòng nhập số từ 1-5" + RESET);
            }
            System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
            scanner.nextLine();
        } while (true);
    }
}