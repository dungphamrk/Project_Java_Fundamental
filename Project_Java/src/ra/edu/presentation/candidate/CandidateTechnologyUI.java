package ra.edu.presentation.candidate;

import ra.edu.business.model.technology.Technology;
import ra.edu.business.service.technology.TechnologyService;
import ra.edu.business.service.technology.TechnologyServiceImp;

import java.util.List;
import java.util.Scanner;

import static ra.edu.presentation.ServiceProvider.technologyService;

public class CandidateTechnologyUI {

    public static void displayMenu(Scanner scanner, int candidateId) {
        do {
            System.out.println("***************MANAGE CANDIDATE TECHNOLOGIES**************");
            System.out.println("1. Xem danh sách công nghệ đã đăng ký");
            System.out.println("2. Thêm công nghệ");
            System.out.println("3. Xóa công nghệ");
            System.out.println("4. Quay lại");
            System.out.print("Lựa chọn của bạn: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        viewCandidateTechnologies(candidateId);
                        break;
                    case 2:
                        addCandidateTechnology(scanner, candidateId);
                        break;
                    case 3:
                        removeCandidateTechnology(scanner, candidateId);
                        break;
                    case 4:
                        return; // Quay lại Candidate Menu
                    default:
                        System.err.println("Vui lòng chọn từ 1-4");
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập số từ 1-4");
            }
        } while (true);
    }

    private static void viewCandidateTechnologies(int candidateId) {
        List<Technology> technologies = technologyService.getCandidateTechnologies(candidateId);
        if (technologies == null || technologies.isEmpty()) {
            System.out.println("Bạn chưa đăng ký công nghệ nào.");
        } else {
            System.out.println("Danh sách công nghệ đã đăng ký:");
            System.out.println("--------------------------------------------------");
            for (Technology tech : technologies) {
                System.out.println("ID: " + tech.getId() +
                        ", Tên: " + tech.getName() +
                        ", Trạng thái: " + tech.getStatus());
            }
        }
    }

    private static void addCandidateTechnology(Scanner scanner, int candidateId) {
         int pageNumber = 1;
        int pageSize = Integer.MAX_VALUE; // Lấy tất cả công nghệ
        List<Technology> technologies = technologyService.findAllTechnologiesByCandidates(pageNumber, pageSize);

        if (technologies == null || technologies.isEmpty()) {
            System.err.println("Không có công nghệ nào đang hoạt động.");
            return;
        }

        // Hiển thị danh sách công nghệ
        System.out.println("Danh sách công nghệ đang hoạt động:");
        System.out.println("--------------------------------------------------");
        for (Technology tech : technologies) {
            System.out.println("ID: " + tech.getId() +
                    ", Tên: " + tech.getName() +
                    ", Trạng thái: " + tech.getStatus());
        }

        System.out.print("Nhập ID công nghệ muốn thêm (từ danh sách trên): ");
        try {
            int technologyId = Integer.parseInt(scanner.nextLine());

            // Kiểm tra xem ID công nghệ có tồn tại và đang active
            Technology technology = technologyService.findById(technologyId);
            if (technology == null || !technology.getStatus().name().equalsIgnoreCase("active")) {
                System.err.println("Công nghệ không tồn tại hoặc không hoạt động!");
                return;
            }

            // Thêm công nghệ cho ứng viên
            int result = technologyService.addCandidateTechnology(candidateId, technologyId);
            if (result == 1) {
                System.out.println("Thêm công nghệ thành công!");
            } else if (result == 2) {
                System.err.println("Không tìm thấy ứng viên!");
            } else if (result == 3) {
                System.err.println("Không tìm thấy công nghệ hoặc công nghệ không hoạt động!");
            } else if (result == 4) {
                System.err.println("Bạn đã đăng ký công nghệ này!");
            } else {
                System.err.println("Thêm công nghệ thất bại!");
            }
        } catch (NumberFormatException e) {
            System.err.println("Vui lòng nhập ID là một số!");
        }
    }
    private static void removeCandidateTechnology(Scanner scanner, int candidateId) {
        viewCandidateTechnologies(candidateId);
        System.out.print("Nhập ID công nghệ muốn xóa: ");
        try {
            int technologyId = Integer.parseInt(scanner.nextLine());
            int result = technologyService.removeCandidateTechnology(candidateId, technologyId);
            if (result == 1) {
                System.out.println("Xóa công nghệ thành công!");
            } else if (result == 2) {
                System.err.println("Bạn chưa đăng ký công nghệ này!");
            } else {
                System.err.println("Xóa công nghệ thất bại!");
            }
        } catch (NumberFormatException e) {
            System.err.println("Vui lòng nhập ID là một số!");
        }
    }
}