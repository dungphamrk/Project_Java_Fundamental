package ra.edu.presentation.admin;

import ra.edu.business.model.technology.Status;
import ra.edu.business.model.technology.Technology;
import ra.edu.business.service.technology.TechnologyService;
import ra.edu.business.service.technology.TechnologyServiceImp;
import ra.edu.validate.Validator;

import java.util.List;
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
                        displayTechnologyList(scanner, 1, 5);
                        break;
                    case 2:
                        Technology newTechnology = new Technology();
                        newTechnology.inputData(scanner);
                        int saveResult = technologyService.save(newTechnology);
                        if (saveResult == 1) {
                            System.out.println("Thêm công nghệ thành công!");
                        } else {
                            System.err.println("Thêm công nghệ thất bại: " + getErrorMessage(saveResult));
                        }
                        break;
                    case 3:
                        updateTechnology(scanner);
                        break;
                    case 4:
                        deleteTechnology(scanner);
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
            List<Technology> technologies = technologyService.findAll(pageNumber, pageSize);
            if (technologies.isEmpty()) {
                System.out.println("Không có công nghệ nào để hiển thị.");
                return;
            }
            System.out.println("Danh sách công nghệ (Trang " + pageNumber + "):");
            System.out.println("--------------------------------------------------");
            for (Technology technology : technologies) {
                System.out.println("ID: " + technology.getId() +
                        ", Tên: " + technology.getName() +
                        ", Trạng thái: " + technology.getStatus());
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
                        int newPage = Integer.parseInt(scanner.nextLine());
                        if (newPage < 1) {
                            System.err.println("Số trang phải lớn hơn 0.");
                            break;
                        }
                        pageNumber = newPage;
                        break;
                    case 2:
                        if (pageNumber > 1) {
                            pageNumber--;
                        } else {
                            System.err.println("Đang ở trang đầu tiên!");
                        }
                        break;
                    case 3:
                        if (!technologies.isEmpty()) {
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

    private static void updateTechnology(Scanner scanner) {
        System.out.print("Nhập ID công nghệ cần sửa: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Technology technology = technologyService.findById(id);
            if (technology == null) {
                System.err.println("Không tìm thấy công nghệ với ID: " + id);
                return;
            }
            System.out.println("Thông tin công nghệ hiện tại:");
            System.out.println("1. Tên: " + technology.getName());
            System.out.println("2. Trạng thái: " + technology.getStatus());
            System.out.print("Chọn trường cần cập nhật (1-2, 0 để thoát): ");
            int fieldChoice = Integer.parseInt(scanner.nextLine());
            if (fieldChoice == 0) {
                return;
            }
            String fieldName;
            Object newValue;
            switch (fieldChoice) {
                case 1:
                    fieldName = "name";
                    System.out.print("Nhập tên mới: ");
                    newValue = scanner.nextLine();
                    break;
                case 2:
                    fieldName = "status";
                    System.out.print("Nhập trạng thái mới (ACTIVE/INACTIVE): ");
                    newValue = scanner.nextLine().toUpperCase();
                    break;
                default:
                    System.err.println("Lựa chọn không hợp lệ!");
                    return;
            }
            int updateResult = technologyService.updateField(id, fieldName, newValue);
            if (updateResult == 0) {
                System.out.println("Cập nhật công nghệ thành công!");
            } else {
                System.err.println("Cập nhật công nghệ thất bại: " + getErrorMessage(updateResult));
            }
        } catch (NumberFormatException e) {
            System.err.println("Vui lòng nhập số hợp lệ");
        }
    }

    private static void deleteTechnology(Scanner scanner) {
        System.out.print("Nhập ID công nghệ cần xóa: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            int deleteResult = technologyService.delete(id);
            if (deleteResult == 1) {
                System.out.println("Xóa công nghệ thành công!");
            } else {
                System.err.println("Xóa công nghệ thất bại: " + getErrorMessage(deleteResult));
            }
        } catch (NumberFormatException e) {
            System.err.println("Vui lòng nhập ID là số hợp lệ");
        }
    }

    private static void searchTechnologyByName(Scanner scanner) {
        System.out.print("Nhập tên công nghệ cần tìm: ");
        String keyword = scanner.nextLine();
        int pageNumber = 1;
        int pageSize = 5;
        do {
            List<Technology> technologies = technologyService.searchByName(keyword, pageNumber, pageSize);
            if (technologies.isEmpty()) {
                System.out.println("Không tìm thấy công nghệ nào khớp với từ khóa: " + keyword);
                return;
            }
            System.out.println("Kết quả tìm kiếm (Trang " + pageNumber + "):");
            System.out.println("--------------------------------------------------");
            for (Technology technology : technologies) {
                System.out.println("ID: " + technology.getId() +
                        ", Tên: " + technology.getName() +
                        ", Trạng thái: " + technology.getStatus());
            }
            System.out.println("***************SEARCH RESULTS**************");
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
                        int newPage = Integer.parseInt(scanner.nextLine());
                        if (newPage < 1) {
                            System.err.println("Số trang phải lớn hơn 0.");
                            break;
                        }
                        pageNumber = newPage;
                        break;
                    case 2:
                        if (pageNumber > 1) {
                            pageNumber--;
                        } else {
                            System.err.println("Đang ở trang đầu tiên!");
                        }
                        break;
                    case 3:
                        if (!technologies.isEmpty()) {
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
            System.err.println("Vui lòng nhập ID là số hợp lệ");
        }
    }

    private static String getErrorMessage(int errorCode) {
        switch (errorCode) {
            case 2:
                return "Tên hoặc giá trị không hợp lệ";
            case 3:
                return "Trường không hợp lệ";
            case 4:
                return "Trạng thái không hợp lệ";
            default:
                return "Lỗi không xác định";
        }
    }
}