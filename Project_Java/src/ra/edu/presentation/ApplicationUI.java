package ra.edu.presentation;

import ra.edu.business.model.application.Application;
import ra.edu.business.model.application.Progress;
import ra.edu.business.service.application.ApplicationService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import static ra.edu.presentation.ServiceProvider.applicationService;
import static ra.edu.presentation.ServiceProvider.userService;

public class ApplicationUI {
    // Mã màu ANSI sáng
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[91m";
    private static final String GREEN = "\u001B[92m";
    private static final String YELLOW = "\u001B[93m";
    private static final String CYAN = "\u001B[96m";
    private static final String MAGENTA = "\u001B[95m";
    private static final String WHITE = "\u001B[97m";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void displayCandidateApplicationMenu(Scanner scanner) {
        do {
            System.out.println(MAGENTA + "=============================================");
            System.out.println("    QUẢN LÝ ĐƠN ỨNG TUYỂN CỦA BẠN           ");
            System.out.println("=============================================" + RESET);
            System.out.println(CYAN + "1. Xem danh sách đơn ứng tuyển");
            System.out.println("2. Hủy đơn ứng tuyển");
            System.out.println("3. Quay lại" + RESET);
            System.out.print(MAGENTA + "Lựa chọn của bạn: " + RESET);
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        displayCandidateApplications(scanner);
                        break;
                    case 2:
                        cancelApplication(scanner);
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

    public static void displayCandidateApplications(Scanner scanner) {
        int candidateId = userService.getCurrentUserId();
        if (candidateId == 0) {
            System.out.println(RED + "Bạn cần đăng nhập để xem đơn ứng tuyển." + RESET);
            System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
            scanner.nextLine();
            return;
        }

        int pageNumber = 1;
        int pageSize = 5;

        while (true) {
            try {
                List<Application> applications = applicationService.getApplicationsByCandidateId(candidateId, pageNumber, pageSize);
                if (applications.isEmpty()) {
                    System.out.println(RED + "Bạn chưa có đơn ứng tuyển nào." + RESET);
                    System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
                    scanner.nextLine();
                    return;
                }

                System.out.println(MAGENTA + "=============================================");
                System.out.println("       DANH SÁCH ĐƠN ỨNG TUYỂN             ");
                System.out.println("=============================================" + RESET);
                System.out.println("Trang " + pageNumber + " - Số phần tử trên trang: " + pageSize);
                System.out.println(YELLOW + "+--------+------------+--------------------------------+-------------+");
                System.out.println("| ID  | ID vị trí công việc  | URL CV                         | Trạng thái  |");
                System.out.println("+--------+------------+--------------------------------+-------------+" + RESET);
                for (Application app : applications) {
                    System.out.printf(WHITE + "| %-6d | %-15d | %-40s | %-11s |%n",
                            app.getId(), app.getRecruitmentPositionId(),
                            app.getCvUrl().length() > 30 ? app.getCvUrl().substring(0, 27) + "..." : app.getCvUrl(),
                            app.getProgress());
                }
                System.out.println(YELLOW + "+--------+------------+--------------------------------+-------------+" + RESET);

                System.out.println(MAGENTA + "=== TÙY CHỌN ===" + RESET);
                System.out.println(CYAN + "1. Trang tiếp theo");
                System.out.println("2. Trang trước");
                System.out.println("3. Thay đổi số phần tử trên trang");
                System.out.println("4. Quay lại" + RESET);
                System.out.print(MAGENTA + "Lựa chọn của bạn: " + RESET);
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        pageNumber++;
                        break;
                    case "2":
                        if (pageNumber > 1) pageNumber--;
                        break;
                    case "3":
                        System.out.print(WHITE + "Nhập số phần tử trên trang: " + RESET);
                        try {
                            pageSize = Integer.parseInt(scanner.nextLine());
                            if (pageSize < 1) {
                                System.out.println(RED + "Số phần tử phải lớn hơn 0." + RESET);
                                pageSize = 5;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(RED + "Vui lòng nhập số hợp lệ." + RESET);
                        }
                        break;
                    case "4":
                        return;
                    default:
                        System.out.println(RED + "Vui lòng chọn từ 1-4" + RESET);
                }
            } catch (RuntimeException e) {
                System.out.println(RED + "Lỗi khi lấy danh sách đơn ứng tuyển: " + e.getMessage() + RESET);
                System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
                scanner.nextLine();
                return;
            }
        }
    }

    public static void cancelApplication(Scanner scanner) {
        System.out.println(MAGENTA + "=== HỦY ĐƠN ỨNG TUYỂN ===" + RESET);
        int candidateId = userService.getCurrentUserId();
        if (candidateId == 0) {
            System.out.println(RED + "Bạn cần đăng nhập để hủy đơn ứng tuyển." + RESET);
            System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
            scanner.nextLine();
            return;
        }

        System.out.print(WHITE + "Nhập ID đơn ứng tuyển cần hủy: " + RESET);
        try {
            int applicationId = Integer.parseInt(scanner.nextLine());
            Application app = applicationService.findById(applicationId);
            if (app == null) {
                System.out.println(RED + "Đơn ứng tuyển không tồn tại." + RESET);
                System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
                scanner.nextLine();
                return;
            }
            if (app.getProgress() == Progress.REJECT || app.getProgress() == Progress.CANCEL) {
                System.out.println(RED + "Đơn ứng tuyển đã bị hủy hoặc từ chối, không thể hủy lại." + RESET);
                System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
                scanner.nextLine();
                return;
            }
            System.out.print(WHITE + "Nhập lý do hủy: " + RESET);
            String destroyReason = scanner.nextLine();

            int result = applicationService.cancelApplication(applicationId, candidateId, destroyReason, Progress.CANCEL);
            if (result == 0) {
                System.out.println(GREEN + "Hủy đơn ứng tuyển thành công." + RESET);
            } else {
                System.out.println(RED + "Hủy đơn ứng tuyển thất bại. Đơn không tồn tại hoặc không thuộc về bạn." + RESET);
            }
        } catch (NumberFormatException e) {
            System.out.println(RED + "ID đơn phải là số nguyên." + RESET);
        } catch (IllegalArgumentException e) {
            System.out.println(RED + "Lỗi: " + e.getMessage() + RESET);
        } catch (RuntimeException e) {
            System.out.println(RED + "Lỗi khi hủy đơn ứng tuyển: " + e.getMessage() + RESET);
        }
        System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
        scanner.nextLine();
    }

    public static void displayAdminApplicationMenu(Scanner scanner) {
        do {
            System.out.println(MAGENTA + "=============================================");
            System.out.println("       QUẢN LÝ ĐƠN ỨNG TUYỂN               ");
            System.out.println("=============================================" + RESET);
            System.out.println(CYAN + "1. Xem danh sách tất cả đơn ứng tuyển");
            System.out.println("2. Lọc đơn theo trạng thái");
            System.out.println("3. Quay lại" + RESET);
            System.out.print(MAGENTA + "Lựa chọn của bạn: " + RESET);
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        displayAllApplications(scanner);
                        break;
                    case 2:
                        filterApplicationsByProgress(scanner);
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

    public static void displayAllApplications(Scanner scanner) {
        int pageNumber = 1;
        int pageSize = 5;

        while (true) {
            try {
                List<Application> applications = applicationService.findAll(pageNumber, pageSize);
                if (applications.isEmpty()) {
                    System.out.println(RED + "Chưa có đơn ứng tuyển nào." + RESET);
                    System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
                    scanner.nextLine();
                    return;
                }

                System.out.println(MAGENTA + "=============================================");
                System.out.println("       DANH SÁCH ĐƠN ỨNG TUYỂN             ");
                System.out.println("=============================================" + RESET);
                System.out.println("Trang " + pageNumber + " - Số phần tử trên trang: " + pageSize);
                System.out.println(YELLOW + "+--------+------------+--------------------------------+-------------+");
                System.out.println("| ID đơn | Vị trí ID  | URL CV                         | Trạng thái  |");
                System.out.println("+--------+------------+--------------------------------+-------------+" + RESET);
                for (Application app : applications) {
                    System.out.printf(WHITE + "| %-6d | %-10d | %-30s | %-11s |%n",
                            app.getId(), app.getRecruitmentPositionId(),
                            app.getCvUrl().length() > 30 ? app.getCvUrl().substring(0, 27) + "..." : app.getCvUrl(),
                            app.getProgress());
                }
                System.out.println(YELLOW + "+--------+------------+--------------------------------+-------------+" + RESET);

                System.out.println(MAGENTA + "=== TÙY CHỌN ===" + RESET);
                System.out.println(CYAN + "1. Trang tiếp theo");
                System.out.println("2. Trang trước");
                System.out.println("3. Thay đổi số phần tử trên trang");
                System.out.println("4. Xem chi tiết đơn");
                System.out.println("5. Quay lại" + RESET);
                System.out.print(MAGENTA + "Lựa chọn của bạn: " + RESET);
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        pageNumber++;
                        break;
                    case "2":
                        if (pageNumber > 1) pageNumber--;
                        break;
                    case "3":
                        System.out.print(WHITE + "Nhập số phần tử trên trang: " + RESET);
                        try {
                            pageSize = Integer.parseInt(scanner.nextLine());
                            if (pageSize < 1) {
                                System.out.println(RED + "Số phần tử phải lớn hơn 0." + RESET);
                                pageSize = 5;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(RED + "Vui lòng nhập số hợp lệ." + RESET);
                        }
                        break;
                    case "4":
                        viewApplicationDetails(scanner);
                        break;
                    case "5":
                        return;
                    default:
                        System.out.println(RED + "Vui lòng chọn từ 1-5" + RESET);
                }
            } catch (RuntimeException e) {
                System.out.println(RED + "Lỗi khi lấy danh sách đơn ứng tuyển: " + e.getMessage() + RESET);
                System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
                scanner.nextLine();
                return;
            }
        }
    }

    public static void filterApplicationsByProgress(Scanner scanner) {
        System.out.println(MAGENTA + "=== CHỌN TRẠNG THÁI ĐỂ LỌC ===" + RESET);
        Progress[] progresses = Progress.values();
        for (int i = 0; i < progresses.length; i++) {
            System.out.println(CYAN + (i + 1) + ". " + progresses[i] + RESET);
        }
        System.out.print(MAGENTA + "Lựa chọn của bạn: " + RESET);
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice < 1 || choice > progresses.length) {
                System.out.println(RED + "Vui lòng chọn trạng thái hợp lệ." + RESET);
                System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
                scanner.nextLine();
                return;
            }
            Progress selectedProgress = progresses[choice - 1];
            displayFilteredApplications(scanner, selectedProgress);
        } catch (NumberFormatException e) {
            System.out.println(RED + "Vui lòng nhập số hợp lệ." + RESET);
            System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
            scanner.nextLine();
        }
    }

    public static void displayFilteredApplications(Scanner scanner, Progress progress) {
        int pageNumber = 1;
        int pageSize = 5;

        while (true) {
            try {
                List<Application> applications = applicationService.findByProgress(progress, pageNumber, pageSize);
                if (applications.isEmpty()) {
                    System.out.println(RED + "Chưa có đơn ứng tuyển nào với trạng thái " + progress + RESET);
                    System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
                    scanner.nextLine();
                    return;
                }

                System.out.println(MAGENTA + "=============================================");
                System.out.println("  DANH SÁCH ĐƠN ỨNG TUYỂN - TRẠNG THÁI: " + progress + "  ");
                System.out.println("=============================================" + RESET);
                System.out.println("Trang " + pageNumber + " - Số phần tử trên trang: " + pageSize);
                System.out.println(YELLOW + "+--------+------------+--------------------------------+-------------+");
                System.out.println("| ID đơn | Vị trí ID  | URL CV                         | Trạng thái  |");
                System.out.println("+--------+------------+--------------------------------+-------------+" + RESET);
                for (Application app : applications) {
                    System.out.printf(WHITE + "| %-6d | %-10d | %-30s | %-11s |%n",
                            app.getId(), app.getRecruitmentPositionId(),
                            app.getCvUrl().length() > 30 ? app.getCvUrl().substring(0, 27) + "..." : app.getCvUrl(),
                            app.getProgress());
                }
                System.out.println(YELLOW + "+--------+------------+--------------------------------+-------------+" + RESET);

                System.out.println(MAGENTA + "=== TÙY CHỌN ===" + RESET);
                System.out.println(CYAN + "1. Trang tiếp theo");
                System.out.println("2. Trang trước");
                System.out.println("3. Thay đổi số phần tử trên trang");
                System.out.println("4. Xem chi tiết đơn");
                System.out.println("5. Quay lại" + RESET);
                System.out.print(MAGENTA + "Lựa chọn của bạn: " + RESET);
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        pageNumber++;
                        break;
                    case "2":
                        if (pageNumber > 1) pageNumber--;
                        break;
                    case "3":
                        System.out.print(WHITE + "Nhập số phần tử trên trang: " + RESET);
                        try {
                            pageSize = Integer.parseInt(scanner.nextLine());
                            if (pageSize < 1) {
                                System.out.println(RED + "Số phần tử phải lớn hơn 0." + RESET);
                                pageSize = 5;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(RED + "Vui lòng nhập số hợp lệ." + RESET);
                        }
                        break;
                    case "4":
                        viewApplicationDetails(scanner);
                        break;
                    case "5":
                        return;
                    default:
                        System.out.println(RED + "Vui lòng chọn từ 1-5" + RESET);
                }
            } catch (RuntimeException e) {
                System.out.println(RED + "Lỗi khi lấy danh sách đơn ứng tuyển: " + e.getMessage() + RESET);
                System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
                scanner.nextLine();
                return;
            }
        }
    }

    public static void viewApplicationDetails(Scanner scanner) {
        System.out.println(MAGENTA + "=== XEM CHI TIẾT ĐƠN ỨNG TUYỂN ===" + RESET);
        System.out.print(WHITE + "Nhập ID đơn ứng tuyển cần xem chi tiết: " + RESET);
        try {
            int applicationId = Integer.parseInt(scanner.nextLine());
            Application app = applicationService.findById(applicationId);
            if (app == null) {
                System.out.println(RED + "Đơn ứng tuyển không tồn tại." + RESET);
                System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
                scanner.nextLine();
                return;
            }

            if (app.getProgress() != Progress.REJECT && app.getProgress() != Progress.CANCEL) {
                applicationService.updateField(applicationId, "progress", Progress.HANDLING);
            }

            System.out.println(MAGENTA + "=== CHI TIẾT ĐƠN ỨNG TUYỂN ===" + RESET);
            System.out.println(YELLOW + "+---------------------------+-----------------------------+");
            System.out.println("| Trường                    | Giá trị                     |");
            System.out.println("+---------------------------+-----------------------------+" + RESET);
            System.out.printf(WHITE + "| %-25s | %-27s |%n", "ID đơn", app.getId());
            System.out.printf("| %-25s | %-27s |%n", "ID ứng viên", app.getCandidateId());
            System.out.printf("| %-25s | %-27s |%n", "ID vị trí tuyển dụng", app.getRecruitmentPositionId());
            System.out.printf("| %-25s | %-27s |%n", "URL CV", app.getCvUrl());
            System.out.printf("| %-25s | %-27s |%n", "Trạng thái", app.getProgress());
            System.out.printf("| %-25s | %-27s |%n", "Ngày yêu cầu phỏng vấn",
                    app.getInterviewRequestDate() != null ? app.getInterviewRequestDate().format(formatter) : "N/A");
            System.out.printf("| %-25s | %-27s |%n", "Kết quả yêu cầu phỏng vấn",
                    app.getInterviewRequestResult() != null ? app.getInterviewRequestResult() : "N/A");
            System.out.printf("| %-25s | %-27s |%n", "Link phỏng vấn",
                    app.getInterviewLink() != null ? app.getInterviewLink() : "N/A");
            System.out.printf("| %-25s | %-27s |%n", "Thời gian phỏng vấn",
                    app.getInterviewTime() != null ? app.getInterviewTime().format(formatter) : "N/A");
            System.out.printf("| %-25s | %-27s |%n", "Kết quả phỏng vấn",
                    app.getInterviewResult() != null ? app.getInterviewResult() : "N/A");
            System.out.printf("| %-25s | %-27s |%n", "Ghi chú kết quả phỏng vấn",
                    app.getInterviewResultNote() != null ? app.getInterviewResultNote() : "N/A");
            System.out.printf("| %-25s | %-27s |%n", "Ngày hủy",
                    app.getDestroyAt() != null ? app.getDestroyAt().format(formatter) : "N/A");
            System.out.printf("| %-25s | %-27s |%n", "Lý do hủy",
                    app.getDestroyReason() != null ? app.getDestroyReason() : "N/A");
            System.out.printf("| %-25s | %-27s |%n", "Ngày tạo",
                    app.getCreateAt() != null ? app.getCreateAt().format(formatter) : "N/A");
            System.out.printf("| %-25s | %-27s |%n", "Ngày cập nhật",
                    app.getUpdateAt() != null ? app.getUpdateAt().format(formatter) : "N/A");
            System.out.println(YELLOW + "+---------------------------+-----------------------------+" + RESET);

            System.out.println(MAGENTA + "=== TÙY CHỌN ===" + RESET);
            if (app.getProgress() == Progress.REJECT || app.getProgress() == Progress.CANCEL) {
                System.out.println(RED + "Đơn ứng tuyển đã bị hủy hoặc từ chối, không thể chỉnh sửa." + RESET);
                System.out.println(CYAN + "1. Quay lại" + RESET);
                System.out.print(MAGENTA + "Lựa chọn của bạn: " + RESET);
                String choice = scanner.nextLine();
                if (!choice.equals("1")) {
                    System.out.println(RED + "Vui lòng chọn 1 để quay lại." + RESET);
                }
                System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
                scanner.nextLine();
                return;
            } else {
                System.out.println(CYAN + "1. Hủy đơn ứng tuyển");
                System.out.println("2. Gửi yêu cầu phỏng vấn");
                System.out.println("3. Gửi link phỏng vấn và thời gian");
                System.out.println("4. Cập nhật kết quả phỏng vấn và trạng thái");
                System.out.println("5. Quay lại" + RESET);
                System.out.print(MAGENTA + "Lựa chọn của bạn: " + RESET);
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        cancelApplicationByAdmin(scanner, applicationId);
                        break;
                    case 2:
                        System.out.print(WHITE + "Nhập kết quả yêu cầu phỏng vấn (ví dụ: Đã gửi, Chưa gửi): " + RESET);
                        String interviewRequestResult = scanner.nextLine();
                        LocalDateTime interviewRequestDate = LocalDateTime.now();
                        int result1 = applicationService.updateInterviewRequest(applicationId, interviewRequestDate, interviewRequestResult);
                        if (result1 == 0) {
                            System.out.println(GREEN + "Cập nhật yêu cầu phỏng vấn thành công." + RESET);
                        } else {
                            System.out.println(RED + "Cập nhật thất bại. Đơn không tồn tại hoặc đã bị hủy." + RESET);
                        }
                        break;
                    case 3:
                        System.out.print(WHITE + "Nhập link phỏng vấn: " + RESET);
                        String interviewLink = scanner.nextLine();
                        System.out.print(WHITE + "Nhập thời gian phỏng vấn (định dạng yyyy-MM-dd HH:mm:ss): " + RESET);
                        String interviewTimeStr = scanner.nextLine();
                        LocalDateTime interviewTime = LocalDateTime.parse(interviewTimeStr, formatter);
                        int result2 = applicationService.updateInterviewDetails(applicationId, interviewLink, interviewTime);
                        if (result2 == 0) {
                            System.out.println(GREEN + "Cập nhật chi tiết phỏng vấn thành công." + RESET);
                        } else {
                            System.out.println(RED + "Cập nhật thất bại. Đơn không tồn tại hoặc đã bị hủy." + RESET);
                        }
                        break;
                    case 4:
                        System.out.print(WHITE + "Nhập kết quả phỏng vấn (ví dụ: Đạt, Không đạt): " + RESET);
                        String interviewResult = scanner.nextLine();
                        System.out.print(WHITE + "Nhập ghi chú kết quả phỏng vấn: " + RESET);
                        String interviewResultNote = scanner.nextLine();
                        System.out.print(WHITE + "Nhập trạng thái mới của đơn (ví dụ: done, reject): " + RESET);
                        String progress = scanner.nextLine();
                        int result3 = applicationService.updateInterviewResult(applicationId, interviewResult, interviewResultNote, progress);
                        if (result3 == 0) {
                            System.out.println(GREEN + "Cập nhật kết quả phỏng vấn và trạng thái thành công." + RESET);
                        } else {
                            System.out.println(RED + "Cập nhật thất bại. Đơn không tồn tại hoặc đã bị hủy." + RESET);
                        }
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println(RED + "Vui lòng chọn từ 1-5" + RESET);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println(RED + "ID đơn hoặc lựa chọn phải là số nguyên." + RESET);
        } catch (Exception e) {
            System.out.println(RED + "Lỗi: " + e.getMessage() + RESET);
        }
        System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
        scanner.nextLine();
    }

    public static void cancelApplicationByAdmin(Scanner scanner, int applicationId) {
        System.out.println(MAGENTA + "=== HỦY ĐƠN ỨNG TUYỂN ===" + RESET);
        System.out.print(WHITE + "Nhập lý do hủy: " + RESET);
        try {
            String destroyReason = scanner.nextLine();
            int result = applicationService.cancelApplicationByAdmin(applicationId, destroyReason, Progress.REJECT);
            if (result == 0) {
                System.out.println(GREEN + "Hủy đơn ứng tuyển thành công." + RESET);
            } else {
                System.out.println(RED + "Hủy đơn ứng tuyển thất bại. Đơn không tồn tại hoặc đã bị hủy." + RESET);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(RED + "Lỗi: " + e.getMessage() + RESET);
        } catch (RuntimeException e) {
            System.out.println(RED + "Lỗi khi hủy đơn ứng tuyển: " + e.getMessage() + RESET);
        }
        System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
        scanner.nextLine();
    }
}