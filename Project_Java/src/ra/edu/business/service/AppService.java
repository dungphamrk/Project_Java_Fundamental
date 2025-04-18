package ra.edu.business.service;

import java.util.Scanner;

public interface AppService {
    int findAll();
    int save(Scanner scanner);
    int update(Scanner scanner);
    int delete(Scanner scanner);
}
