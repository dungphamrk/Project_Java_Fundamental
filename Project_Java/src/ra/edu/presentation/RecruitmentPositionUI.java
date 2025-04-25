package ra.edu.presentation;

import ra.edu.business.model.application.Application;
import ra.edu.business.model.application.Progress;
import ra.edu.business.model.recruitmentPosition.RecruitmentPosition;
import ra.edu.business.model.technology.Status;
import ra.edu.business.model.technology.Technology;
import ra.edu.business.service.application.ApplicationService;
import ra.edu.business.service.application.ApplicationServiceImp;
import ra.edu.business.service.recruitmentPositionService.RecruitmentPositionService;
import ra.edu.business.service.recruitmentPositionService.RecruitmentPositionServiceImp;
import ra.edu.business.service.technology.TechnologyService;
import ra.edu.validate.Validator;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static ra.edu.presentation.ServiceProvider.technologyService;
import static ra.edu.presentation.ServiceProvider.userService;

public class RecruitmentPositionUI {
    // Mã màu ANSI sáng
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[91m";
    private static final String GREEN = "\u001B[92m";
    private static final String YELLOW = "\u001B[93m";
    private static final String CYAN = "\u001B[96m";
    private static final String MAGENTA = "\u001B[95m";
    private static final String WHITE = "\u001B[97m";

    private static final RecruitmentPositionService recruitmentPositionService = new RecruitmentPositionServiceImp();
    private static final ApplicationService applicationService = new ApplicationServiceImp();

    public static void displayAdminRecruitmentPositionMenu(Scanner scanner) {
        do {
            System.out.println(MAGENTA + "=============================================");
            System.out.println("   QUẢN LÝ VỊ TRÍ TUYỂN DỤNG (ADMIN)       ");
            System.out.println("=============================================" + RESET);
            System.out.println(CYAN + "1. Xem danh sách vị trí tuyển dụng");
            System.out.println("2. Thêm vị trí tuyển dụng");
            System.out.println("3. Cập nhật vị trí tuyển dụng");
            System.out.println("4. Xóa vị trí tuyển dụng");
            System.out.println("5. Quay lại" + RESET);
            System.out.print(MAGENTA + "Lựa chọn của bạn: " + RESET);
            try {
                int choice = Validator.validateChoice(scanner);
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
                        return;
                    default:
                        System.out.println(RED + "Vui lòng chọn từ 1-5" + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Vui lòng nhập số từ 1-5" + RESET);
            }
            System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
            scanner.nextLine();
        } while (true);
    }

    public static void displayCandidateRecruitmentMenu(Scanner scanner) {
        do {
            System.out.println(MAGENTA + "=============================================");
            System.out.println("     QUẢN LÝ TUYỂN DỤNG (ỨNG VIÊN)         ");
            System.out.println("=============================================" + RESET);
            System.out.println(CYAN + "1. Xem danh sách vị trí công việc đang tuyển");
            System.out.println("2. Quản lý đơn ứng tuyển của bạn");
            System.out.println("3. Lọc vị trí theo công nghệ"); // Thêm mục mới
            System.out.println("4. Quay lại" + RESET);
            System.out.print(MAGENTA + "Lựa chọn của bạn: " + RESET);
            try {
                int choice = Validator.validateChoice(scanner);
                switch (choice) {
                    case 1:
                        displayActiveJobListingsForCandidate(scanner);
                        break;
                    case 2:
                        ApplicationUI.displayCandidateApplicationMenu(scanner);
                        break;
                    case 3:
                        filterJobsByTechnologies(scanner); // Gọi phương thức mới
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println(RED + "Vui lòng chọn từ 1-4" + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Vui lòng nhập số từ 1-4" + RESET);
            }
            System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
            scanner.nextLine();
        } while (true);
    }
    private static void displayRecruitmentPositionList(Scanner scanner) {
        int pageNumber = 1;
        int pageSize = 5;
        int totalRecords = recruitmentPositionService.getTotalPositionsCount();
        List<RecruitmentPosition> positions = recruitmentPositionService.getAllPositions(pageNumber, pageSize);
        displayPaginationMenu(scanner, pageNumber, pageSize, totalRecords, positions, false);
    }

    private static void displayActiveJobListingsForCandidate(Scanner scanner) {
        int pageNumber = 1;
        int pageSize = 10;
        int totalRecords = recruitmentPositionService.getActivePositionsCount();
        List<RecruitmentPosition> positions = recruitmentPositionService.getActivePositions(pageNumber, pageSize);
        displayPaginationMenu(scanner, pageNumber, pageSize, totalRecords, positions, true);
    }

    private static void displayPaginationMenu(Scanner scanner, int currentPage, int pageSize, int totalRecords, List<RecruitmentPosition> positions, boolean isCandidate) {
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        do {
            printPositionTable(positions, isCandidate);
            System.out.println(YELLOW + "Trang: " + currentPage + "/" + totalPages + " | Số phần tử trên trang: " + pageSize + RESET);
            System.out.println(MAGENTA + "=== MENU PHÂN TRANG ===");
            System.out.println("1. Trang trước");
            System.out.println("2. Trang sau");
            System.out.println("3. Thay đổi số phần tử trên trang");
            System.out.println("4. Chọn trang");
            if (isCandidate) {
                System.out.println("5. Nhập ID vị trí để nộp đơn");
                System.out.println("6. Quay lại");
            } else {
                System.out.println("5. Quay lại");
            }
            System.out.print(MAGENTA + "Lựa chọn của bạn: " + RESET);
            try {
                int choice = Validator.validateChoice(scanner);
                if (isCandidate) {
                    switch (choice) {
                        case 1: // Trang trước
                            if (currentPage > 1) {
                                currentPage--;
                                positions = recruitmentPositionService.getActivePositions(currentPage, pageSize);
                            } else {
                                System.out.println(RED + "Đây là trang đầu tiên!" + RESET);
                            }
                            break;
                        case 2: // Trang sau
                            if (currentPage < totalPages) {
                                currentPage++;
                                positions = recruitmentPositionService.getActivePositions(currentPage, pageSize);
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
                                    positions = recruitmentPositionService.getActivePositions(currentPage, pageSize);
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
                                    positions = recruitmentPositionService.getActivePositions(currentPage, pageSize);
                                } else {
                                    System.out.println(RED + "Trang không hợp lệ!" + RESET);
                                }
                            } catch (NumberFormatException e) {
                                System.out.println(RED + "Vui lòng nhập số hợp lệ!" + RESET);
                            }
                            break;
                        case 5: // Nộp đơn
                            System.out.print(WHITE + "Nhập ID vị trí: " + RESET);
                            try {
                                int positionId = Integer.parseInt(scanner.nextLine());
                                submitJobApplication(scanner, positionId);
                                return;
                            } catch (NumberFormatException e) {
                                System.out.println(RED + "Vui lòng nhập ID là một số!" + RESET);
                            }
                            break;
                        case 6: // Quay lại
                            return;
                        default:
                            System.out.println(RED + "Vui lòng chọn từ 1-6" + RESET);
                    }
                } else {
                    switch (choice) {
                        case 1: // Trang trước
                            if (currentPage > 1) {
                                currentPage--;
                                positions = recruitmentPositionService.getAllPositions(currentPage, pageSize);
                            } else {
                                System.out.println(RED + "Đây là trang đầu tiên!" + RESET);
                            }
                            break;
                        case 2: // Trang sau
                            if (currentPage < totalPages) {
                                currentPage++;
                                positions = recruitmentPositionService.getAllPositions(currentPage, pageSize);
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
                                    positions = recruitmentPositionService.getAllPositions(currentPage, pageSize);
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
                                    positions = recruitmentPositionService.getAllPositions(currentPage, pageSize);
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
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Vui lòng nhập số từ 1-" + (isCandidate ? "6" : "5") + RESET);
            }
        } while (true);
    }

    private static void printPositionTable(List<RecruitmentPosition> positions, boolean isCandidate) {
        System.out.println(MAGENTA + "=== DANH SÁCH VỊ TRÍ " + (isCandidate ? "CÔNG VIỆC ĐANG TUYỂN" : "TUYỂN DỤNG") + " ===" + RESET);
        if (positions == null || positions.isEmpty()) {
            System.out.println(RED + (isCandidate ? "Không có vị trí công việc nào đang tuyển." : "Không có vị trí tuyển dụng nào để hiển thị.") + RESET);
        } else {
            if (isCandidate) {
                System.out.println(YELLOW + "+-----+--------------------+--------------------+-------------+-------------+" + RESET);
                System.out.println("| ID  | Tên vị trí         | Mô tả              | Lương tối đa| Kinh nghiệm |");
                System.out.println("+-----+--------------------+--------------------+-------------+-------------+" + RESET);
                for (RecruitmentPosition position : positions) {
                    System.out.printf(WHITE + "| %-3d | %-18s | %-18s | %-11.0f | %-11d |%n",
                            position.getId(),
                            position.getName().length() > 18 ? position.getName().substring(0, 15) + "..." : position.getName(),
                            position.getDescription().length() > 18 ? position.getDescription().substring(0, 15) + "..." : position.getDescription(),
                            position.getMaxSalary(),
                            position.getMinExperience());
                }
                System.out.println(YELLOW + "+-----+--------------------+--------------------+-------------+-------------+" + RESET);
            } else {
                System.out.println(YELLOW + "+-----+--------------------+--------------------+-------------+-------------+--------+" + RESET);
                System.out.println("| ID  | Tên vị trí         | Mô tả              | Lương tối đa| Kinh nghiệm | Trạng thái |");
                System.out.println("+-----+--------------------+--------------------+-------------+-------------+--------+" + RESET);
                for (RecruitmentPosition position : positions) {
                    System.out.printf(WHITE + "| %-3d | %-18s | %-18s | %-11.0f | %-11d | %-10s |%n",
                            position.getId(),
                            position.getName().length() > 18 ? position.getName().substring(0, 15) + "..." : position.getName(),
                            position.getDescription().length() > 18 ? position.getDescription().substring(0, 15) + "..." : position.getDescription(),
                            position.getMaxSalary(),
                            position.getMinExperience(),
                            position.getStatus());
                }
                System.out.println(YELLOW + "+-----+--------------------+--------------------+-------------+-------------+--------+" + RESET);
            }
        }
    }

    private static void addRecruitmentPosition(Scanner scanner) {
        System.out.println(MAGENTA + "=== THÊM VỊ TRÍ TUYỂN DỤNG ===" + RESET);
        RecruitmentPosition position = new RecruitmentPosition();
        position.inputData(scanner);

        List<Technology> availableTechnologies = technologyService.findAllTechnologiesByCandidates(1, 10);
        if (availableTechnologies.isEmpty()) {
            System.out.println(RED + "Không có công nghệ nào trong hệ thống. Vui lòng thêm công nghệ trước." + RESET);
            System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
            scanner.nextLine();
            return;
        }

        System.out.println(MAGENTA + "=== CHỌN CÔNG NGHỆ ===" + RESET);
        System.out.println(YELLOW + "+-----+--------------------+--------+" + RESET);
        System.out.println("| ID  | Tên công nghệ      | Trạng thái |");
        System.out.println(YELLOW + "+-----+--------------------+--------+" + RESET);
        for (Technology tech : availableTechnologies) {
            System.out.printf(WHITE + "| %-3d | %-18s | %-10s |%n",
                    tech.getId(), tech.getName(), tech.getStatus());
        }
        System.out.println(YELLOW + "+-----+--------------------+--------+" + RESET);

        boolean continueAdding = true;
        while (continueAdding) {
            System.out.print(WHITE + "Nhập ID công nghệ để thêm (nhập 0 để kết thúc): " + RESET);
            try {
                int techId = Integer.parseInt(scanner.nextLine());
                if (techId == 0) {
                    continueAdding = false;
                    continue;
                }
                Technology selectedTech = availableTechnologies.stream()
                        .filter(tech -> tech.getId() == techId && tech.getStatus() == Status.ACTIVE)
                        .findFirst()
                        .orElse(null);
                if (selectedTech == null) {
                    System.out.println(RED + "Công nghệ không tồn tại hoặc không ở trạng thái active." + RESET);
                    continue;
                }
                if (position.getTechnologies().stream().anyMatch(tech -> tech.getId() == techId)) {
                    System.out.println(RED + "Công nghệ này đã được chọn." + RESET);
                    continue;
                }
                position.getTechnologies().add(selectedTech);
                System.out.println(GREEN + "Đã thêm công nghệ: " + selectedTech.getName() + RESET);
            } catch (NumberFormatException e) {
                System.out.println(RED + "Vui lòng nhập số hợp lệ" + RESET);
            }
        }

        if (position.getTechnologies().isEmpty()) {
            System.out.println(RED + "Phải chọn ít nhất một công nghệ để tạo vị trí tuyển dụng." + RESET);
            System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
            scanner.nextLine();
            return;
        }

        int result = recruitmentPositionService.save(position);
        if (result == 0) {
            System.out.println(GREEN + "Thêm vị trí tuyển dụng thành công." + RESET);
        } else {
            System.out.println(RED + "Thêm vị trí tuyển dụng thất bại: " + getErrorMessage(result) + RESET);
        }
        System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
        scanner.nextLine();
    }

    private static void updateRecruitmentPosition(Scanner scanner) {
        System.out.println(MAGENTA + "=== CẬP NHẬT VỊ TRÍ TUYỂN DỤNG ===" + RESET);
        System.out.print(WHITE + "Nhập ID vị trí tuyển dụng cần sửa: " + RESET);
        try {
            int id = Integer.parseInt(scanner.nextLine());
            if (!recruitmentPositionService.isPositionActive(id)) {
                System.out.println(RED + "Không tìm thấy vị trí tuyển dụng với ID: " + id + RESET);
                System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
                scanner.nextLine();
                return;
            }
            System.out.println(MAGENTA + "=== Chọn trường cần cập nhật ===" + RESET);
            System.out.println(CYAN + "1. Tên vị trí");
            System.out.println("2. Mô tả");
            System.out.println("3. Lương tối thiểu");
            System.out.println("4. Lương tối đa");
            System.out.println("5. Kinh nghiệm tối thiểu");
            System.out.println("6. Ngày hết hạn");
            System.out.println("7. Trạng thái (ACTIVE/INACTIVE)");
            System.out.println("8. Quản lý công nghệ (thêm/xóa công nghệ)");
            System.out.println("0. Thoát" + RESET);
            int fieldChoice = Validator.validateChoice(scanner);
            if (fieldChoice == 0) {
                System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
                scanner.nextLine();
                return;
            }

            if (fieldChoice == 8) {
                manageTechnologies(scanner, id);
                return;
            }

            String fieldName;
            Object newValue;
            System.out.print(WHITE + "Nhập giá trị mới: " + RESET);
            switch (fieldChoice) {
                case 1:
                    fieldName = "name";
                    newValue = scanner.nextLine();
                    break;
                case 2:
                    fieldName = "description";
                    newValue = scanner.nextLine();
                    break;
                case 3:
                    fieldName = "minSalary";
                    newValue = Double.parseDouble(scanner.nextLine());
                    break;
                case 4:
                    fieldName = "maxSalary";
                    newValue = Double.parseDouble(scanner.nextLine());
                    break;
                case 5:
                    fieldName = "minExperience";
                    newValue = Integer.parseInt(scanner.nextLine());
                    break;
                case 6:
                    fieldName = "expiredDate";
                    newValue = LocalDate.parse(scanner.nextLine());
                    break;
                case 7:
                    fieldName = "status";
                    newValue = scanner.nextLine().toUpperCase();
                    break;
                default:
                    System.out.println(RED + "Lựa chọn không hợp lệ!" + RESET);
                    System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
                    scanner.nextLine();
                    return;
            }
            int result = recruitmentPositionService.updateField(id, fieldName, newValue);
            if (result == 0) {
                System.out.println(GREEN + "Cập nhật vị trí tuyển dụng thành công." + RESET);
            } else {
                System.out.println(RED + "Cập nhật vị trí tuyển dụng thất bại: " + getErrorMessage(result) + RESET);
            }
        } catch (NumberFormatException e) {
            System.out.println(RED + "Vui lòng nhập số hoặc định dạng hợp lệ" + RESET);
        }
        System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
        scanner.nextLine();
    }

    private static void manageTechnologies(Scanner scanner, int positionId) {
        do {
            System.out.println(MAGENTA + "=============================================");
            System.out.println("   QUẢN LÝ CÔNG NGHỆ CHO VỊ TRÍ TUYỂN DỤNG  ");
            System.out.println("=============================================" + RESET);
            List<Technology> currentTechnologies = recruitmentPositionService.getPositionTechnologies(positionId);
            System.out.println(MAGENTA + "Danh sách công nghệ hiện tại của vị trí (ID: " + positionId + "):" + RESET);
            if (currentTechnologies.isEmpty()) {
                System.out.println(RED + "Chưa có công nghệ nào được liên kết." + RESET);
            } else {
                System.out.println(YELLOW + "+-----+--------------------+--------+" + RESET);
                System.out.println("| ID  | Tên công nghệ      | Trạng thái |");
                System.out.println(YELLOW + "+-----+--------------------+--------+" + RESET);
                for (Technology tech : currentTechnologies) {
                    System.out.printf(WHITE + "| %-3d | %-18s | %-10s |%n",
                            tech.getId(), tech.getName(), tech.getStatus());
                }
                System.out.println(YELLOW + "+-----+--------------------+--------+" + RESET);
            }

            System.out.println(CYAN + "1. Thêm công nghệ");
            System.out.println("2. Xóa công nghệ");
            System.out.println("3. Quay lại" + RESET);
            System.out.print(MAGENTA + "Lựa chọn của bạn: " + RESET);
            try {
                int choice = Validator.validateChoice(scanner);
                switch (choice) {
                    case 1:
                        List<Technology> availableTechnologies = technologyService.findAllTechnologiesByCandidates(1, 10);
                        if (availableTechnologies.isEmpty()) {
                            System.out.println(RED + "Không có công nghệ nào trong hệ thống." + RESET);
                            System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
                            scanner.nextLine();
                            break;
                        }
                        System.out.println(MAGENTA + "Danh sách công nghệ có sẵn:" + RESET);
                        System.out.println(YELLOW + "+-----+--------------------+--------+" + RESET);
                        System.out.println("| ID  | Tên công nghệ      | Trạng thái |");
                        System.out.println(YELLOW + "+-----+--------------------+--------+" + RESET);
                        for (Technology tech : availableTechnologies) {
                            System.out.printf(WHITE + "| %-3d | %-18s | %-10s |%n",
                                    tech.getId(), tech.getName(), tech.getStatus());
                        }
                        System.out.println(YELLOW + "+-----+--------------------+--------+" + RESET);
                        System.out.print(WHITE + "Nhập ID công nghệ để thêm: " + RESET);
                        int techIdToAdd = Integer.parseInt(scanner.nextLine());
                        int addResult = recruitmentPositionService.addPositionTechnology(positionId, techIdToAdd);
                        if (addResult == 0) {
                            System.out.println(GREEN + "Thêm công nghệ thành công." + RESET);
                        } else {
                            System.out.println(RED + "Thêm công nghệ thất bại: " + getErrorMessage(addResult) + RESET);
                        }
                        break;
                    case 2:
                        if (currentTechnologies.isEmpty()) {
                            System.out.println(RED + "Không có công nghệ nào để xóa." + RESET);
                            System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
                            scanner.nextLine();
                            break;
                        }
                        System.out.print(WHITE + "Nhập ID công nghệ để xóa: " + RESET);
                        int techIdToRemove = Integer.parseInt(scanner.nextLine());
                        int removeResult = recruitmentPositionService.removePositionTechnology(positionId, techIdToRemove);
                        if (removeResult == 0) {
                            System.out.println(GREEN + "Xóa công nghệ thành công." + RESET);
                        } else {
                            System.out.println(RED + "Xóa công nghệ thất bại: " + getErrorMessage(removeResult) + RESET);
                        }
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println(RED + "Vui lòng chọn từ 1-3" + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Vui lòng nhập số hợp lệ" + RESET);
            }
            System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
            scanner.nextLine();
        } while (true);
    }

    private static void deleteRecruitmentPosition(Scanner scanner) {
        System.out.println(MAGENTA + "=== XÓA VỊ TRÍ TUYỂN DỤNG ===" + RESET);
        System.out.print(WHITE + "Nhập ID vị trí tuyển dụng cần xóa: " + RESET);
        try {
            int id = Integer.parseInt(scanner.nextLine());
            int result = recruitmentPositionService.delete(id);
            if (result == 0) {
                System.out.println(GREEN + "Xóa vị trí tuyển dụng thành công (chuyển trạng thái sang INACTIVE)." + RESET);
            } else {
                System.out.println(RED + "Xóa vị trí tuyển dụng thất bại: " + getErrorMessage(result) + RESET);
            }
        } catch (NumberFormatException e) {
            System.out.println(RED + "Vui lòng nhập ID là số hợp lệ" + RESET);
        }
        System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
        scanner.nextLine();
    }

    private static void submitJobApplication(Scanner scanner, int positionId) {
        System.out.println(MAGENTA + "=== NỘP ĐƠN ỨNG TUYỂN ===" + RESET);
        System.out.println(WHITE + "Bạn đang nộp đơn cho vị trí có ID: " + positionId + RESET);
        if (!recruitmentPositionService.isPositionActive(positionId)) {
            System.out.println(RED + "Vị trí công việc không tồn tại hoặc không ở trạng thái ACTIVE." + RESET);
            System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
            scanner.nextLine();
            return;
        }
        int candidateId = userService.getCurrentUserId();
        if (candidateId == 0) {
            System.out.println(RED + "Bạn cần đăng nhập để nộp đơn." + RESET);
            System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
            scanner.nextLine();
            return;
        }
        Application application = new Application();
        application.setRecruitmentPositionId(positionId);
        application.setCandidateId(candidateId);
        System.out.print(WHITE + "Nhập URL CV của bạn: " + RESET);
        application.setCvUrl(scanner.nextLine());
        application.setProgress(Progress.PENDING);
        int result = applicationService.save(application);
        if (result == 0) {
            System.out.println(GREEN + "Nộp đơn ứng tuyển thành công!" + RESET);
        } else {
            System.out.println(RED + "Nộp đơn ứng tuyển thất bại." + RESET);
        }
        System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
        scanner.nextLine();
    }

    private static String getErrorMessage(int errorCode) {
        switch (errorCode) {
            case 1:
                return "ID hoặc tên không hợp lệ";
            case 2:
                return "Giá trị hoặc lương không hợp lệ";
            case 3:
                return "Trường hoặc kinh nghiệm không hợp lệ";
            case 4:
                return "Lương không hợp lệ";
            case 5:
                return "Kinh nghiệm không hợp lệ";
            case 6:
                return "Trạng thái không hợp lệ";
            case 7:
                return "Ngày hết hạn không hợp lệ";
            default:
                return "Lỗi không xác định";
        }
    }

    private static void filterJobsByTechnologies(Scanner scanner) {
        int pageNumber = 1;
        int pageSize = 10;

        // Lấy danh sách công nghệ đang hoạt động
        TechnologyService.TechnologyPage techPage = technologyService.findActiveTechnologiesWithCount(pageNumber, pageSize);
        List<Technology> technologies = techPage.getTechnologies();
        int totalTechRecords = techPage.getTotalRecords();

        if (technologies.isEmpty()) {
            System.out.println(RED + "Không có công nghệ nào đang hoạt động để lọc." + RESET);
            return;
        }

        // Hiển thị danh sách công nghệ
        System.out.println(MAGENTA + "=== DANH SÁCH CÔNG NGHỆ ĐANG HOẠT ĐỘNG ===" + RESET);
        System.out.println(YELLOW + "+-----+--------------------+" + RESET);
        System.out.println("| ID  | Tên công nghệ      |" + RESET);
        System.out.println(YELLOW + "+-----+--------------------+" + RESET);
        for (Technology tech : technologies) {
            System.out.printf(WHITE + "| %-3d | %-18s |%n",
                    tech.getId(), tech.getName().length() > 18 ? tech.getName().substring(0, 15) + "..." : tech.getName());
        }
        System.out.println(YELLOW + "+-----+--------------------+" + RESET);

        // Cho phép người dùng chọn nhiều công nghệ
        List<Integer> selectedTechIds = new ArrayList<>();
        System.out.println(MAGENTA + "Nhập ID công nghệ để lọc (nhập 0 để kết thúc):" + RESET);
        while (true) {
            System.out.print(WHITE + "ID công nghệ: " + RESET);
            try {
                int techId = Integer.parseInt(scanner.nextLine());
                if (techId == 0) {
                    break;
                }
                if (technologies.stream().noneMatch(tech -> tech.getId() == techId)) {
                    System.out.println(RED + "ID công nghệ không hợp lệ!" + RESET);
                    continue;
                }
                if (selectedTechIds.contains(techId)) {
                    System.out.println(RED + "Công nghệ này đã được chọn!" + RESET);
                    continue;
                }
                selectedTechIds.add(techId);
                System.out.println(GREEN + "Đã chọn công nghệ ID: " + techId + RESET);
            } catch (NumberFormatException e) {
                System.out.println(RED + "Vui lòng nhập số hợp lệ!" + RESET);
            }
        }

        if (selectedTechIds.isEmpty()) {
            System.out.println(RED + "Bạn chưa chọn công nghệ nào để lọc." + RESET);
            return;
        }

        // Lọc các vị trí tuyển dụng dựa trên danh sách công nghệ đã chọn
        int totalRecords = recruitmentPositionService.getFilteredPositionsCountByTechnologies(selectedTechIds);
        List<RecruitmentPosition> filteredPositions = recruitmentPositionService.getFilteredPositionsByTechnologies(
                selectedTechIds, pageNumber, pageSize);

        // Hiển thị kết quả với phân trang
        displayPaginationMenu(scanner, pageNumber, pageSize, totalRecords, filteredPositions, true);
    }
}