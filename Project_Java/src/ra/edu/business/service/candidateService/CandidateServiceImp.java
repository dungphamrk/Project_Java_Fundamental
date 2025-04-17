package ra.edu.business.service.candidateService;


import ra.edu.business.dao.candidateDao.CandidateDao;
import ra.edu.business.dao.candidateDao.CandidateDaoImp;
import ra.edu.business.model.Candidate;

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
    public int save() {
        return candidateDao.save();
    }

    @Override
    public int update() {
        return candidateDao.update();
    }

    @Override
    public int delete() {
        return candidateDao.delete();
    }


    @Override
    public int register(Candidate candidate) {
        return candidateDao.register(candidate);
    }

    @Override
    public int login(String username, String password) {
        return candidateDao.login(username, password);
    }

    @Override
    public void logout() {
        candidateDao.logout();
    }

    @Override
    public String isLoggedIn() {
        return candidateDao.isLoggedIn();
    }
}