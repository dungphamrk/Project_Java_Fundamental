package ra.edu.presentation.admin;

import ra.edu.business.model.technology.Technology;
import ra.edu.business.service.technology.TechnologyService;
import ra.edu.business.service.technology.TechnologyServiceImp;
import ra.edu.validate.Validator;

import java.util.List;
import java.util.Scanner;

public class TechnologyUI {
    // Mã màu ANSI sáng
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[91m";
    private static final String GREEN = "\u001B[92m";
    private static final String YELLOW = "\u001B[93m";
    private static final String CYAN = "\u001B[96m";
    private static final String MAGENTA = "\u001B[95m";
    private static final String WHITE = "\u001B[97m";

    private static final TechnologyService technologyService = new TechnologyServiceImp();

    public static void displayTechnologyMenu(Scanner scanner) {
        do {
            System.out.println(MAGENTA + "=============================================");
            System.out.println("         QUẢN LÝ CÔNG NGHỆ                 ");
            System.out.println("=============================================" + RESET);
            System.out.println(CYAN + "1. Xem danh sách công nghệ");
            System.out.println("2. Tìm kiếm công nghệ theo tên");
            System.out.println("3. Tìm kiếm công nghệ theo ID");
            System.out.println("4. Thêm công nghệ");
            System.out.println("5. Cập nhật công nghệ");
            System.out.println("6. Xóa công nghệ");
            System.out.println("7. Quay lại" + RESET);
            System.out.print(MAGENTA + "Lựa chọn của bạn: " + RESET);
            try {
                int choice = Validator.validateChoice(scanner);
                switch (choice) {
                    case 1:
                        displayTechnologyList(scanner);
                        break;
                    case 2:
                        searchTechnologyByName(scanner);
                        break;
                    case 3:
                        searchTechnologyById(scanner);
                        break;
                    case 4:
                        addTechnology(scanner);
                        break;
                    case 5:
                        updateTechnology(scanner);
                        break;
                    case 6:
                        deleteTechnology(scanner);
                        break;
                    case 7:
                        return;
                    default:
                        System.out.println(RED + "Vui lòng chọn từ 1-7" + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Vui lòng nhập số từ 1-7" + RESET);
            }
            System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
            scanner.nextLine();
        } while (true);
    }

    private static void displayTechnologyList(Scanner scanner) {
        int pageNumber = 1;
        int pageSize = 5;
        int totalRecords = technologyService.getTotalTechnologiesCount();
        List<Technology> technologies = technologyService.findAllTechnologiesByCandidates(pageNumber, pageSize);
        displayPaginationMenu(scanner, pageNumber, pageSize, totalRecords, technologies, "DANH SÁCH CÔNG NGHỆ");
    }

    private static void searchTechnologyByName(Scanner scanner) {
        System.out.println(MAGENTA + "=== TÌM KIẾM CÔNG NGHỆ THEO TÊN ===" + RESET);
        System.out.print(WHITE + "Nhập tên công nghệ: " + RESET);
        String name = scanner.nextLine();
        int pageNumber = 1;
        int pageSize = 5;
        int totalRecords = technologyService.getTotalTechnologiesByName(name);
        List<Technology> technologies = technologyService.searchByName(name, pageNumber, pageSize);
        displayPaginationMenu(scanner, pageNumber, pageSize, totalRecords, technologies, "KẾT QUẢ TÌM KIẾM (Tên: " + name + ")");
    }

    private static void displayPaginationMenu(Scanner scanner, int currentPage, int pageSize, int totalRecords, List<Technology> technologies, String title) {
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        do {
            printTechnologyTable(technologies, title);
            System.out.println(YELLOW + "Trang: " + currentPage + "/" + totalPages + " | Số phần tử trên trang: " + pageSize + RESET);
            System.out.println(MAGENTA + "=== MENU PHÂN TRANG ===");
            System.out.println("1. Trang trước");
            System.out.println("2. Trang sau");
            System.out.println("3. Thay đổi số phần tử trên trang");
            System.out.println("4. Chọn trang");
            System.out.println("5. Quay lại");
            System.out.print(MAGENTA + "Lựa chọn của bạn: " + RESET);
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1: // Trang trước
                        if (currentPage > 1) {
                            currentPage--;
                            if (title.startsWith("DANH SÁCH CÔNG NGHỆ")) {
                                technologies = technologyService.findAllTechnologiesByCandidates(currentPage, pageSize);
                            } else if (title.startsWith("KẾT QUẢ TÌM KIẾM")) {
                                String name = title.split("Tên: ")[1].replace(")", "");
                                technologies = technologyService.searchByName(name, currentPage, pageSize);
                            }
                        } else {
                            System.out.println(RED + "Đây là trang đầu tiên!" + RESET);
                        }
                        break;
                    case 2: // Trang sau
                        if (currentPage < totalPages) {
                            currentPage++;
                            if (title.startsWith("DANH SÁCH CÔNG NGHỆ")) {
                                technologies = technologyService.findAllTechnologiesByCandidates(currentPage, pageSize);
                            } else if (title.startsWith("KẾT QUẢ TÌM KIẾM")) {
                                String name = title.split("Tên: ")[1].replace(")", "");
                                technologies = technologyService.searchByName(name, currentPage, pageSize);
                            }
                        } else {
                            System.out.println(RED + "Đây là trang cuối cùng!" + RESET);
                        }
                        break;
                    case 3: // Thay đổi số phần tử trên trang
                        System.out.print(WHITE + "Nhập số phần tử trên trang: " + RESET);
                        try {
                            int newPageSize = Integer.parseInt(scanner.nextLine());
                            if (newPageSize > 0) {
                                pageSize = newPageSize;
                                currentPage = 1; // Reset về trang 1
                                if (title.startsWith("DANH SÁCH CÔNG NGHỆ")) {
                                    technologies = technologyService.findAllTechnologiesByCandidates(currentPage, pageSize);
                                } else if (title.startsWith("KẾT QUẢ TÌM KIẾM")) {
                                    String name = title.split("Tên: ")[1].replace(")", "");
                                    technologies = technologyService.searchByName(name, currentPage, pageSize);
                                }
                                totalPages = (int) Math.ceil((double) totalRecords / pageSize);
                            } else {
                                System.out.println(RED + "Số phần tử phải lớn hơn 0!" + RESET);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(RED + "Vui lòng nhập số hợp lệ!" + RESET);
                        }
                        break;
                    case 4: // Chọn trang
                        System.out.print(WHITE + "Nhập số trang muốn tới: " + RESET);
                        try {
                            int newPage = Integer.parseInt(scanner.nextLine());
                            if (newPage >= 1 && newPage <= totalPages) {
                                currentPage = newPage;
                                if (title.startsWith("DANH SÁCH CÔNG NGHỆ")) {
                                    technologies = technologyService.findAllTechnologiesByCandidates(currentPage, pageSize);
                                } else if (title.startsWith("KẾT QUẢ TÌM KIẾM")) {
                                    String name = title.split("Tên: ")[1].replace(")", "");
                                    technologies = technologyService.searchByName(name, currentPage, pageSize);
                                }
                            } else {
                                System.out.println(RED + "Trang không hợp lệ!" + RESET);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(RED + "Vui lòng nhập số hợp lệ!" + RESET);
                        }
                        break;
                    case 5: // Quay lại
                        return;
                    default:
                        System.out.println(RED + "Vui lòng chọn từ 1-5" + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Vui lòng nhập số từ 1-5" + RESET);
            }
        } while (true);
    }

    private static void printTechnologyTable(List<Technology> technologies, String title) {
        System.out.println(MAGENTA + "=== " + title + " ===" + RESET);
        if (technologies == null || technologies.isEmpty()) {
            System.out.println(RED + "Không có công nghệ nào để hiển thị." + RESET);
        } else {
            System.out.println(YELLOW + "+-----+--------------------+--------+" + RESET);
            System.out.println("| ID  | Tên công nghệ      | Trạng thái |");
            System.out.println(YELLOW + "+-----+--------------------+--------+" + RESET);
            for (Technology tech : technologies) {
                System.out.printf(WHITE + "| %-3d | %-18s | %-10s |%n",
                        tech.getId(),
                        tech.getName().length() > 18 ? tech.getName().substring(0, 15) + "..." : tech.getName(),
                        tech.getStatus());
            }
            System.out.println(YELLOW + "+-----+--------------------+--------+" + RESET);
        }
    }

    private static void searchTechnologyById(Scanner scanner) {
        System.out.println(MAGENTA + "=== TÌM KIẾM CÔNG NGHỆ THEO ID ===" + RESET);
        System.out.print(WHITE + "Nhập ID công nghệ: " + RESET);
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Technology technology = technologyService.findById(id);
            if (technology == null) {
                System.out.println(RED + "Không tìm thấy công nghệ với ID: " + id + RESET);
            } else {
                System.out.println(MAGENTA + "=== THÔNG TIN CÔNG NGHỆ ===" + RESET);
                System.out.println(YELLOW + "+-----+--------------------+--------+" + RESET);
                System.out.println("| ID  | Tên công nghệ      | Trạng thái |");
                System.out.println(YELLOW + "+-----+--------------------+--------+" + RESET);
                System.out.printf(WHITE + "| %-3d | %-18s | %-10s |%n",
                        technology.getId(),
                        technology.getName().length() > 18 ? technology.getName().substring(0, 15) + "..." : technology.getName(),
                        technology.getStatus());
                System.out.println(YELLOW + "+-----+--------------------+--------+" + RESET);
            }
        } catch (NumberFormatException e) {
            System.out.println(RED + "Vui lòng nhập ID là một số!" + RESET);
        }
        System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
        scanner.nextLine();
    }

    private static void addTechnology(Scanner scanner) {
        System.out.println(MAGENTA + "=== THÊM CÔNG NGHỆ ===" + RESET);
        Technology technology = new Technology();
        technology.inputData(scanner);
        int result = technologyService.save(technology);
        if (result == 0) {
            System.out.println(GREEN + "Thêm công nghệ thành công!" + RESET);
        } else {
            System.out.println(RED + "Thêm công nghệ thất bại!" + RESET);
        }
        System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
        scanner.nextLine();
    }

    private static void updateTechnology(Scanner scanner) {
        System.out.println(MAGENTA + "=== CẬP NHẬT CÔNG NGHỆ ===" + RESET);
        System.out.print(WHITE + "Nhập ID công nghệ cần cập nhật: " + RESET);
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Technology technology = technologyService.findById(id);
            if (technology == null) {
                System.out.println(RED + "Không tìm thấy công nghệ với ID: " + id + RESET);
                System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
                scanner.nextLine();
                return;
            }
            System.out.println(MAGENTA + "=== Chọn trường cần cập nhật ===" + RESET);
            System.out.println(CYAN + "1. Tên công nghệ");
            System.out.println("2. Trạng thái (ACTIVE/INACTIVE)");
            System.out.println("3. Quay lại" + RESET);
            System.out.print(MAGENTA + "Lựa chọn của bạn: " + RESET);
            int fieldChoice = Integer.parseInt(scanner.nextLine());
            String fieldName = "";
            String newValue;
            switch (fieldChoice) {
                case 1:
                    fieldName = "name";
                    System.out.print(WHITE + "Nhập tên mới: " + RESET);
                    newValue = scanner.nextLine();
                    break;
                case 2:
                    fieldName = "status";
                    System.out.print(WHITE + "Nhập trạng thái mới (ACTIVE/INACTIVE): " + RESET);
                    newValue = scanner.nextLine();
                    break;
                case 3:
                    return;
                default:
                    System.out.println(RED + "Vui lòng chọn từ 1-3" + RESET);
                    System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
                    scanner.nextLine();
                    return;
            }
            int result = technologyService.updateField(id, fieldName, newValue);
            if (result == 0) {
                System.out.println(GREEN + "Cập nhật công nghệ thành công!" + RESET);
            } else {
                System.out.println(RED + "Cập nhật công nghệ thất bại!" + RESET);
            }
        } catch (NumberFormatException e) {
            System.out.println(RED + "Vui lòng nhập ID hoặc lựa chọn là một số!" + RESET);
        }
        System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
        scanner.nextLine();
    }

    private static void deleteTechnology(Scanner scanner) {
        System.out.println(MAGENTA + "=== XÓA CÔNG NGHỆ ===" + RESET);
        System.out.print(WHITE + "Nhập ID công nghệ cần xóa: " + RESET);
        try {
            int id = Integer.parseInt(scanner.nextLine());
            int result = technologyService.delete(id);
            if (result == 0) {
                System.out.println(GREEN + "Xóa công nghệ thành công!" + RESET);
            } else {
                System.out.println(RED + "Xóa công nghệ thất bại!" + RESET);
            }
        } catch (NumberFormatException e) {
            System.out.println(RED + "Vui lòng nhập ID là một số!" + RESET);
        }
        System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
        scanner.nextLine();
    }
}