package ra.edu.presentation.candidate;

import ra.edu.presentation.MainUI;
import ra.edu.presentation.RecruitmentPositionUI;

import java.util.Scanner;

import static ra.edu.presentation.ServiceProvider.userService;

public class CandidateUI {
    public static void displayCandidateMenu(Scanner scanner) {
        do {
            System.out.println("***************MENU ỨNG VIÊN**************");
            System.out.println("1. Quản lý thông tin cá nhân");
            System.out.println("2. Quản lý đăng ký công nghệ");
            System.out.println("3. Quản lý tuyển dụng");
            System.out.println("4. Đăng xuất");
            System.out.print("Lựa chọn của bạn: ");
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
                        System.out.println("Đã đăng xuất!");
                        MainUI.displayMainMenu(scanner);
                        return;
                    default:
                        System.err.println("Vui lòng chọn từ 1-4");
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập số từ 1-4");
            }
        } while (true);
    }
}