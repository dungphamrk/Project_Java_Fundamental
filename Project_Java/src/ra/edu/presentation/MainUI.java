package ra.edu.presentation;

import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.model.user.Status;
import ra.edu.business.model.user.User;
import ra.edu.presentation.admin.AdminUI;
import ra.edu.presentation.candidate.CandidateUI;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static ra.edu.presentation.ServiceProvider.candidateService;
import static ra.edu.presentation.ServiceProvider.userService;

public class MainUI {
    // Mã màu ANSI sáng
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[91m";    // Đỏ sáng
    private static final String GREEN = "\u001B[92m";  // Xanh lá sáng
    private static final String YELLOW = "\u001B[93m"; // Vàng sáng
    private static final String CYAN = "\u001B[96m";   // Xanh lam sáng
    private static final String MAGENTA = "\u001B[95m"; // Tím sáng
    private static final String WHITE = "\u001B[97m";  // Trắng sáng


    public static void displayMainMenu(Scanner scanner) {
        String loggedInRole = userService.isLoggedIn();
        if (loggedInRole != null) {
            if (loggedInRole.equalsIgnoreCase("CANDIDATE")) {
                CandidateUI.displayCandidateMenu(scanner);
            } else if (loggedInRole.equalsIgnoreCase("ADMIN")) {
                AdminUI.displayAdminMenu(scanner);
            }
            return;
        }

        do {
            System.out.println(MAGENTA + "=============================================");
            System.out.println("       HỆ THỐNG QUẢN LÝ TUYỂN DỤNG         ");
            System.out.println("=============================================" + RESET);
            System.out.println(CYAN + "1. Đăng ký tài khoản");
            System.out.println("2. Đăng nhập");
            System.out.println("3. Quên mật khẩu");
            System.out.println("4. Thoát" + RESET);
            System.out.print(MAGENTA + "Lựa chọn của bạn: " + RESET);
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        register(scanner);
                        break;
                    case 2:
                        login(scanner);
                        break;
                    case 3:
                        forgotPassword(scanner);
                        break;
                    case 4:
                        System.out.println(GREEN + "Thoát chương trình." + RESET);
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println(RED + "Vui lòng chọn từ 1-4" + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Vui lòng nhập số từ 1-4" + RESET);
            }
        } while (true);
    }

    private static void forgotPassword(Scanner scanner) {
        System.out.println(MAGENTA + "=== QUÊN MẬT KHẨU ===" + RESET);
        System.out.print(WHITE + "Nhập tên đăng nhập: " + RESET);
        String username = scanner.nextLine();
        System.out.print(WHITE + "Nhập email: " + RESET);
        String email = scanner.nextLine();

        User user = null;
        for (Candidate candidate : candidateService.findAll(1, Integer.MAX_VALUE)) {
            User tempUser = userService.getUserById(candidate.getId());
            if (tempUser.getUsername().equals(username) && candidate.getEmail().equals(email)) {
                user = tempUser;
                break;
            }
        }

        if (user == null) {
            System.out.println(RED + "Tên đăng nhập hoặc email không đúng." + RESET);
            return;
        }

        if (!user.getStatus().equals(Status.HANDLING)) {
            System.out.println(RED + "Tài khoản không ở trạng thái HANDLING." + RESET);
            return;
        }

        // Đọc file reset_password_info.txt để kiểm tra OTP
        String storedOtp = null;
        try (BufferedReader reader = new BufferedReader(new FileReader("reset_password_info.txt"))) {
            String line;
            String currentUsername = null;
            String currentEmail = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("username=")) {
                    currentUsername = line.substring("username=".length());
                } else if (line.startsWith("email=")) {
                    currentEmail = line.substring("email=".length());
                } else if (line.startsWith("otp=") && currentUsername != null && currentEmail != null) {
                    if (currentUsername.equals(username) && currentEmail.equals(email)) {
                        storedOtp = line.substring("otp=".length());
                        break;
                    }
                } else if (line.equals("---")) {
                    currentUsername = null;
                    currentEmail = null;
                }
            }
        } catch (IOException e) {
            System.err.println(RED + "Lỗi khi đọc file OTP: " + e.getMessage() + RESET);
            return;
        }

