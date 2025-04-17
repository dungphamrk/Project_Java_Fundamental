package ra.edu.presentation;

import java.util.Scanner;

public class TechnologyUI {
    public static void displayTechnologyMenu(Scanner scanner) {
        do {
            System.out.println("***************TECHNOLOGY MANAGEMENT**************");
            System.out.println("1. Lấy danh sách công nghệ");
            System.out.println("2. Thêm công nghệ");
            System.out.println("3. Sửa công nghệ");
            System.out.println("4. Xóa công nghệ");
            System.out.println("5. Quay lại");
            System.out.print("Lựa chọn của bạn: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        System.out.println("Chức năng lấy danh sách công nghệ chưa được triển khai.");
                        break;
                    case 2:
                        System.out.println("Chức năng thêm công nghệ chưa được triển khai.");
                        break;
                    case 3:
                        System.out.println("Chức năng sửa công nghệ chưa được triển khai.");
                        break;
                    case 4:
                        System.out.println("Chức năng xóa công nghệ chưa được triển khai.");
                        break;
                    case 5:
                        return; // Quay lại AdminUI
                    default:
                        System.err.println("Vui lòng chọn từ 1-5");
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập số từ 1-5");
            }
        } while (true);
    }
}