package ra.edu.presentation;

import java.util.Scanner;

import static ra.edu.presentation.ServiceProvider.userService;

public class CandidateUI {
    public static void displayCandidateMenu(Scanner scanner) {
        do {
            System.out.println("***************CANDIDATE MENU**************");
            System.out.println("1. Quản lý thông tin cá nhân");
            System.out.println("2. Quản lý đơn ứng tuyển");
            System.out.println("3. Quản lý đăng ký công nghệ");
            System.out.println("4. Xem và nộp đơn ứng tuyển");
            System.out.println("5. Đăng xuất");
            System.out.print("Lựa chọn của bạn: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        ProfileUI.displayProfileMenu(scanner);
                        break;
                    case 2:
                        ApplicationUI.displayCandidateApplicationMenu(scanner);
                        break;
                    case 3:
                        CandidateTechnologyUI.displayMenu(scanner, userService.getCurrentUserId());
                        break;
                    case 4:
                        System.out.println("Chức năng xem và nộp đơn ứng tuyển chưa được triển khai.");
                        break;
                    case 5:
                        userService.logout();
                        System.out.println("Đã đăng xuất!");
                        MainUI.displayMainMenu(scanner);
                        return;
                    default:
                        System.err.println("Vui lòng chọn từ 1-5");
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập số từ 1-5");
            }
        } while (true);
    }
}