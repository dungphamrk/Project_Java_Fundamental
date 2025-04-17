package ra.edu.presentation;

import java.util.Scanner;

public class ProfileUI {
    public static void displayProfileMenu(Scanner scanner) {
        do {
            System.out.println("***************PROFILE MANAGEMENT**************");
            System.out.println("1. Thay đổi thông tin cá nhân");
            System.out.println("2. Đổi mật khẩu");
            System.out.println("3. Quay lại");
            System.out.print("Lựa chọn của bạn: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        System.out.println("Chức năng thay đổi thông tin cá nhân chưa được triển khai.");
                        break;
                    case 2:
                        System.out.println("Chức năng đổi mật khẩu chưa được triển khai.");
                        break;
                    case 3:
                        return; // Quay lại CandidateUI
                    default:
                        System.err.println("Vui lòng chọn từ 1-3");
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập số từ 1-3");
            }
        } while (true);
    }
}