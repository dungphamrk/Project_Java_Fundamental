package ra.edu.presentation;

import ra.edu.business.model.application.Application;
import ra.edu.business.model.application.Progress;
import ra.edu.business.model.recruitmentPosition.RecruitmentPosition;
import ra.edu.business.model.technology.Status;
import ra.edu.business.model.technology.Technology; // Thêm import
import ra.edu.business.service.application.ApplicationService;
import ra.edu.business.service.application.ApplicationServiceImp;
import ra.edu.business.service.recruitmentPositionService.RecruitmentPositionService;
import ra.edu.business.service.recruitmentPositionService.RecruitmentPositionServiceImp;
import ra.edu.validate.Validator;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static ra.edu.presentation.ServiceProvider.technologyService;
import static ra.edu.presentation.ServiceProvider.userService;

public class RecruitmentPositionUI {
    private static final RecruitmentPositionService recruitmentPositionService = new RecruitmentPositionServiceImp();
    private static final ApplicationService applicationService = new ApplicationServiceImp();

    public static void displayAdminRecruitmentPositionMenu(Scanner scanner) {
        do {
            System.out.println("***************QUẢN LÝ VỊ TRÍ TUYỂN DỤNG (ADMIN)**************");
            System.out.println("1. Xem danh sách vị trí tuyển dụng");
            System.out.println("2. Thêm vị trí tuyển dụng");
            System.out.println("3. Cập nhật vị trí tuyển dụng");
            System.out.println("4. Xóa vị trí tuyển dụng");
            System.out.println("5. Quay lại");
            System.out.print("Lựa chọn của bạn: ");
            try {
                int choice = Validator.validateChoice(scanner);
                switch (choice) {
                    case 1:
                        displayRecruitmentPositionList(scanner, 1, 5);
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
                        System.err.println("Vui lòng chọn từ 1-5");
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập số từ 1-5");
            }
        } while (true);
    }

    public static void displayCandidateRecruitmentMenu(Scanner scanner) {
        do {
            System.out.println("***************QUẢN LÝ TUYỂN DỤNG (ỨNG VIÊN)**************");
            System.out.println("1. Xem danh sách vị trí công việc đang tuyển");
            System.out.println("2. Quản lý đơn ứng tuyển của bạn");
            System.out.println("3. Quay lại");
            System.out.print("Lựa chọn của bạn: ");
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
                        return;
                    default:
                        System.err.println("Vui lòng chọn từ 1-3");
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập số từ 1-3");
            }
        } while (true);
    }

