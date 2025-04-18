package ra.edu.presentation;
import java.util.Scanner;
import static ra.edu.presentation.ServiceProvider.userService;

public class AdminUI {
    public static void displayAdminMenu(Scanner scanner) {
        do {
            System.out.println("***************ADMIN MENU**************");
            System.out.println("1. Quản lý công nghệ");
            System.out.println("2. Quản lý ứng viên");
            System.out.println("3. Quản lý vị trí tuyển dụng");
            System.out.println("4. Quản lý đơn ứng tuyển");
            System.out.println("5. Đăng xuất");
            System.out.print("Lựa chọn của bạn: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        TechnologyUI.displayTechnologyMenu(scanner);
                        break;
                    case 2:
                        CandidateManagementUI.displayCandidateManagementMenu(scanner);
                        break;
                    case 3:
                        RecruitmentPositionUI.displayRecruitmentPositionMenu(scanner);
                        break;
                    case 4:
                        ApplicationUI.displayAdminApplicationMenu(scanner);
                        break;
                    case 5:
                        userService.logout();
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