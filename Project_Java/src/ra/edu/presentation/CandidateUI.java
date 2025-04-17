package ra.edu.presentation;

import java.util.Scanner;

public class CandidateUI {
    public static void displayCandidateMenu(Scanner scanner) {
        do {
            System.out.println("***************CANDIDATE MENU**************");
            System.out.println("1. Quản lý thông tin cá nhân");
            System.out.println("2. Quản lý đơn ứng tuyển");
            System.out.println("3. Xem và nộp đơn ứng tuyển");
            System.out.println("4. Đăng xuất");
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
                        System.out.println("Chức năng xem và nộp đơn ứng tuyển chưa được triển khai.");
                        break;
                    case 4:
                        System.out.println("Chức năng đăng xuất chưa được triển khai.");
                        return; // Quay lại MainUI
                    default:
                        System.err.println("Vui lòng chọn từ 1-4");
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập số từ 1-4");
            }
        } while (true);
    }
}