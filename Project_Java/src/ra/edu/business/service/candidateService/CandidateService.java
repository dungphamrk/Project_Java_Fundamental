package ra.edu.business.service.candidateService;

import ra.edu.business.model.Candidate;
public interface CandidateService {
    int findAll();
    int save();
    int update();
    int delete();
    int register(Candidate candidate);
    int login(String username, String password);
    void logout();
    String isLoggedIn();
}