        if (storedOtp == null) {
            System.out.println(RED + "Không tìm thấy thông tin OTP cho tài khoản này." + RESET);
            return;
        }

        System.out.print(WHITE + "Nhập mã OTP: " + RESET);
        String enteredOtp = scanner.nextLine().trim();

        if (!enteredOtp.equals(storedOtp)) {
            System.out.println(RED + "Mã OTP không đúng. Vui lòng thử lại." + RESET);
            return;
        }

        System.out.print(WHITE + "Nhập mật khẩu mới: " + RESET);
        String newPassword = scanner.nextLine();
        int result = candidateService.resetPassword(user.getId(), newPassword);
        if (result == 0) {
            // Cập nhật trạng thái về ACTIVE
            user.setStatus(Status.ACTIVE);
            candidateService.lockUnlockAccount(user.getId());
            System.out.println(GREEN + "Đổi mật khẩu thành công! Bạn có thể đăng nhập lại." + RESET);
            // Xóa thông tin OTP khỏi file
            clearResetInfoForUser(username);
        } else {
            System.out.println(RED + "Đổi mật khẩu thất bại! Vui lòng kiểm tra mật khẩu." + RESET);
        }
    }

    private static void clearResetInfoForUser(String username) {
        File inputFile = new File("reset_password_info.txt");
        List<String> lines = new ArrayList<>();
        boolean skipRecord = false;
        String currentUsername = null;

        // Đọc file và lọc bỏ bản ghi liên quan đến username
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("username=")) {
                    currentUsername = line.substring("username=".length());
                    if (currentUsername.equals(username)) {
                        skipRecord = true; // Bắt đầu bỏ qua bản ghi
                    } else {
                        skipRecord = false;
                        lines.add(line);
                    }
                } else if (line.equals("---")) {
                    if (!skipRecord) {
                        lines.add(line);
                    }
                    skipRecord = false; // Kết thúc bản ghi
                    currentUsername = null;
                } else if (!skipRecord) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println(RED + "Lỗi khi đọc file OTP: " + e.getMessage() + RESET);
            return;
        }

        // Ghi lại file với các bản ghi còn lại
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println(GREEN + "Đã xóa thông tin OTP cho tài khoản " + username + RESET);
        } catch (IOException e) {
            System.err.println(RED + "Lỗi khi ghi file OTP: " + e.getMessage() + RESET);
        }
    }


    private static void register(Scanner scanner) {
        System.out.println(MAGENTA + "=== ĐĂNG KÝ TÀI KHOẢN ===" + RESET);
        Candidate newCandidate = new Candidate();
        newCandidate.inputData(scanner);
        User newUser = new User();
        newUser.inputData(scanner);
        int result2 = userService.save(newUser);
        newCandidate.setId(newUser.getId());
        int result1 = candidateService.save(newCandidate);
        if (result1 == 0 && result2 == 0) {
            System.out.println(GREEN + "Đăng ký thành công!" + RESET);
        } else {
            System.out.println(RED + "Đăng ký thất bại. Vui lòng kiểm tra lại thông tin." + RESET);
        }
        System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
        scanner.nextLine();
    }

    private static void login(Scanner scanner) {
        System.out.println(MAGENTA + "=== ĐĂNG NHẬP ===" + RESET);
        System.out.print(WHITE + "Nhập tên đăng nhập: " + RESET);
        String username = scanner.nextLine();
        System.out.print(WHITE + "Nhập mật khẩu: " + RESET);
        String password = scanner.nextLine();

        int result = userService.login(username, password);
        if (result == 0) {
            System.out.println(GREEN + "Đăng nhập thành công!" + RESET);
            String role = userService.isLoggedIn();
            if (role != null) {
                if (role.equalsIgnoreCase("CANDIDATE")) {
                    CandidateUI.displayCandidateMenu(scanner);
                } else if (role.equalsIgnoreCase("ADMIN")) {
                    AdminUI.displayAdminMenu(scanner);
                }
            }
        } else {
            System.out.println(RED + "Đăng nhập thất bại. Tên đăng nhập hoặc mật khẩu không đúng." + RESET);
        }
        System.out.println(MAGENTA + "Nhấn Enter để quay lại..." + RESET);
        scanner.nextLine();
    }
}