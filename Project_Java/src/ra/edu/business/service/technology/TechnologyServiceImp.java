package ra.edu.business.service.technology;

import ra.edu.business.dao.technology.TechnologyDao;
import ra.edu.business.dao.technology.TechnologyDaoImp;
import ra.edu.business.model.technology.Technology;

import java.util.List;
import java.util.Scanner;

public class TechnologyServiceImp implements TechnologyService {
    private final TechnologyDao technologyDao;

    public TechnologyServiceImp() {
        this.technologyDao = new TechnologyDaoImp();
    }

    @Override
    public int findAll(int pageNumber, int pageSize) {
        return technologyDao.findAll(pageNumber, pageSize);
    }

    @Override
    public int save(Technology technology) {
        return technologyDao.save(technology);
    }

    @Override
    public int update(Scanner scanner) {
        return technologyDao.update(scanner);
    }

    @Override
    public int delete(Scanner scanner) {
        return technologyDao.delete(scanner);
    }

    @Override
    public Technology findById(int id) {
        return technologyDao.findById(id);
    }

    @Override
    public int searchByName(String keyword, int pageNumber, int pageSize) {
        return technologyDao.searchByName(keyword, pageNumber, pageSize);
    }

    @Override
    public List<Technology> getCandidateTechnologies(int candidateId) {
        return technologyDao.getCandidateTechnologies(candidateId);
    }

    @Override
    public int addCandidateTechnology(int candidateId, int technologyId) {
        return technologyDao.addCandidateTechnology(candidateId, technologyId);
    }

    @Override
    public int removeCandidateTechnology(int candidateId, int technologyId) {
        return technologyDao.removeCandidateTechnology(candidateId, technologyId);
    }
}