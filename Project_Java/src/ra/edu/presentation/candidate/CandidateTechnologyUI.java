package ra.edu.presentation.candidate;

import ra.edu.business.model.technology.Technology;
import ra.edu.business.service.technology.TechnologyService;
import ra.edu.business.service.technology.TechnologyServiceImp;

import java.util.List;
import java.util.Scanner;

import static ra.edu.presentation.ServiceProvider.technologyService;

public class CandidateTechnologyUI {
    // Mã màu ANSI sáng
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[91m";
    private static final String GREEN = "\u001B[92m";
    private static final String YELLOW = "\u001B[93m";
    private static final String CYAN = "\u001B[96m";
    private static final String MAGENTA = "\u001B[95m";
    private static final String WHITE = "\u001B[97m";

    private static final TechnologyService technologyServiceImp = new TechnologyServiceImp();

    public static void displayMenu(Scanner scanner, int candidateId) {
        do {
            System.out.println(MAGENTA + "=============================================");
            System.out.println("     QUẢN LÝ CÔNG NGHỆ ỨNG VIÊN            ");
            System.out.println("=============================================" + RESET);
            System.out.println(CYAN + "1. Danh sách công nghệ đã đăng ký");
            System.out.println("2. Xem danh sách công nghệ đang hoạt động");
            System.out.println("3. Quay lại" + RESET);
            System.out.print(MAGENTA + "Lựa chọn của bạn: " + RESET);
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        viewCandidateTechnologies(scanner, candidateId);
                        break;
                    case 2:
                        viewActiveTechnologies(scanner, candidateId);
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println(RED + "Vui lòng chọn từ 1-3" + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Vui lòng nhập số từ 1-3" + RESET);
            }
            System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
            scanner.nextLine();
        } while (true);
    }

    private static void displayPaginationMenu(Scanner scanner, int currentPage, int pageSize, int totalRecords, List<Technology> technologies, int candidateId, boolean isCandidateTech) {
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        do {
            printTechnologyTable(technologies, isCandidateTech);
            System.out.println(YELLOW + "Trang: " + currentPage + "/" + totalPages + " | Số phần tử trên trang: " + pageSize + RESET);
            System.out.println(MAGENTA + "=== MENU PHÂN TRANG ===");
            System.out.println("1. Trang trước");
            System.out.println("2. Trang sau");
            System.out.println("3. Thay đổi số phần tử trên trang");
            System.out.println("4. Chọn trang");
            if (isCandidateTech) {
                System.out.println("5. Xóa công nghệ");
            } else {
                System.out.println("5. Thêm công nghệ");
            }
            System.out.println("6. Quay lại" + RESET);
            System.out.print(MAGENTA + "Lựa chọn của bạn: " + RESET);
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1: // Trang trước
                        if (currentPage > 1) {
                            currentPage--;
                            if (isCandidateTech) {
                                TechnologyService.TechnologyPage techPage = technologyService.getCandidateTechnologiesWithCount(candidateId, currentPage, pageSize);
                                technologies = techPage.getTechnologies();
                                totalRecords = techPage.getTotalRecords();
                            } else {
                                TechnologyService.TechnologyPage techPage = technologyService.findActiveTechnologiesWithCount(currentPage, pageSize);
                                technologies = techPage.getTechnologies();
                                totalRecords = techPage.getTotalRecords();
                            }
                        } else {
                            System.out.println(RED + "Đây là trang đầu tiên!" + RESET);
                        }
                        break;
                    case 2: // Trang sau
                        if (currentPage < totalPages) {
                            currentPage++;
                            if (isCandidateTech) {
                                TechnologyService.TechnologyPage techPage = technologyService.getCandidateTechnologiesWithCount(candidateId, currentPage, pageSize);
                                technologies = techPage.getTechnologies();
                                totalRecords = techPage.getTotalRecords();
                            } else {
                                TechnologyService.TechnologyPage techPage = technologyService.findActiveTechnologiesWithCount(currentPage, pageSize);
                                technologies = techPage.getTechnologies();
                                totalRecords = techPage.getTotalRecords();
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
                                if (isCandidateTech) {
                                    TechnologyService.TechnologyPage techPage = technologyService.getCandidateTechnologiesWithCount(candidateId, currentPage, pageSize);
                                    technologies = techPage.getTechnologies();
                                    totalRecords = techPage.getTotalRecords();
                                } else {
                                    TechnologyService.TechnologyPage techPage = technologyService.findActiveTechnologiesWithCount(currentPage, pageSize);
                                    technologies = techPage.getTechnologies();
                                    totalRecords = techPage.getTotalRecords();
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
                                if (isCandidateTech) {
                                    TechnologyService.TechnologyPage techPage = technologyService.getCandidateTechnologiesWithCount(candidateId, currentPage, pageSize);
                                    technologies = techPage.getTechnologies();
                                    totalRecords = techPage.getTotalRecords();
                                } else {
                                    TechnologyService.TechnologyPage techPage = technologyService.findActiveTechnologiesWithCount(currentPage, pageSize);
                                    technologies = techPage.getTechnologies();
                                    totalRecords = techPage.getTotalRecords();
                                }
                            } else {
                                System.out.println(RED + "Trang không hợp lệ!" + RESET);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(RED + "Vui lòng nhập số hợp lệ!" + RESET);
                        }
                        break;
                    case 5: // Xóa hoặc Thêm công nghệ
                        if (isCandidateTech) {
                            removeCandidateTechnology(scanner, candidateId, technologies);
                            TechnologyService.TechnologyPage techPage = technologyService.getCandidateTechnologiesWithCount(candidateId, currentPage, pageSize);
                            technologies = techPage.getTechnologies();
                            totalRecords = techPage.getTotalRecords();
                        } else {
                            addCandidateTechnology(scanner, candidateId, technologies);
                            TechnologyService.TechnologyPage techPage = technologyService.findActiveTechnologiesWithCount(currentPage, pageSize);
                            technologies = techPage.getTechnologies();
                            totalRecords = techPage.getTotalRecords();
                        }
                        totalPages = (int) Math.ceil((double) totalRecords / pageSize);
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println(RED + "Vui lòng chọn từ 1-6" + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Vui lòng nhập số từ 1-6" + RESET);
            }
        } while (totalPages!=0);
    }

    private static void printTechnologyTable(List<Technology> technologies, boolean isCandidateTech) {
        System.out.println(MAGENTA + "=== DANH SÁCH CÔNG NGHỆ " + (isCandidateTech ? "ĐÃ ĐĂNG KÝ" : "ĐANG HOẠT ĐỘNG") + " ===" + RESET);
        if (technologies == null || technologies.isEmpty()) {
            System.out.println(RED + (isCandidateTech ? "Bạn chưa đăng ký công nghệ nào." : "Không có công nghệ nào đang hoạt động.") + RESET);
        } else {
            System.out.println(YELLOW + "+-----+--------------------+--------+" + RESET);
            System.out.println("| ID  | Tên công nghệ      | Trạng thái |");
            System.out.println(YELLOW + "+-----+--------------------+--------+" + RESET);
            for (Technology tech : technologies) {
                System.out.printf(WHITE + "| %-3d | %-18s | %-10s |%n",
                        tech.getId(), tech.getName(), tech.getStatus());
            }
            System.out.println(YELLOW + "+-----+--------------------+--------+" + RESET);
        }
    }

    private static void viewCandidateTechnologies(Scanner scanner, int candidateId) {
        int pageNumber = 1;
        int pageSize = 5;
        TechnologyService.TechnologyPage techPage = technologyService.getCandidateTechnologiesWithCount(candidateId, pageNumber, pageSize);
        List<Technology> technologies = techPage.getTechnologies();
        int totalRecords = techPage.getTotalRecords();
        if(totalRecords!=0){
            displayPaginationMenu(scanner, pageNumber, pageSize, totalRecords, technologies, candidateId, true);
        }else {
            System.out.println(RED + "Bạn chưa có liên kết công nghệ nào cả"+ RESET);
        }
    }

    private static void viewActiveTechnologies(Scanner scanner, int candidateId) {
        int pageNumber = 1;
        int pageSize = 5;
        TechnologyService.TechnologyPage techPage = technologyService.findActiveTechnologiesWithCount(pageNumber, pageSize);
        List<Technology> technologies = techPage.getTechnologies();
        int totalRecords = techPage.getTotalRecords();
        if(totalRecords!=0){
            displayPaginationMenu(scanner, pageNumber, pageSize, totalRecords, technologies, candidateId, false);
        }else {
            System.out.println(RED + "Không có công nghệ nào đang được tuyển dụng." + RESET);
        }
    }

    private static void addCandidateTechnology(Scanner scanner, int candidateId, List<Technology> technologies) {
        System.out.println(MAGENTA + "=== THÊM CÔNG NGHỆ ===" + RESET);
        System.out.print(WHITE + "Nhập ID công nghệ muốn thêm: " + RESET);
        try {
            int technologyId = Integer.parseInt(scanner.nextLine());
            Technology technology = technologyService.findById(technologyId);
            if (technology == null || !technology.getStatus().name().equalsIgnoreCase("ACTIVE")) {
                System.out.println(RED + "Công nghệ không tồn tại hoặc không hoạt động!" + RESET);
                return;
            }
            int result = technologyService.addCandidateTechnology(candidateId, technologyId);
            if (result == 1) {
                System.out.println(GREEN + "Thêm công nghệ thành công!" + RESET);
            } else if (result == 2) {
                System.out.println(RED + "Không tìm thấy ứng viên!" + RESET);
            } else if (result == 3) {
                System.out.println(RED + "Không tìm thấy công nghệ hoặc công nghệ không hoạt động!" + RESET);
            } else if (result == 4) {
                System.out.println(RED + "Bạn đã đăng ký công nghệ này!" + RESET);
            } else {
                System.out.println(RED + "Thêm công nghệ thất bại!" + RESET);
            }
        } catch (NumberFormatException e) {
            System.out.println(RED + "Vui lòng nhập ID là một số!" + RESET);
        }
    }

    private static void removeCandidateTechnology(Scanner scanner, int candidateId, List<Technology> technologies) {
        System.out.println(MAGENTA + "=== XÓA CÔNG NGHỆ ===" + RESET);
        System.out.print(WHITE + "Nhập ID công nghệ muốn xóa: " + RESET);
        try {
            int technologyId = Integer.parseInt(scanner.nextLine());
            Technology technology = technologyService.findById(technologyId);
            if (technology == null) {
                System.out.println(RED + "Công nghệ không tồn tại!" + RESET);
                return;
            }
            System.out.print(YELLOW + "Bạn có chắc chắn muốn xóa công nghệ '" + technology.getName() + "'? (y/n): " + RESET);
            String confirmation = scanner.nextLine().trim().toLowerCase();
            if (!confirmation.equals("y")) {
                System.out.println(GREEN + "Hủy xóa công nghệ." + RESET);
                return;
            }
            int result = technologyService.removeCandidateTechnology(candidateId, technologyId);
            if (result == 1) {
                System.out.println(GREEN + "Xóa công nghệ thành công!" + RESET);
            } else if (result == 2) {
                System.out.println(RED + "Không tìm thấy ứng viên!" + RESET);
            } else if (result == 3) {
                System.out.println(RED + "Không tìm thấy công nghệ!" + RESET);
            } else if (result == 4) {
                System.out.println(RED + "Bạn chưa đăng ký công nghệ này!" + RESET);
            } else {
                System.out.println(RED + "Xóa công nghệ thất bại!" + RESET);
            }
        } catch (NumberFormatException e) {
            System.out.println(RED + "Vui lòng nhập ID là một số!" + RESET);
        }
    }
}