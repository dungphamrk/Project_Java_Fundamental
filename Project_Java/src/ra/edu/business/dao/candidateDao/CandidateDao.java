package ra.edu.business.dao.candidateDao;

import ra.edu.business.dao.AppDao;
import ra.edu.business.model.Candidate;

public interface CandidateDao extends AppDao {
    int login(String username,String password);
    void logout();
    String isLoggedIn();
    int register(Candidate candidate);
}
