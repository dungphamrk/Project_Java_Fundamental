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
    public int findAll(int pageNumber,int  pageSize) {
        return candidateDao.findAll(pageNumber,pageSize);
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

}