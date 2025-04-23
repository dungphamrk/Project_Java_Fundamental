package ra.edu.business.model.recruitmentPosition;

import ra.edu.business.model.Inputable;
import ra.edu.business.model.technology.Technology;
import ra.edu.validate.RecruitmentPositionValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RecruitmentPosition implements Inputable {
    private int id;
    private String name;
    private String description;
    private double minSalary;
    private double maxSalary;
    private int minExperience;
    private LocalDate createdDate;
    private LocalDate expiredDate;
    private Status status;
    private List<Technology> technologies;

    public RecruitmentPosition() {
        this.technologies = new ArrayList<>();
    }

    public RecruitmentPosition(int id, String name, String description, double minSalary, double maxSalary,
                               int minExperience, LocalDate createdDate, LocalDate expiredDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.minExperience = minExperience;
        this.createdDate = createdDate;
        this.expiredDate = expiredDate;
        this.status = Status.active;
        this.technologies = new ArrayList<>();
    }

    // Getters and Setters

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

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

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(LocalDate expiredDate) {
        this.expiredDate = expiredDate;
    }

    public List<Technology> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<Technology> technologies) {
        this.technologies = technologies;
    }

    @Override
    public void inputData(Scanner scanner) {
        this.name = RecruitmentPositionValidator.inputName(scanner);
        this.description = RecruitmentPositionValidator.inputDescription(scanner);
        this.minSalary = RecruitmentPositionValidator.inputMinSalary(scanner);
        this.maxSalary = RecruitmentPositionValidator.inputMaxSalary(scanner, this.minSalary);
        this.minExperience = RecruitmentPositionValidator.inputMinExperience(scanner);
        this.createdDate = LocalDate.now();
        this.expiredDate = RecruitmentPositionValidator.inputExpiredDate(scanner);
        this.status = Status.active;
    }
}