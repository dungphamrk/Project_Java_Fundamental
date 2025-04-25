package ra.edu.presentation.admin;

import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.model.technology.Technology;
import ra.edu.business.model.user.Status;
import ra.edu.business.model.user.User;
import ra.edu.business.service.candidateService.CandidateService;
import ra.edu.business.service.candidateService.CandidateServiceImp;
import ra.edu.business.service.technology.TechnologyService;
import ra.edu.business.service.technology.TechnologyServiceImp;
import ra.edu.presentation.ServiceProvider;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import static ra.edu.presentation.ServiceProvider.applicationService;

public class CandidateManagementUI {
    private static final String EMAIL_SMTP_HOST = "smtp.gmail.com";
    private static final int EMAIL_SMTP_PORT = 587;
    private static final String FROM_EMAIL = "dungpham197qn@gmail.com";
    private static final String EMAIL_PASSWORD = "xjwf ylep btbo sciz";
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[91m";
    private static final String GREEN = "\u001B[92m";
    private static final String YELLOW = "\u001B[93m";
    private static final String CYAN = "\u001B[96m";
    private static final String MAGENTA = "\u001B[95m";
    private static final String WHITE = "\u001B[97m";

    private static final CandidateService candidateService = new CandidateServiceImp();
    private static final TechnologyService technologyService = new TechnologyServiceImp();

    public static void displayCandidateManagementMenu(Scanner scanner) {
        do {
            System.out.println(MAGENTA + "=============================================");
            System.out.println("         QUẢN LÝ ỨNG VIÊN                  ");
            System.out.println("=============================================" + RESET);
            System.out.println(CYAN + "1. Xem danh sách ứng viên");
            System.out.println("2. Khóa/mở khóa tài khoản ứng viên");
            System.out.println("3. Reset mật khẩu ứng viên");
            System.out.println("4. Tìm kiếm ứng viên theo tên");
            System.out.println("5. Lọc ứng viên theo kinh nghiệm");
            System.out.println("6. Lọc ứng viên theo giới tính");
            System.out.println("7. Lọc ứng viên theo công nghệ");
            System.out.println("8. Quay lại" + RESET);
            System.out.print(MAGENTA + "Lựa chọn của bạn: " + RESET);
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        displayCandidateList(scanner);
                        break;
                    case 2:
                        lockUnlockCandidateAccount(scanner);
                        break;
                    case 3:
                        resetCandidatePassword(scanner);
                        break;
                    case 4:
                        searchCandidateByName(scanner);
                        break;
                    case 5:
                        filterCandidatesByExperience(scanner);
                        break;
                    case 6:
                        filterCandidatesByGender(scanner);
                        break;
                    case 7:
                        filterCandidatesByTechnology(scanner);
                        break;
                    case 8:
                        return;
                    default:
                        System.out.println(RED + "Vui lòng chọn từ 1-8" + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Vui lòng nhập số từ 1-8" + RESET);
            }
            System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
            scanner.nextLine();
        } while (true);
    }

    private static void displayCandidateList(Scanner scanner) {
        int pageNumber = 1;
        int pageSize = 5;
        int totalRecords = candidateService.getTotalCandidatesCount();
        List<Candidate> candidates = candidateService.findAll(pageNumber, pageSize);
        displayPaginationMenu(scanner, pageNumber, pageSize, totalRecords, candidates, "DANH SÁCH ỨNG VIÊN");
    }

    private static void searchCandidateByName(Scanner scanner) {
        System.out.println(MAGENTA + "=== TÌM KIẾM ỨNG VIÊN THEO TÊN ===" + RESET);
        System.out.print(WHITE + "Nhập tên ứng viên: " + RESET);
        String name = scanner.nextLine();
        int pageNumber = 1;
        int pageSize = 5;
        int totalRecords = candidateService.getTotalCandidatesByName(name);
        List<Candidate> candidates = candidateService.searchByName(name, pageNumber, pageSize);
        displayPaginationMenu(scanner, pageNumber, pageSize, totalRecords, candidates, "KẾT QUẢ TÌM KIẾM (Tên: " + name + ")");
    }

    private static void filterCandidatesByExperience(Scanner scanner) {
        System.out.println(MAGENTA + "=== LỌC ỨNG VIÊN THEO KINH NGHIỆM ===" + RESET);
        System.out.print(WHITE + "Nhập số năm kinh nghiệm tối thiểu: " + RESET);
        try {
            int experience = Integer.parseInt(scanner.nextLine());
            int pageNumber = 1;
            int pageSize = 5;
            int totalRecords = candidateService.getTotalCandidatesByExperience(experience);
            List<Candidate> candidates = candidateService.filterByExperience(experience, pageNumber, pageSize);
            displayPaginationMenu(scanner, pageNumber, pageSize, totalRecords, candidates, "KẾT QUẢ LỌC (Kinh nghiệm: " + experience + " năm)");
        } catch (NumberFormatException e) {
            System.out.println(RED + "Vui lòng nhập số hợp lệ!" + RESET);
            System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
            scanner.nextLine();
        }
    }

    private static void filterCandidatesByGender(Scanner scanner) {
        System.out.println(MAGENTA + "=== LỌC ỨNG VIÊN THEO GIỚI TÍNH ===" + RESET);
        System.out.println(CYAN + "1. Nam (MALE)");
        System.out.println("2. Nữ (FEMALE)" + RESET);
        System.out.print(MAGENTA + "Lựa chọn của bạn: " + RESET);
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            String gender = choice == 1 ? "MALE" : choice == 2 ? "FEMALE" : null;
            if (gender == null) {
                System.out.println(RED + "Vui lòng chọn 1 hoặc 2!" + RESET);
                System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
                scanner.nextLine();
                return;
            }
            int pageNumber = 1;
            int pageSize = 5;
            int totalRecords = candidateService.getTotalCandidatesByGender(gender);
            List<Candidate> candidates = candidateService.filterByGender(gender, pageNumber, pageSize);
            displayPaginationMenu(scanner, pageNumber, pageSize, totalRecords, candidates, "KẾT QUẢ LỌC (Giới tính: " + gender + ")");
        } catch (NumberFormatException e) {
            System.out.println(RED + "Vui lòng nhập số hợp lệ!" + RESET);
            System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
            scanner.nextLine();
        }
    }

