package ra.edu.presentation;

import ra.edu.business.model.technology.Technology;
import ra.edu.business.service.technology.TechnologyService;
import ra.edu.business.service.technology.TechnologyServiceImp;
import ra.edu.validate.Validator;

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
            System.out.println("5. Tìm kiếm công nghệ theo tên");
            System.out.println("6. Tìm kiếm công nghệ theo ID");
            System.out.println("7. Quay lại");
            try {
                int choice = Validator.validateChoice(scanner);
                switch (choice) {
                    case 1:
                        int pageNumber = 1; // Mặc định trang đầu tiên
                        int pageSize = 5;  // Mặc định 5 phần tử trên trang
                        displayTechnologyList(scanner, pageNumber, pageSize);
                        break;
                    case 2:
                        Technology newTechnology = new Technology();
                        newTechnology.inputData(scanner);
                        int saveResult = technologyService.save(newTechnology);
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
                        searchTechnologyByName(scanner);
                        break;
                    case 6:
                        searchTechnologyById(scanner);
                        break;
                    case 7:
                        return; // Quay lại AdminUI
                    default:
                        System.err.println("Vui lòng chọn từ 1-7");
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập số hợp lệ");
            }
        } while (true);
    }

    private static void displayTechnologyList(Scanner scanner, int pageNumber, int pageSize) {
        do {
            int totalPages = technologyService.findAll(pageNumber, pageSize);
            if (totalPages == 0) {
                System.err.println("Không có trang nào để hiển thị.");
                return;
            }
            System.out.println("***************PAGINATION MENU**************");
            System.out.println("1. Nhập số trang bạn muốn chuyển sang");
            System.out.println("2. Trang trước");
            System.out.println("3. Trang sau");
            System.out.println("4. Thay đổi số phần tử trên một trang");
            System.out.println("5. Quay lại");
            try {
                int paginationChoice = Validator.validateChoice(scanner);
                switch (paginationChoice) {
                    case 1:
                        System.out.print("Nhập số trang: ");
                        pageNumber = Integer.parseInt(scanner.nextLine());
                        if (pageNumber < 1 || pageNumber > totalPages) {
                            System.err.println("Số trang phải từ 1 đến " + totalPages + ".");
                            pageNumber = 1;
                        }
                        break;
                    case 2:
                        if (pageNumber > 1) {
                            pageNumber--;
                        } else {
                            System.err.println("Đang ở trang đầu tiên!");
                        }
                        break;
                    case 3:
                        if (pageNumber < totalPages) {
                            pageNumber++;
                        } else {
                            System.err.println("Đang ở trang cuối cùng!");
                        }
                        break;
                    case 4:
                        System.out.print("Nhập số phần tử trên trang: ");
                        pageSize = Integer.parseInt(scanner.nextLine());
                        if (pageSize < 1) {
                            System.err.println("Số phần tử phải lớn hơn 0.");
                            pageSize = 5; // Quay lại mặc định nếu không hợp lệ
                        }
                        pageNumber = 1; // Reset về trang 1 khi thay đổi pageSize
                        break;
                    case 5:
                        return; // Quay lại menu chính
                    default:
                        System.err.println("Vui lòng chọn từ 1-5");
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập số hợp lệ");
            }
        } while (true);
    }

    private static void searchTechnologyByName(Scanner scanner) {
        System.out.print("Nhập tên công nghệ cần tìm: ");
        String keyword = scanner.nextLine();
        int pageNumber = 1; // Mặc định trang đầu tiên
        int pageSize = 5;  // Mặc định 5 phần tử trên trang
        do {
            int totalPages = technologyService.searchByName(keyword, pageNumber, pageSize);
            if (totalPages == 0) {
                System.err.println("Không tìm thấy công nghệ nào khớp với từ khóa.");
                return;
            }
            System.out.println("***************SEARCH RESULTS (Trang " + pageNumber + "/" + totalPages + ")**************");
            System.out.println("1. Nhập số trang bạn muốn chuyển sang");
            System.out.println("2. Trang trước");
            System.out.println("3. Trang sau");
            System.out.println("4. Thay đổi số phần tử trên một trang");
            System.out.println("5. Quay lại");
            try {
                int searchChoice = Validator.validateChoice(scanner);
                switch (searchChoice) {
                    case 1:
                        System.out.print("Nhập số trang: ");
                        pageNumber = Integer.parseInt(scanner.nextLine());
                        if (pageNumber < 1 || pageNumber > totalPages) {
                            System.err.println("Số trang phải từ 1 đến " + totalPages + ".");
                            pageNumber = 1;
                        }
                        break;
                    case 2:
                        if (pageNumber > 1) {
                            pageNumber--;
                        } else {
                            System.err.println("Đang ở trang đầu tiên!");
                        }
                        break;
                    case 3:
                        if (pageNumber < totalPages) {
                            pageNumber++;
                        } else {
                            System.err.println("Đang ở trang cuối cùng!");
                        }
                        break;
                    case 4:
                        System.out.print("Nhập số phần tử trên trang: ");
                        pageSize = Integer.parseInt(scanner.nextLine());
                        if (pageSize < 1) {
                            System.err.println("Số phần tử phải lớn hơn 0.");
                            pageSize = 5;
                        }
                        pageNumber = 1;
                        break;
                    case 5:
                        return;
                    default:
                        System.err.println("Vui lòng chọn từ 1-5");
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập số hợp lệ");
            }
        } while (true);
    }

    private static void searchTechnologyById(Scanner scanner) {
        System.out.print("Nhập ID công nghệ cần tìm: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Technology technology = technologyService.findById(id);
            if (technology != null) {
                System.out.println("Kết quả tìm kiếm:");
                System.out.println("--------------------------------------------------");
                System.out.println("ID: " + technology.getId() +
                        ", Tên: " + technology.getName() +
                        ", Trạng thái: " + technology.getStatus());
            } else {
                System.err.println("Không tìm thấy công nghệ với ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.err.println("Vui lòng nhập ID là một số hợp lệ");
        }
    }
}