package ra.edu.business.model.candidate;
import ra.edu.business.model.Inputable;
import ra.edu.validate.CandidateValidator;
import java.time.LocalDate;
import java.util.Scanner;

public class Candidate implements Inputable {
    private int id;
    private String name;
    private String email;
    private String phone;
    private int experience;
    private Gender gender;
    private String description;
    private LocalDate dob;

    // Constructors
    public Candidate() {
    }

    public Candidate(int id, String name, String email, String phone, int experience,
                     Gender gender, String description, LocalDate dob) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.experience = experience;
        this.gender = gender;
        this.description = description;
        this.dob = dob;
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



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }


    @Override
    public void inputData(Scanner scanner) {
        this.name = CandidateValidator.inputName(scanner);
        this.email = CandidateValidator.inputEmail(scanner);
        this.phone = CandidateValidator.inputPhone(scanner);
        this.experience = CandidateValidator.inputExperience(scanner);
        this.gender = CandidateValidator.inputGender(scanner);
        this.description = CandidateValidator.inputDescription(scanner);
        this.dob = CandidateValidator.inputDob(scanner);
    }
}