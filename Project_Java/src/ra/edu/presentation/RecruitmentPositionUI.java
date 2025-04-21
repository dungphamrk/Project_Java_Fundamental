package ra.edu.presentation;

import ra.edu.business.model.recruitmentPosition.RecruitmentPosition;
import ra.edu.business.service.recruitmentPositionService.RecruitmentPositionService;
import ra.edu.business.service.recruitmentPositionService.RecruitmentPositionServiceImp;

import java.util.Scanner;

public class RecruitmentPositionUI {
    private static final RecruitmentPositionService recruitmentPositionService = new RecruitmentPositionServiceImp();

    public static void displayRecruitmentPositionMenu(Scanner scanner) {
        do {
            System.out.println("***************QUẢN LÝ VỊ TRÍ TUYỂN DỤNG**************");
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
                        displayRecruitmentPositionList(scanner);
                        break;
                    case 2:
                        addRecruitmentPosition(scanner);
                        break;
                    case 3:
                        updateRecruitmentPosition(scanner);
                        break;
                    case 4:
                        deleteRecruitmentPosition(scanner);
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

    private static void displayRecruitmentPositionList(Scanner scanner) {
        int count = recruitmentPositionService.findAll(1, 5);
        if (count == 0) {
            System.out.println("Không có vị trí tuyển dụng nào để hiển thị.");
        }
    }

    private static void addRecruitmentPosition(Scanner scanner) {
        RecruitmentPosition position = new RecruitmentPosition();
        position.inputData(scanner);
        int result = recruitmentPositionService.save(position);
        if (result == 0) {
            System.out.println("Thêm vị trí tuyển dụng thành công.");
        } else {
            System.out.println("Thêm vị trí tuyển dụng thất bại.");
        }
    }

    private static void updateRecruitmentPosition(Scanner scanner) {
        int result = recruitmentPositionService.update(scanner);
        if (result != 0) {
            System.out.println("Không tìm thấy vị trí tuyển dụng hoặc cập nhật thất bại.");
        }
    }

    private static void deleteRecruitmentPosition(Scanner scanner) {
        int result = recruitmentPositionService.delete(scanner);
        if (result != 0) {
            System.out.println("Không tìm thấy vị trí tuyển dụng hoặc xóa thất bại.");
        }
    }
}