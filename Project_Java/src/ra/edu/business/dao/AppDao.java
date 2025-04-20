package ra.edu.business.dao;

import java.util.Scanner;

public interface AppDao<T> {
    public int findAll(int pageNumber, int pageSize) ;
    public int save(T obj);
    public int update(Scanner scanner);
    public int delete(Scanner scanner);

}
