package ra.edu.presentation;

import java.util.Scanner;

public class CandidateManagementUI {
    public static void displayCandidateManagementMenu(Scanner scanner) {
        do {
            System.out.println("***************CANDIDATE MANAGEMENT**************");
            System.out.println("1. Hiển thị danh sách ứng viên");
            System.out.println("2. Khóa/mở khóa tài khoản ứng viên");
            System.out.println("3. Reset mật khẩu ứng viên");
            System.out.println("4. Tìm kiếm ứng viên theo tên");
            System.out.println("5. Lọc ứng viên theo kinh nghiệm");
            System.out.println("6. Lọc ứng viên theo tuổi");
            System.out.println("7. Lọc ứng viên theo giới tính");
            System.out.println("8. Lọc ứng viên theo công nghệ");
            System.out.println("9. Quay lại");
            System.out.print("Lựa chọn của bạn: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        System.out.println("Chức năng hiển thị danh sách ứng viên chưa được triển khai.");
                        break;
                    case 2:
                        System.out.println("Chức năng khóa/mở khóa tài khoản chưa được triển khai.");
                        break;
                    case 3:
                        System.out.println("Chức năng reset mật khẩu chưa được triển khai.");
                        break;
                    case 4:
                        System.out.println("Chức năng tìm kiếm ứng viên theo tên chưa được triển khai.");
                        break;
                    case 5:
                        System.out.println("Chức năng lọc ứng viên theo kinh nghiệm chưa được triển khai.");
                        break;
                    case 6:
                        System.out.println("Chức năng lọc ứng viên theo tuổi chưa được triển khai.");
                        break;
                    case 7:
                        System.out.println("Chức năng lọc ứng viên theo giới tính chưa được triển khai.");
                        break;
                    case 8:
                        System.out.println("Chức năng lọc ứng viên theo công nghệ chưa được triển khai.");
                        break;
                    case 9:
                        return; // Quay lại AdminUI
                    default:
                        System.err.println("Vui lòng chọn từ 1-9");
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập số từ 1-9");
            }
        } while (true);
    }
}