package ra.edu.business.service;

import java.util.Scanner;

public interface AppService<T> {
    int findAll(int pageNumber, int pageSize);
    int save(T obj);
    int update(Scanner scanner);
    int delete(Scanner scanner);
}
