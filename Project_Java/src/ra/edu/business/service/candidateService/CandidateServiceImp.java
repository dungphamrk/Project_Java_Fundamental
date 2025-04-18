package ra.edu.business.service.candidateService;


import ra.edu.business.dao.candidate.CandidateDao;
import ra.edu.business.dao.candidate.CandidateDaoImp;

import java.util.Scanner;

public class CandidateServiceImp implements CandidateService {
    private final CandidateDao candidateDao;
    public CandidateServiceImp() {
        this.candidateDao = new CandidateDaoImp();
    }

    @Override
    public int findAll() {
        return candidateDao.findAll();
    }

    @Override
    public int save(Scanner scanner) {
        return candidateDao.save(scanner);
    }

    @Override
    public int update(Scanner scanner) {
        return candidateDao.update(scanner);
    }

    @Override
    public int delete(Scanner scanner) {
        return candidateDao.delete(scanner);
    }



}