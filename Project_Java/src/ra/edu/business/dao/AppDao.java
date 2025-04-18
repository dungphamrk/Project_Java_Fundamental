package ra.edu.business.dao;

import java.util.Scanner;

public interface AppDao {
    public int findAll() ;
    public int save(Scanner scanner);
    public int update(Scanner scanner);
    public int delete(Scanner scanner);

}
