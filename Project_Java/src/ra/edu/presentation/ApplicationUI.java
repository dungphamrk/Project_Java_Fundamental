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
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void displayCandidateApplicationMenu(Scanner scanner) {
        do {
            System.out.println("***************QUẢN LÝ ĐƠN ỨNG TUYỂN CỦA BẠN**************");
            System.out.println("1. Xem danh sách đơn ứng tuyển");
            System.out.println("2. Hủy đơn ứng tuyển");
            System.out.println("3. Quay lại");
            System.out.print("Lựa chọn của bạn: ");
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
                        System.out.println("Vui lòng chọn từ 1-3");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số từ 1-3");
            }
        } while (true);
    }

    public static void displayCandidateApplications(Scanner scanner) {
        int candidateId = userService.getCurrentUserId();
        if (candidateId == 0) {
            System.out.println("Bạn cần đăng nhập để xem đơn ứng tuyển.");
            return;
        }

        int pageNumber = 1;
        int pageSize = 5;

        while (true) {
            try {
                List<Application> applications = applicationService.getApplicationsByCandidateId(candidateId, pageNumber, pageSize);
                if (applications.isEmpty()) {
                    System.out.println("Bạn chưa có đơn ứng tuyển nào.");
                    return;
                }

                System.out.println("***************DANH SÁCH ĐƠN ỨNG TUYỂN**************");
                System.out.println("Trang " + pageNumber + " - Số phần tử trên trang: " + pageSize);
                for (Application app : applications) {
                    System.out.printf("ID đơn: %d, Vị trí ID: %d, URL CV: %s, Trạng thái: %s%n",
                            app.getId(), app.getRecruitmentPositionId(), app.getCvUrl(), app.getProgress());
                }

                System.out.println("***************TÙY CHỌN**************");
                System.out.println("1. Trang tiếp theo");
                System.out.println("2. Trang trước");
                System.out.println("3. Thay đổi số phần tử trên trang");
                System.out.println("4. Quay lại");
                System.out.print("Lựa chọn của bạn: ");
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        pageNumber++;
                        break;
                    case "2":
                        if (pageNumber > 1) pageNumber--;
                        break;
                    case "3":
                        System.out.println("Nhập số phần tử trên trang:");
                        try {
                            pageSize = Integer.parseInt(scanner.nextLine());
                            if (pageSize < 1) {
                                System.out.println("Số phần tử phải lớn hơn 0.");
                                pageSize = 5;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Vui lòng nhập số hợp lệ.");
                        }
                        break;
                    case "4":
                        return;
                    default:
                        System.out.println("Vui lòng chọn từ 1-4");
                }
            } catch (RuntimeException e) {
                System.out.println("Lỗi khi lấy danh sách đơn ứng tuyển: " + e.getMessage());
                return;
            }
        }
    }

    public static void cancelApplication(Scanner scanner) {
        int candidateId = userService.getCurrentUserId();
        if (candidateId == 0) {
            System.out.println("Bạn cần đăng nhập để hủy đơn ứng tuyển.");
            return;
        }

        System.out.println("Nhập ID đơn ứng tuyển cần hủy:");
        try {
            int applicationId = Integer.parseInt(scanner.nextLine());
            Application app = applicationService.findById(applicationId);
            if (app == null) {
                System.out.println("Đơn ứng tuyển không tồn tại.");
                return;
            }
            if (app.getProgress() == Progress.REJECT || app.getProgress() == Progress.CANCEL) {
                System.out.println("Đơn ứng tuyển đã bị hủy hoặc từ chối, không thể hủy lại.");
                return;
            }
            System.out.println("Nhập lý do hủy:");
            String destroyReason = scanner.nextLine();

            int result = applicationService.cancelApplication(applicationId, candidateId, destroyReason, Progress.CANCEL);
            if (result == 0) {
                System.out.println("Hủy đơn ứng tuyển thành công.");
            } else {
                System.out.println("Hủy đơn ứng tuyển thất bại. Đơn không tồn tại hoặc không thuộc về bạn.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID đơn phải là số nguyên.");
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Lỗi khi hủy đơn ứng tuyển: " + e.getMessage());
        }
    }

    public static void displayAdminApplicationMenu(Scanner scanner) {
        do {
            System.out.println("***************QUẢN LÝ ĐƠN ỨNG TUYỂN**************");
            System.out.println("1. Xem danh sách tất cả đơn ứng tuyển");
            System.out.println("2. Lọc đơn theo trạng thái");
            System.out.println("3. Quay lại");
            System.out.print("Lựa chọn của bạn: ");
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
                        System.out.println("Vui lòng chọn từ 1-3");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số từ 1-3");
            }
        } while (true);
    }

    public static void displayAllApplications(Scanner scanner) {
        int pageNumber = 1;
        int pageSize = 5;

        while (true) {
            try {
                List<Application> applications = applicationService.findAll(pageNumber, pageSize);
                if (applications.isEmpty()) {
                    System.out.println("Chưa có đơn ứng tuyển nào.");
                    return;
                }

                System.out.println("***************DANH SÁCH ĐƠN ỨNG TUYỂN**************");
                System.out.println("Trang " + pageNumber + " - Số phần tử trên trang: " + pageSize);
                for (Application app : applications) {
                    System.out.printf("ID đơn: %d, Vị trí ID: %d, URL CV: %s, Trạng thái: %s%n",
                            app.getId(), app.getRecruitmentPositionId(), app.getCvUrl(), app.getProgress());
                }

                System.out.println("***************TÙY CHỌN**************");
                System.out.println("1. Trang tiếp theo");
                System.out.println("2. Trang trước");
                System.out.println("3. Thay đổi số phần tử trên trang");
                System.out.println("4. Xem chi tiết đơn");
                System.out.println("5. Quay lại");
                System.out.print("Lựa chọn của bạn: ");
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        pageNumber++;
                        break;
                    case "2":
                        if (pageNumber > 1) pageNumber--;
                        break;
                    case "3":
                        System.out.println("Nhập số phần tử trên trang:");
                        try {
                            pageSize = Integer.parseInt(scanner.nextLine());
                            if (pageSize < 1) {
                                System.out.println("Số phần tử phải lớn hơn 0.");
                                pageSize = 5;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Vui lòng nhập số hợp lệ.");
                        }
                        break;
                    case "4":
                        viewApplicationDetails(scanner);
                        break;
                    case "5":
                        return;
                    default:
                        System.out.println("Vui lòng chọn từ 1-5");
                }
            } catch (RuntimeException e) {
                System.out.println("Lỗi khi lấy danh sách đơn ứng tuyển: " + e.getMessage());
                return;
            }
        }
    }

    public static void filterApplicationsByProgress(Scanner scanner) {
        System.out.println("Chọn trạng thái để lọc:");
        Progress[] progresses = Progress.values();
        for (int i = 0; i < progresses.length; i++) {
            System.out.println((i + 1) + ". " + progresses[i]);
        }
        System.out.print("Lựa chọn của bạn: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice < 1 || choice > progresses.length) {
                System.out.println("Vui lòng chọn trạng thái hợp lệ.");
                return;
            }
            Progress selectedProgress = progresses[choice - 1];
            displayFilteredApplications(scanner, selectedProgress);
        } catch (NumberFormatException e) {
            System.out.println("Vui lòng nhập số hợp lệ.");
        }
    }

    public static void displayFilteredApplications(Scanner scanner, Progress progress) {
        int pageNumber = 1;
        int pageSize = 5;

        while (true) {
            try {
                List<Application> applications = applicationService.findByProgress(progress, pageNumber, pageSize);
                if (applications.isEmpty()) {
                    System.out.println("Chưa có đơn ứng tuyển nào với trạng thái " + progress);
                    return;
                }

                System.out.println("***************DANH SÁCH ĐƠN ỨNG TUYỂN - TRẠNG THÁI: " + progress + "**************");
                System.out.println("Trang " + pageNumber + " - Số phần tử trên trang: " + pageSize);
                for (Application app : applications) {
                    System.out.printf("ID đơn: %d, Vị trí ID: %d, URL CV: %s, Trạng thái: %s%n",
                            app.getId(), app.getRecruitmentPositionId(), app.getCvUrl(), app.getProgress());
                }

                System.out.println("***************TÙY CHỌN**************");
                System.out.println("1. Trang tiếp theo");
                System.out.println("2. Trang trước");
                System.out.println("3. Thay đổi số phần tử trên trang");
                System.out.println("4. Xem chi tiết đơn");
                System.out.println("5. Quay lại");
                System.out.print("Lựa chọn của bạn: ");
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        pageNumber++;
                        break;
                    case "2":
                        if (pageNumber > 1) pageNumber--;
                        break;
                    case "3":
                        System.out.println("Nhập số phần tử trên trang:");
                        try {
                            pageSize = Integer.parseInt(scanner.nextLine());
                            if (pageSize < 1) {
                                System.out.println("Số phần tử phải lớn hơn 0.");
                                pageSize = 5;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Vui lòng nhập số hợp lệ.");
                        }
                        break;
                    case "4":
                        viewApplicationDetails(scanner);
                        break;
                    case "5":
                        return;
                    default:
                        System.out.println("Vui lòng chọn từ 1-5");
                }
            } catch (RuntimeException e) {
                System.out.println("Lỗi khi lấy danh sách đơn ứng tuyển: " + e.getMessage());
                return;
            }
        }
    }

    public static void viewApplicationDetails(Scanner scanner) {
        System.out.println("Nhập ID đơn ứng tuyển cần xem chi tiết:");
        try {
            int applicationId = Integer.parseInt(scanner.nextLine());
            Application app = applicationService.findById(applicationId);
            if (app == null) {
                System.out.println("Đơn ứng tuyển không tồn tại.");
                return;
            }

            // Update progress to HANDLING only if not REJECT or CANCEL
            if (app.getProgress() != Progress.REJECT && app.getProgress() != Progress.CANCEL) {
                applicationService.updateField(applicationId, "progress", Progress.HANDLING);
            }

            System.out.println("***************CHI TIẾT ĐƠN ỨNG TUYỂN**************");
            System.out.printf("ID đơn: %d%n", app.getId());
            System.out.printf("ID ứng viên: %d%n", app.getCandidateId());
            System.out.printf("ID vị trí tuyển dụng: %d%n", app.getRecruitmentPositionId());
            System.out.printf("URL CV: %s%n", app.getCvUrl());
            System.out.printf("Trạng thái: %s%n", app.getProgress());
            System.out.printf("Ngày yêu cầu phỏng vấn: %s%n", app.getInterviewRequestDate() != null ? app.getInterviewRequestDate().format(formatter) : "N/A");
            System.out.printf("Kết quả yêu cầu phỏng vấn: %s%n", app.getInterviewRequestResult() != null ? app.getInterviewRequestResult() : "N/A");
            System.out.printf("Link phỏng vấn: %s%n", app.getInterviewLink() != null ? app.getInterviewLink() : "N/A");
            System.out.printf("Thời gian phỏng vấn: %s%n", app.getInterviewTime() != null ? app.getInterviewTime().format(formatter) : "N/A");
            System.out.printf("Kết quả phỏng vấn: %s%n", app.getInterviewResult() != null ? app.getInterviewResult() : "N/A");
            System.out.printf("Ghi chú kết quả phỏng vấn: %s%n", app.getInterviewResultNote() != null ? app.getInterviewResultNote() : "N/A");
            System.out.printf("Ngày hủy: %s%n", app.getDestroyAt() != null ? app.getDestroyAt().format(formatter) : "N/A");
            System.out.printf("Lý do hủy: %s%n", app.getDestroyReason() != null ? app.getDestroyReason() : "N/A");
            System.out.printf("Ngày tạo: %s%n", app.getCreateAt() != null ? app.getCreateAt().format(formatter) : "N/A");
            System.out.printf("Ngày cập nhật: %s%n", app.getUpdateAt() != null ? app.getUpdateAt().format(formatter) : "N/A");

            System.out.println("***************TÙY CHỌN**************");
            if (app.getProgress() == Progress.REJECT || app.getProgress() == Progress.CANCEL) {
                System.out.println("Đơn ứng tuyển đã bị hủy hoặc từ chối, không thể chỉnh sửa.");
                System.out.println("1. Quay lại");
                System.out.print("Lựa chọn của bạn: ");
                String choice = scanner.nextLine();
                if (!choice.equals("1")) {
                    System.out.println("Vui lòng chọn 1 để quay lại.");
                }
                return;
            } else {
                System.out.println("1. Hủy đơn ứng tuyển");
                System.out.println("2. Gửi yêu cầu phỏng vấn");
                System.out.println("3. Gửi link phỏng vấn và thời gian");
                System.out.println("4. Cập nhật kết quả phỏng vấn và trạng thái");
                System.out.println("5. Quay lại");
                System.out.print("Lựa chọn của bạn: ");
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        cancelApplicationByAdmin(scanner, applicationId);
                        break;
                    case 2:
                        System.out.println("Nhập kết quả yêu cầu phỏng vấn (ví dụ: Đã gửi, Chưa gửi):");
                        String interviewRequestResult = scanner.nextLine();
                        LocalDateTime interviewRequestDate = LocalDateTime.now();
                        int result1 = applicationService.updateInterviewRequest(applicationId, interviewRequestDate, interviewRequestResult);
                        if (result1 == 0) {
                            System.out.println("Cập nhật yêu cầu phỏng vấn thành công.");
                        } else {
                            System.out.println("Cập nhật thất bại. Đơn không tồn tại hoặc đã bị hủy.");
                        }
                        break;
                    case 3:
                        System.out.println("Nhập link phỏng vấn:");
                        String interviewLink = scanner.nextLine();
                        System.out.println("Nhập thời gian phỏng vấn (định dạng yyyy-MM-dd HH:mm:ss):");
                        String interviewTimeStr = scanner.nextLine();
                        LocalDateTime interviewTime = LocalDateTime.parse(interviewTimeStr, formatter);
                        int result2 = applicationService.updateInterviewDetails(applicationId, interviewLink, interviewTime);
                        if (result2 == 0) {
                            System.out.println("Cập nhật chi tiết phỏng vấn thành công.");
                        } else {
                            System.out.println("Cập nhật thất bại. Đơn không tồn tại hoặc đã bị hủy.");
                        }
                        break;
                    case 4:
                        System.out.println("Nhập kết quả phỏng vấn (ví dụ: Đạt, Không đạt):");
                        String interviewResult = scanner.nextLine();
                        System.out.println("Nhập ghi chú kết quả phỏng vấn:");
                        String interviewResultNote = scanner.nextLine();
                        System.out.println("Nhập trạng thái mới của đơn (ví dụ: done, reject):");
                        String progress = scanner.nextLine();
                        int result3 = applicationService.updateInterviewResult(applicationId, interviewResult, interviewResultNote, progress);
                        if (result3 == 0) {
                            System.out.println("Cập nhật kết quả phỏng vấn và trạng thái thành công.");
                        } else {
                            System.out.println("Cập nhật thất bại. Đơn không tồn tại hoặc đã bị hủy.");
                        }
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Vui lòng chọn từ 1-5");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("ID đơn hoặc lựa chọn phải là số nguyên.");
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public static void cancelApplicationByAdmin(Scanner scanner, int applicationId) {
        System.out.println("Nhập lý do hủy:");
        try {
            String destroyReason = scanner.nextLine();
            int result = applicationService.cancelApplicationByAdmin(applicationId, destroyReason, Progress.REJECT);
            if (result == 0) {
                System.out.println("Hủy đơn ứng tuyển thành công.");
            } else {
                System.out.println("Hủy đơn ứng tuyển thất bại. Đơn không tồn tại hoặc đã bị hủy.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Lỗi khi hủy đơn ứng tuyển: " + e.getMessage());
        }
    }
}