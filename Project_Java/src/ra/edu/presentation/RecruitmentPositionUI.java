package ra.edu.presentation;


import java.util.Scanner;

public class RecruitmentPositionUI {
    public static void displayRecruitmentPositionMenu(Scanner scanner) {
        do {
            System.out.println("***************RECRUITMENT POSITION MANAGEMENT**************");
            System.out.println("1. Lấy danh sách vị trí tuyển dụng");
            System.out.println("2. Thêm vị trí tuyển dụng");
            System.out.println("3. Cập nhật vị trí tuyển dụng");
            System.out.println("4. Xóa vị trí tuyển dụng");
            System.out.println("5. Quay lại");
            System.out.print("Lựa chọn của bạn: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        System.out.println("Chức năng lấy danh sách vị trí tuyển dụng chưa được triển khai.");
                        break;
                    case 2:
                        System.out.println("Chức năng thêm vị trí tuyển dụng chưa được triển khai.");
                        break;
                    case 3:
                        System.out.println("Chức năng cập nhật vị trí tuyển dụng chưa được triển khai.");
                        break;
                    case 4:
                        System.out.println("Chức năng xóa vị trí tuyển dụng chưa được triển khai.");
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