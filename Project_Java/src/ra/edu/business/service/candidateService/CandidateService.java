package ra.edu.business.service.candidateService;

import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.service.AppService;

public interface CandidateService extends AppService<Candidate> {
    int changePassword(int userId, String oldPassword, String newPassword);
}