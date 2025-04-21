package ra.edu.business.service.candidateService;

import ra.edu.business.dao.candidate.CandidateDao;
import ra.edu.business.dao.candidate.CandidateDaoImp;
import ra.edu.business.model.candidate.Candidate;

import java.util.Scanner;

public class CandidateServiceImp implements CandidateService {
    private final CandidateDao candidateDao;

    public CandidateServiceImp() {
        this.candidateDao = new CandidateDaoImp();
    }

    @Override
    public int findAll(int pageNumber, int pageSize) {
        return candidateDao.findAll(pageNumber, pageSize);
    }

    @Override
    public int save(Candidate candidate) {
        return candidateDao.save(candidate);
    }

    @Override
    public int update(Scanner scanner) {
        return candidateDao.update(scanner);
    }

    @Override
    public int delete(Scanner scanner) {
        return candidateDao.delete(scanner);
    }

    @Override
    public int changePassword(int userId, String oldPassword, String newPassword) {
        return candidateDao.changePassword(userId, oldPassword, newPassword);
    }

    @Override
    public boolean lockUnlockAccount(int candidateId) {
        return candidateDao.lockUnlockAccount(candidateId, false); // lockStatus không được sử dụng
    }

    @Override
    public boolean searchByName(String name) {
        return candidateDao.searchByName(name);
    }

    @Override
    public boolean filterByExperience(int experience) {
        return candidateDao.filterByExperience(experience);
    }

    @Override
    public boolean filterByAge(int age) {
        return candidateDao.filterByAge(age);
    }

    @Override
    public boolean filterByGender(String gender) {
        return candidateDao.filterByGender(gender);
    }

    @Override
    public boolean filterByTechnology(String technology) {
        return candidateDao.filterByTechnology(technology);
    }
}