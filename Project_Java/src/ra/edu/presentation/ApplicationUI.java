package ra.edu.presentation;


import java.util.Scanner;

public class ApplicationUI {
    public static void displayAdminApplicationMenu(Scanner scanner) {
        do {
            System.out.println("***************ADMIN APPLICATION MANAGEMENT**************");
            System.out.println("1. Lấy danh sách đơn ứng tuyển");
            System.out.println("2. Lọc đơn theo tiến trình/kết quả");
            System.out.println("3. Hủy đơn ứng tuyển");
            System.out.println("4. Xem chi tiết đơn ứng tuyển");
            System.out.println("5. Chuyển đơn sang trạng thái interviewing");
            System.out.println("6. Cập nhật kết quả đơn ứng tuyển");
            System.out.println("7. Quay lại");
            System.out.print("Lựa chọn của bạn: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        System.out.println("Chức năng lấy danh sách đơn ứng tuyển chưa được triển khai.");
                        break;
                    case 2:
                        System.out.println("Chức năng lọc đơn theo tiến trình/kết quả chưa được triển khai.");
                        break;
                    case 3:
                        System.out.println("Chức năng hủy đơn ứng tuyển chưa được triển khai.");
                        break;
                    case 4:
                        System.out.println("Chức năng xem chi tiết đơn ứng tuyển chưa được triển khai.");
                        break;
                    case 5:
                        System.out.println("Chức năng chuyển đơn sang interviewing chưa được triển khai.");
                        break;
                    case 6:
                        System.out.println("Chức năng cập nhật kết quả đơn chưa được triển khai.");
                        break;
                    case 7:
                        return; // Quay lại AdminUI
                    default:
                        System.err.println("Vui lòng chọn từ 1-7");
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập số từ 1-7");
            }
        } while (true);
    }

    public static void displayCandidateApplicationMenu(Scanner scanner) {
        do {
            System.out.println("***************CANDIDATE APPLICATION MANAGEMENT**************");
            System.out.println("1. Xem các đơn ứng tuyển đã nộp");
            System.out.println("2. Xem chi tiết đơn ứng tuyển");
            System.out.println("3. Quay lại");
            System.out.print("Lựa chọn của bạn: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        System.out.println("Chức năng xem các đơn ứng tuyển đã nộp chưa được triển khai.");
                        break;
                    case 2:
                        System.out.println("Chức năng xem chi tiết đơn ứng tuyển chưa được triển khai.");
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