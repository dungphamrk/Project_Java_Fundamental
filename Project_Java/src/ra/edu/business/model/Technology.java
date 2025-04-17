package ra.edu.business.model;
import java.util.Scanner;

public class Technology implements Inputable {
    private int id;
    private String name;
    private Status status;

    public enum Status {
        ACTIVE, INACTIVE
    }

    // Constructors
    public Technology() {
    }

    public Technology(int id, String name, Status status) {
        this.id = id;
        this.name = name;
        this.status = status;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public void inputData(Scanner scanner) {
        System.out.println("Nhập tên công nghệ:");
        this.name = scanner.nextLine();
        System.out.println("Nhập trạng thái (active/inactive):");
        this.status = Status.valueOf(scanner.nextLine().toUpperCase());
    }
}