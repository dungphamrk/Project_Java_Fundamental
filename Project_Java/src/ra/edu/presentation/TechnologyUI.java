package ra.edu.presentation;

import ra.edu.business.service.technology.TechnologyService;
import ra.edu.business.service.technology.TechnologyServiceImp;

import java.util.Scanner;

public class TechnologyUI {
    private static final TechnologyService technologyService = new TechnologyServiceImp();

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
                        int count = technologyService.findAll();
                        System.out.println("Tổng số công nghệ: " + count);
                        break;
                    case 2:
                        int saveResult = technologyService.save(scanner);
                        if (saveResult > 0) {
                            System.out.println("Thêm công nghệ thành công!");
                        } else {
                            System.err.println("Thêm công nghệ thất bại.");
                        }
                        break;
                    case 3:
                        int updateResult = technologyService.update(scanner);
                        if (updateResult > 0) {
                            System.out.println("Cập nhật công nghệ thành công!");
                        } else {
                            System.err.println("Cập nhật công nghệ thất bại.");
                        }
                        break;
                    case 4:
                        int deleteResult = technologyService.delete(scanner);
                        if (deleteResult > 0) {
                            System.out.println("Xóa công nghệ thành công!");
                        } else {
                            System.err.println("Xóa công nghệ thất bại.");
                        }
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