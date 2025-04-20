package ra.edu.business.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class RecruitmentPosition implements Inputable {
    private int id;
    private String name;
    private String description;
    private double minSalary;
    private double maxSalary;
    private int minExperience;
    private Date createdDate;
    private Date expiredDate;

    public RecruitmentPosition() {
    }

    public RecruitmentPosition(int id, String name, String description, double minSalary, double maxSalary,
                               int minExperience, Date createdDate, Date expiredDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.minExperience = minExperience;
        this.createdDate = createdDate;
        this.expiredDate = expiredDate;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(double minSalary) {
        this.minSalary = minSalary;
    }

    public double getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(double maxSalary) {
        this.maxSalary = maxSalary;
    }

    public int getMinExperience() {
        return minExperience;
    }

    public void setMinExperience(int minExperience) {
        this.minExperience = minExperience;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    @Override
    public void inputData(Scanner scanner) {
        System.out.println("Nhập tên vị trí tuyển dụng:");
        this.name = scanner.nextLine();
        System.out.println("Nhập mô tả:");
        this.description = scanner.nextLine();
        System.out.println("Nhập lương tối thiểu:");
        this.minSalary = Double.parseDouble(scanner.nextLine());
        System.out.println("Nhập lương tối đa:");
        this.maxSalary = Double.parseDouble(scanner.nextLine());
        System.out.println("Nhập kinh nghiệm tối thiểu (năm):");
        this.minExperience = Integer.parseInt(scanner.nextLine());
        System.out.println("Nhập ngày hết hạn (dd/MM/yyyy HH:mm:ss, để trống nếu không có):");
        String expiredDateStr = scanner.nextLine();
        if (!expiredDateStr.isEmpty()) {
            try {
                this.expiredDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(expiredDateStr);
            } catch (ParseException e) {
                System.err.println("Định dạng ngày không hợp lệ, đặt ngày hết hạn là null.");
                this.expiredDate = null;
            }
        }
    }
}