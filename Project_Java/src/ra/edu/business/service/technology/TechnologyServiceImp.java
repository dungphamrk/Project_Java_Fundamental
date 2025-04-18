package ra.edu.business.service.technology;
import ra.edu.business.dao.technology.TechnologyDao;
import ra.edu.business.dao.technology.TechnologyDaoImp;
import java.util.Scanner;

public class TechnologyServiceImp implements TechnologyService {
    private final TechnologyDao technologyDao;

    public TechnologyServiceImp() {
        this.technologyDao = new TechnologyDaoImp();
    }

    @Override
    public int findAll() {
        return technologyDao.findAll();
    }

    @Override
    public int save(Scanner scanner) {
        return technologyDao.save(scanner);
    }

    @Override
    public int update(Scanner scanner) {
        return technologyDao.update(scanner);
    }

    @Override
    public int delete(Scanner scanner) {
        return technologyDao.delete(scanner);
    }
}