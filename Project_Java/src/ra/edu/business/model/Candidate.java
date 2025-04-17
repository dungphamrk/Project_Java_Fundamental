package ra.edu.business.model;
import ra.edu.validate.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

public class Candidate implements Inputable {
    private int id;
    private String name;
    private String username;
    private String email;
    private String password;
    private String phone;
    private int experience;
    private Gender gender;
    private Status status;
    private String description;
    private Date dob;
    private UserRole role; // Sử dụng enum UserRole thay vì boolean

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum Status {
        ACTIVE, INACTIVE
    }

    // Giả định enum UserRole đã được định nghĩa
    public enum UserRole {
        ADMIN, USER
    }

    // Constructors
    public Candidate() {
    }

    public Candidate(int id, String name, String username, String email, String password, String phone, int experience,
                     Gender gender, Status status, String description, Date dob, UserRole role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.experience = experience;
        this.gender = gender;
        this.status = status;
        this.description = description;
        this.dob = dob;
        this.role = role;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public void inputData(Scanner scanner) {
        do {
            System.out.print("Nhập tên ứng viên: ");
            this.name = scanner.nextLine();
            if (this.name.trim().isEmpty()) {
                System.out.println(" Tên không được để trống. Vui lòng nhập lại.");
            }
        } while (this.name.trim().isEmpty());

        do {
            System.out.print("Nhập tên đăng nhập: ");
            this.username = scanner.nextLine();
            if (this.username.trim().isEmpty()) {
                System.out.println(" Tên đăng nhập không được để trống. Vui lòng nhập lại.");
            }
        } while (this.username.trim().isEmpty());

        do {
            System.out.print("Nhập email: ");
            this.email = scanner.nextLine();
            if (!Validator.isValidEmail(this.email)) {
                System.out.println("Email không hợp lệ. Vui lòng nhập lại.");
            }
        } while (!Validator.isValidEmail(this.email));

        System.out.print("Nhập mật khẩu: ");
        this.password = scanner.nextLine();
        do {
            System.out.print("Nhập số điện thoại: ");
            this.phone = scanner.nextLine();
            if (!Validator.isValidPhoneNumberVN(this.phone)) {
                System.out.println(" Số điện thoại không hợp lệ. Vui lòng nhập lại.");
            }
        } while (!Validator.isValidPhoneNumberVN(this.phone));

        String expStr;
        do {
            System.out.print("Nhập số năm kinh nghiệm: ");
            expStr = scanner.nextLine();
            if (!Validator.isValidDataType(expStr, Integer.class)) {
                System.out.println(" Giá trị không hợp lệ. Vui lòng nhập số nguyên.");
            }
        } while (!Validator.isValidDataType(expStr, Integer.class));
        this.experience = Integer.parseInt(expStr);

        // Giới tính
        String genderStr;
        do {
            System.out.print("Nhập giới tính (male/female/other): ");
            genderStr = scanner.nextLine();
            if (!Validator.isValidDataType(genderStr, Gender.class)) {
                System.out.println(" Giá trị không hợp lệ. Vui lòng nhập lại.");
            }
        } while (!Validator.isValidDataType(genderStr, Gender.class));
        this.gender = Gender.valueOf(genderStr.toUpperCase());

        // Trạng thái
        String statusStr;
        do {
            System.out.print("Nhập trạng thái (active/inactive): ");
            statusStr = scanner.nextLine();
            if (!Validator.isValidDataType(statusStr, Status.class)) {
                System.out.println(" Trạng thái không hợp lệ. Vui lòng nhập lại.");
            }
        } while (!Validator.isValidDataType(statusStr, Status.class));
        this.status = Status.valueOf(statusStr.toUpperCase());

        System.out.print("Nhập mô tả: ");
        this.description = scanner.nextLine();

        // Ngày sinh
        String dobStr;
        do {
            System.out.print("Nhập ngày sinh (dd/MM/yyyy): ");
            dobStr = scanner.nextLine();
            if (!Validator.isValidDataType(dobStr, LocalDate.class)) {
                System.out.println(" Ngày sinh không hợp lệ. Vui lòng nhập lại.");
            }
        } while (!Validator.isValidDataType(dobStr, LocalDate.class));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.dob = java.sql.Date.valueOf(LocalDate.parse(dobStr, formatter));

        // Vai trò
        String roleStr;
        do {
            System.out.print("Nhập vai trò (ADMIN/USER): ");
            roleStr = scanner.nextLine();
            if (!Validator.isValidDataType(roleStr, UserRole.class)) {
                System.out.println(" Vai trò không hợp lệ. Vui lòng nhập lại.");
            }
        } while (!Validator.isValidDataType(roleStr, UserRole.class));
        this.role = UserRole.valueOf(roleStr.toUpperCase());
    }

}