    private static void displayRecruitmentPositionList(Scanner scanner, int pageNumber, int pageSize) {
        do {
            List<RecruitmentPosition> positions = recruitmentPositionService.getAllPositions(pageNumber, pageSize);
            if (positions.isEmpty()) {
                System.out.println("Không có vị trí tuyển dụng nào để hiển thị.");
                return;
            }
            System.out.println("***************DANH SÁCH VỊ TRÍ TUYỂN DỤNG (Trang " + pageNumber + ")**************");
            for (RecruitmentPosition position : positions) {
                // Hiển thị danh sách công nghệ
                String techList = position.getTechnologies().isEmpty() ? "Không có công nghệ" :
                        position.getTechnologies().stream().map(Technology::getName).reduce((a, b) -> a + ", " + b).get();
                System.out.println("ID: " + position.getId() +
                        ", Tên: " + position.getName() +
                        ", Mô tả: " + position.getDescription() +
                        ", Lương tối thiểu: " + position.getMinSalary() +
                        ", Lương tối đa: " + position.getMaxSalary() +
                        ", Kinh nghiệm tối thiểu: " + position.getMinExperience() +
                        ", Ngày tạo: " + position.getCreatedDate() +
                        ", Ngày hết hạn: " + position.getExpiredDate() +
                        ", Trạng thái: " + position.getStatus() +
                        ", Công nghệ: " + techList);
            }
            System.out.println("***************PAGINATION MENU**************");
            System.out.println("1. Nhập số trang");
            System.out.println("2. Trang trước");
            System.out.println("3. Trang sau");
            System.out.println("4. Quay lại");
            try {
                int choice = Validator.validateChoice(scanner);
                switch (choice) {
                    case 1:
                        System.out.print("Nhập số trang: ");
                        pageNumber = Integer.parseInt(scanner.nextLine());
                        if (pageNumber < 1) {
                            System.err.println("Số trang phải lớn hơn 0.");
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
                        if (!positions.isEmpty()) {
                            pageNumber++;
                        } else {
                            System.err.println("Đang ở trang cuối cùng!");
                        }
                        break;
                    case 4:
                        return;
                    default:
                        System.err.println("Vui lòng chọn từ 1-4");
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập số hợp lệ");
            }
        } while (true);
    }

    private static void addRecruitmentPosition(Scanner scanner) {
        RecruitmentPosition position = new RecruitmentPosition();
        position.inputData(scanner);

        // Hiển thị danh sách công nghệ để người dùng chọn
        List<Technology> availableTechnologies = technologyService.findAllTechnologiesByCandidates(1,10);
        if (availableTechnologies.isEmpty()) {
            System.err.println("Không có công nghệ nào trong hệ thống. Vui lòng thêm công nghệ trước khi tạo vị trí tuyển dụng.");
            return;
        }

        System.out.println("***************CHỌN CÔNG NGHỆ CHO VỊ TRÍ TUYỂN DỤNG**************");
        System.out.println("Danh sách công nghệ hiện có:");
        for (Technology tech : availableTechnologies) {
            System.out.println("ID: " + tech.getId() + ", Tên: " + tech.getName() + ", Trạng thái: " + tech.getStatus());
        }

        boolean continueAdding = true;
        while (continueAdding) {
            System.out.print("Nhập ID công nghệ để thêm (nhập 0 để kết thúc): ");
            try {
                int techId = Integer.parseInt(scanner.nextLine());
                if (techId == 0) {
                    continueAdding = false;
                    continue;
                }
                // Kiểm tra xem công nghệ có tồn tại và active không
                Technology selectedTech = availableTechnologies.stream()
                        .filter(tech -> tech.getId() == techId && tech.getStatus() == Status.ACTIVE)
                        .findFirst()
                        .orElse(null);
                if (selectedTech == null) {
                    System.err.println("Công nghệ không tồn tại hoặc không ở trạng thái active.");
                    continue;
                }
                // Tránh trùng lặp
                if (position.getTechnologies().stream().anyMatch(tech -> tech.getId() == techId)) {
                    System.err.println("Công nghệ này đã được chọn.");
                    continue;
                }
                position.getTechnologies().add(selectedTech);
                System.out.println("Đã thêm công nghệ: " + selectedTech.getName());
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập số hợp lệ");
            }
        }

        // Kiểm tra xem có ít nhất một công nghệ được chọn không
        if (position.getTechnologies().isEmpty()) {
            System.err.println("Phải chọn ít nhất một công nghệ để tạo vị trí tuyển dụng.");
            return;
        }

        // Lưu vị trí tuyển dụng và liên kết với công nghệ
        int result = recruitmentPositionService.save(position);
        if (result == 0) {
            System.out.println("Thêm vị trí tuyển dụng thành công.");
        } else {
            System.err.println("Thêm vị trí tuyển dụng thất bại: " + getErrorMessage(result));
        }
    }

    private static void updateRecruitmentPosition(Scanner scanner) {
        System.out.print("Nhập ID vị trí tuyển dụng cần sửa: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            if (!recruitmentPositionService.isPositionActive(id)) {
                System.err.println("Không tìm thấy vị trí tuyển dụng với ID: " + id);
                return;
            }
            System.out.println("Chọn trường cần cập nhật:");
            System.out.println("1. Tên vị trí");
            System.out.println("2. Mô tả");
            System.out.println("3. Lương tối thiểu");
            System.out.println("4. Lương tối đa");
            System.out.println("5. Kinh nghiệm tối thiểu");
            System.out.println("6. Ngày hết hạn");
            System.out.println("7. Trạng thái (ACTIVE/INACTIVE)");
            System.out.println("8. Quản lý công nghệ (thêm/xóa công nghệ)"); // Thêm tùy chọn mới
            System.out.println("0. Thoát");
            int fieldChoice = Validator.validateChoice(scanner);
            if (fieldChoice == 0) {
                return;
            }

            if (fieldChoice == 8) {
                manageTechnologies(scanner, id);
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
                    fieldName = "description";
                    System.out.print("Nhập mô tả mới: ");
                    newValue = scanner.nextLine();
                    break;
                case 3:
                    fieldName = "minSalary";
                    System.out.print("Nhập lương tối thiểu mới: ");
                    newValue = Double.parseDouble(scanner.nextLine());
                    break;
                case 4:
                    fieldName = "maxSalary";
                    System.out.print("Nhập lương tối đa mới: ");
                    newValue = Double.parseDouble(scanner.nextLine());
                    break;
                case 5:
                    fieldName = "minExperience";
                    System.out.print("Nhập kinh nghiệm tối thiểu mới: ");
                    newValue = Integer.parseInt(scanner.nextLine());
                    break;
                case 6:
                    fieldName = "expiredDate";
                    System.out.print("Nhập ngày hết hạn mới (YYYY-MM-DD): ");
                    newValue = LocalDate.parse(scanner.nextLine());
                    break;
                case 7:
                    fieldName = "status";
                    System.out.print("Nhập trạng thái mới (ACTIVE/INACTIVE): ");
                    newValue = scanner.nextLine().toUpperCase();
                    break;
                default:
                    System.err.println("Lựa chọn không hợp lệ!");
                    return;
            }
            int result = recruitmentPositionService.updateField(id, fieldName, newValue);
            if (result == 0) {
                System.out.println("Cập nhật vị trí tuyển dụng thành công.");
            } else {
                System.err.println("Cập nhật vị trí tuyển dụng thất bại: " + getErrorMessage(result));
            }
        } catch (NumberFormatException e) {
            System.err.println("Vui lòng nhập số hoặc định dạng hợp lệ");
        }
    }

    // Phương thức mới để quản lý công nghệ (thêm/xóa)
    private static void manageTechnologies(Scanner scanner, int positionId) {
        do {
            System.out.println("***************QUẢN LÝ CÔNG NGHỆ CHO VỊ TRÍ TUYỂN DỤNG**************");
            // Hiển thị danh sách công nghệ hiện tại của vị trí
            List<Technology> currentTechnologies = recruitmentPositionService.getPositionTechnologies(positionId);
            System.out.println("Danh sách công nghệ hiện tại của vị trí (ID: " + positionId + "):");
            if (currentTechnologies.isEmpty()) {
                System.out.println("Chưa có công nghệ nào được liên kết.");
            } else {
                for (Technology tech : currentTechnologies) {
                    System.out.println("ID: " + tech.getId() + ", Tên: " + tech.getName() + ", Trạng thái: " + tech.getStatus());
                }
            }

            System.out.println("1. Thêm công nghệ");
            System.out.println("2. Xóa công nghệ");
            System.out.println("3. Quay lại");
            System.out.print("Lựa chọn của bạn: ");
            try {
                int choice = Validator.validateChoice(scanner);
                switch (choice) {
                    case 1:
                        // Hiển thị danh sách tất cả công nghệ
                        List<Technology> availableTechnologies = recruitmentPositionService.getPositionTechnologies(0);
                        if (availableTechnologies.isEmpty()) {
                            System.err.println("Không có công nghệ nào trong hệ thống.");
                            break;
                        }
                        System.out.println("Danh sách công nghệ có sẵn:");
                        for (Technology tech : availableTechnologies) {
                            System.out.println("ID: " + tech.getId() + ", Tên: " + tech.getName() + ", Trạng thái: " + tech.getStatus());
                        }
                        System.out.print("Nhập ID công nghệ để thêm: ");
                        int techIdToAdd = Integer.parseInt(scanner.nextLine());
                        int addResult = recruitmentPositionService.addPositionTechnology(positionId, techIdToAdd);
                        if (addResult == 0) {
                            System.out.println("Thêm công nghệ thành công.");
                        } else {
                            System.err.println("Thêm công nghệ thất bại: " + getErrorMessage(addResult));
                        }
                        break;
                    case 2:
                        if (currentTechnologies.isEmpty()) {
                            System.err.println("Không có công nghệ nào để xóa.");
                            break;
                        }
                        System.out.print("Nhập ID công nghệ để xóa: ");
                        int techIdToRemove = Integer.parseInt(scanner.nextLine());
                        int removeResult = recruitmentPositionService.removePositionTechnology(positionId, techIdToRemove);
                        if (removeResult == 0) {
                            System.out.println("Xóa công nghệ thành công.");
                        } else {
                            System.err.println("Xóa công nghệ thất bại: " + getErrorMessage(removeResult));
                        }
                        break;
                    case 3:
                        return;
                    default:
                        System.err.println("Vui lòng chọn từ 1-3");
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập số hợp lệ");
            }
        } while (true);
    }

    private static void deleteRecruitmentPosition(Scanner scanner) {
        System.out.print("Nhập ID vị trí tuyển dụng cần xóa: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            int result = recruitmentPositionService.delete(id);
            if (result == 0) {
                System.out.println("Xóa vị trí tuyển dụng thành công (chuyển trạng thái sang INACTIVE).");
            } else {
                System.err.println("Xóa vị trí tuyển dụng thất bại: " + getErrorMessage(result));
            }
        } catch (NumberFormatException e) {
            System.err.println("Vui lòng nhập ID là số hợp lệ");
        }
    }

    private static void displayActiveJobListingsForCandidate(Scanner scanner) {
        int pageNumber = 1;
        int pageSize = 10;
        do {
            List<RecruitmentPosition> positions = recruitmentPositionService.getActivePositions(pageNumber, pageSize);
            if (positions.isEmpty()) {
                System.out.println("Không có vị trí công việc nào đang tuyển.");
                return;
            }
            System.out.println("***************DANH SÁCH VỊ TRÍ CÔNG VIỆC ĐANG TUYỂN (Trang " + pageNumber + ")**************");
            for (RecruitmentPosition position : positions) {
                // Hiển thị danh sách công nghệ
                String techList = position.getTechnologies().isEmpty() ? "Không có công nghệ" :
                        position.getTechnologies().stream().map(Technology::getName).reduce((a, b) -> a + ", " + b).get();
                System.out.println("ID: " + position.getId() +
                        ", Tên: " + position.getName() +
                        ", Mô tả: " + position.getDescription() +
                        ", Lương tối thiểu: " + position.getMinSalary() +
                        ", Lương tối đa: " + position.getMaxSalary() +
                        ", Kinh nghiệm tối thiểu: " + position.getMinExperience() +
                        ", Ngày tạo: " + position.getCreatedDate() +
                        ", Ngày hết hạn: " + position.getExpiredDate() +
                        ", Công nghệ: " + techList);
            }
            System.out.println("***************PAGINATION MENU**************");
            System.out.println("1. Nhập số trang");
            System.out.println("2. Trang trước");
            System.out.println("3. Trang sau");
            System.out.println("4. Nhập ID vị trí để nộp đơn");
            System.out.println("5. Quay lại");
            try {
                int choice = Validator.validateChoice(scanner);
                switch (choice) {
                    case 1:
                        System.out.print("Nhập số trang: ");
                        pageNumber = Integer.parseInt(scanner.nextLine());
                        if (pageNumber < 1) {
                            System.err.println("Số trang phải lớn hơn 0.");
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
                        if (!positions.isEmpty()) {
                            pageNumber++;
                        } else {
                            System.err.println("Đang ở trang cuối cùng!");
                        }
                        break;
                    case 4:
                        System.out.print("Nhập ID vị trí: ");
                        int positionId = Integer.parseInt(scanner.nextLine());
                        submitJobApplication(scanner, positionId);
                        return;
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

    private static void submitJobApplication(Scanner scanner, int positionId) {
        System.out.println("***************NỘP ĐƠN ỨNG TUYỂN**************");
        System.out.println("Bạn đang nộp đơn cho vị trí có ID: " + positionId);
        if (!recruitmentPositionService.isPositionActive(positionId)) {
            System.err.println("Vị trí công việc không tồn tại hoặc không ở trạng thái ACTIVE.");
            return;
        }
        int candidateId = userService.getCurrentUserId();
        if (candidateId == 0) {
            System.err.println("Bạn cần đăng nhập để nộp đơn.");
            return;
        }
        Application application = new Application();
        application.setRecruitmentPositionId(positionId);
        application.setCandidateId(candidateId);
        System.out.print("Nhập URL CV của bạn: ");
        application.setCvUrl(scanner.nextLine());
        application.setProgress(Progress.PENDING);
        int result = applicationService.save(application);
        if (result == 0) {
            System.out.println("Nộp đơn ứng tuyển thành công!");
        } else {
            System.err.println("Nộp đơn ứng tuyển thất bại.");
        }
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
}