    private static void filterCandidatesByTechnology(Scanner scanner) {
        System.out.println(MAGENTA + "=== LỌC ỨNG VIÊN THEO CÔNG NGHỆ ===" + RESET);
        System.out.print(WHITE + "Nhập ID công nghệ: " + RESET);
        try {
            int technologyId = Integer.parseInt(scanner.nextLine());
            Technology technology = technologyService.findById(technologyId);
            if (technology == null) {
                System.out.println(RED + "Không tìm thấy công nghệ với ID: " + technologyId + RESET);
                System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
                scanner.nextLine();
                return;
            }
            int pageNumber = 1;
            int pageSize = 5;
            int totalRecords = candidateService.getTotalCandidatesByTechnology(technology.getName());
            List<Candidate> candidates = candidateService.filterByTechnology(technology.getName(), pageNumber, pageSize);
            displayPaginationMenu(scanner, pageNumber, pageSize, totalRecords, candidates, "KẾT QUẢ LỌC (Công nghệ: " + technology.getName() + ")");
        } catch (NumberFormatException e) {
            System.out.println(RED + "Vui lòng nhập ID là một số!" + RESET);
            System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
            scanner.nextLine();
        }
    }

    private static void displayPaginationMenu(Scanner scanner, int currentPage, int pageSize, int totalRecords, List<Candidate> candidates, String title) {
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        do {
            printCandidateTable(candidates, title);
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
                            if (title.startsWith("DANH SÁCH ỨNG VIÊN")) {
                                candidates = candidateService.findAll(currentPage, pageSize);
                            } else if (title.startsWith("KẾT QUẢ TÌM KIẾM")) {
                                String name = title.split("Tên: ")[1].replace(")", "");
                                candidates = candidateService.searchByName(name, currentPage, pageSize);
                            } else if (title.startsWith("KẾT QUẢ LỌC (Kinh nghiệm")) {
                                int experience = Integer.parseInt(title.split("Kinh nghiệm: ")[1].split(" ")[0]);
                                candidates = candidateService.filterByExperience(experience, currentPage, pageSize);
                            } else if (title.startsWith("KẾT QUẢ LỌC (Giới tính")) {
                                String gender = title.split("Giới tính: ")[1].replace(")", "");
                                candidates = candidateService.filterByGender(gender, currentPage, pageSize);
                            } else if (title.startsWith("KẾT QUẢ LỌC (Công nghệ")) {
                                String technology = title.split("Công nghệ: ")[1].replace(")", "");
                                candidates = candidateService.filterByTechnology(technology, currentPage, pageSize);
                            }
                        } else {
                            System.out.println(RED + "Đây là trang đầu tiên!" + RESET);
                        }
                        break;
                    case 2: // Trang sau
                        if (currentPage < totalPages) {
                            currentPage++;
                            if (title.startsWith("DANH SÁCH ỨNG VIÊN")) {
                                candidates = candidateService.findAll(currentPage, pageSize);
                            } else if (title.startsWith("KẾT QUẢ TÌM KIẾM")) {
                                String name = title.split("Tên: ")[1].replace(")", "");
                                candidates = candidateService.searchByName(name, currentPage, pageSize);
                            } else if (title.startsWith("KẾT QUẢ LỌC (Kinh nghiệm")) {
                                int experience = Integer.parseInt(title.split("Kinh nghiệm: ")[1].split(" ")[0]);
                                candidates = candidateService.filterByExperience(experience, currentPage, pageSize);
                            } else if (title.startsWith("KẾT QUẢ LỌC (Giới tính")) {
                                String gender = title.split("Giới tính: ")[1].replace(")", "");
                                candidates = candidateService.filterByGender(gender, currentPage, pageSize);
                            } else if (title.startsWith("KẾT QUẢ LỌC (Công nghệ")) {
                                String technology = title.split("Công nghệ: ")[1].replace(")", "");
                                candidates = candidateService.filterByTechnology(technology, currentPage, pageSize);
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
                                if (title.startsWith("DANH SÁCH ỨNG VIÊN")) {
                                    candidates = candidateService.findAll(currentPage, pageSize);
                                } else if (title.startsWith("KẾT QUẢ TÌM KIẾM")) {
                                    String name = title.split("Tên: ")[1].replace(")", "");
                                    candidates = candidateService.searchByName(name, currentPage, pageSize);
                                } else if (title.startsWith("KẾT QUẢ LỌC (Kinh nghiệm")) {
                                    int experience = Integer.parseInt(title.split("Kinh nghiệm: ")[1].split(" ")[0]);
                                    candidates = candidateService.filterByExperience(experience, currentPage, pageSize);
                                } else if (title.startsWith("KẾT QUẢ LỌC (Giới tính")) {
                                    String gender = title.split("Giới tính: ")[1].replace(")", "");
                                    candidates = candidateService.filterByGender(gender, currentPage, pageSize);
                                } else if (title.startsWith("KẾT QUẢ LỌC (Công nghệ")) {
                                    String technology = title.split("Công nghệ: ")[1].replace(")", "");
                                    candidates = candidateService.filterByTechnology(technology, currentPage, pageSize);
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
                                if (title.startsWith("DANH SÁCH ỨNG VIÊN")) {
                                    candidates = candidateService.findAll(currentPage, pageSize);
                                } else if (title.startsWith("KẾT QUẢ TÌM KIẾM")) {
                                    String name = title.split("Tên: ")[1].replace(")", "");
                                    candidates = candidateService.searchByName(name, currentPage, pageSize);
                                } else if (title.startsWith("KẾT QUẢ LỌC (Kinh nghiệm")) {
                                    int experience = Integer.parseInt(title.split("Kinh nghiệm: ")[1].split(" ")[0]);
                                    candidates = candidateService.filterByExperience(experience, currentPage, pageSize);
                                } else if (title.startsWith("KẾT QUẢ LỌC (Giới tính")) {
                                    String gender = title.split("Giới tính: ")[1].replace(")", "");
                                    candidates = candidateService.filterByGender(gender, currentPage, pageSize);
                                } else if (title.startsWith("KẾT QUẢ LỌC (Công nghệ")) {
                                    String technology = title.split("Công nghệ: ")[1].replace(")", "");
                                    candidates = candidateService.filterByTechnology(technology, currentPage, pageSize);
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

    private static void printCandidateTable(List<Candidate> candidates, String title) {
        System.out.println(MAGENTA + "=== " + title + " ===" + RESET);
        if (candidates == null || candidates.isEmpty()) {
            System.out.println(RED + "Không có ứng viên nào để hiển thị." + RESET);
        } else {
            System.out.println(YELLOW + "+-----+--------------------+-------------------------+-------------+--------+" + RESET);
            System.out.println("| ID  | Tên                | Email                   | Kinh nghiệm | Giới tính |");
            System.out.println(YELLOW + "+-----+--------------------+-------------------------+-------------+--------+" + RESET);
            for (Candidate candidate : candidates) {
                System.out.printf(WHITE + "| %-3d | %-18s | %-23s | %-11d | %-10s |%n",
                        candidate.getId(),
                        candidate.getName().length() > 18 ? candidate.getName().substring(0, 15) + "..." : candidate.getName(),
                        candidate.getEmail().length() > 23 ? candidate.getEmail().substring(0, 20) + "..." : candidate.getEmail(),
                        candidate.getExperience(),
                        candidate.getGender());
            }
            System.out.println(YELLOW + "+-----+--------------------+-------------------------+-------------+--------+" + RESET);
        }
    }

    private static void lockUnlockCandidateAccount(Scanner scanner) {
        System.out.print(MAGENTA + "Nhập ID ứng viên cần khóa/mở khóa: " + RESET);
        try {
            int candidateId = Integer.parseInt(scanner.nextLine());
            Candidate candidate = candidateService.getCandidateById(candidateId);
            User user = ServiceProvider.userService.getUserById(candidateId);

            if (candidate == null || user == null) {
                System.out.println(RED + "Không tìm thấy ứng viên hoặc thông tin người dùng với ID: " + candidateId + RESET);
                System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
                scanner.nextLine();
                return;
            }

            System.out.println(MAGENTA + "=============================================");
            System.out.println("       THÔNG TIN ỨNG VIÊN                  ");
            System.out.println("=============================================" + RESET);
            System.out.println(YELLOW + "+-----+--------------------+-----------------+" + RESET);
            System.out.println("| ID  | Tên tài khoản      | Trạng thái      |");
            System.out.println(YELLOW + "+-----+--------------------+-----------------+" + RESET);
            System.out.printf(WHITE + "| %-3d | %-18s | %-15s |%n",
                    candidate.getId(),
                    user.getUsername().length() > 18 ? user.getUsername().substring(0, 15) + "..." : user.getUsername(),
                    user.getStatus());
            System.out.println(YELLOW + "+-----+--------------------+-----------------+" + RESET);

            System.out.print(MAGENTA + "Bạn có chắc chắn muốn thay đổi trạng thái tài khoản? (Y/N): " + RESET);
            String confirmation = scanner.nextLine().trim().toUpperCase();
            if (!confirmation.equals("Y")) {
                System.out.println(CYAN + "Hủy thao tác khóa/mở khóa." + RESET);
                System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
                scanner.nextLine();
                return;
            }

            if(user.getStatus().equals(Status.ACTIVE)) {
               applicationService.cancelAllApplicationById(user.getId());
            }
            boolean success = candidateService.lockUnlockAccount(candidateId);
            if (success) {
                System.out.println(GREEN + "Thay đổi trạng thái tài khoản thành công." + RESET);
            } else {
                System.out.println(RED + "Không tìm thấy ứng viên hoặc cập nhật thất bại." + RESET);
            }
        } catch (NumberFormatException e) {
            System.out.println(RED + "ID ứng viên phải là số nguyên." + RESET);
        }
        System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
        scanner.nextLine();
    }

    private static void resetCandidatePassword(Scanner scanner) {
        System.out.println(MAGENTA + "=== RESET MẬT KHẨU ỨNG VIÊN ===" + RESET);
        System.out.print(WHITE + "Nhập ID ứng viên: " + RESET);
        try {
            int candidateId = Integer.parseInt(scanner.nextLine());
            Candidate candidate = candidateService.getCandidateById(candidateId);
            if (candidate == null) {
                System.out.println(RED + "Không tìm thấy ứng viên với ID: " + candidateId + RESET);
            } else {
                String email = candidate.getEmail();
                String otp = generateOtp();
                if (!sendOtpEmail(email, otp)) {
                    System.out.println(RED + "Gửi OTP thất bại. Vui lòng thử lại sau." + RESET);
                } else {
                    System.out.print(WHITE + "Nhập mã OTP được gửi đến " + email + ": " + RESET);
                    String enteredOtp = scanner.nextLine().trim();
                    if (otp.equals(enteredOtp)) {
                        System.out.print(WHITE + "Nhập mật khẩu mới: " + RESET);
                        String newPassword = scanner.nextLine();
                        int result = candidateService.resetPassword(candidateId, newPassword);
                        if (result == 0) {
                            System.out.println(GREEN + "Reset mật khẩu thành công!" + RESET);
                        } else {
                            System.out.println(RED + "Reset mật khẩu thất bại! Vui lòng kiểm tra mật khẩu." + RESET);
                        }
                    } else {
                        System.out.println(RED + "Mã OTP không đúng." + RESET);
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println(RED + "Vui lòng nhập ID là một số!" + RESET);
        }
        System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
        scanner.nextLine();
    }

    private static String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    private static boolean sendOtpEmail(String toEmail, String otp) {
        Properties props = new Properties();
        props.put("mail.smtp.host", EMAIL_SMTP_HOST);
        props.put("mail.smtp.port", String.valueOf(EMAIL_SMTP_PORT));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Mã OTP Reset Mật Khẩu");
            message.setText("Mã OTP của bạn là: " + otp + "\nVui lòng không chia sẻ mã này.");
            Transport.send(message);
            System.out.println(GREEN + "Gửi email OTP thành công." + RESET);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}