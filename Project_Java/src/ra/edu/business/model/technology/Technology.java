package ra.edu.business.model.technology;
import ra.edu.business.model.Inputable;
import ra.edu.validate.TechnologyValidator;

import java.util.Scanner;

public class Technology implements Inputable {
    private int id;
    private String name;
    private Status status;

    public Technology() {
    }

    public Technology(int id, String name, Status status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public int getId() { return id; }

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
        this.name = TechnologyValidator.inputName(scanner);
        this.status = Status.ACTIVE;
    }
}