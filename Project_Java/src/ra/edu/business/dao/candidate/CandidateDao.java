package ra.edu.business.dao.candidate;

import ra.edu.business.dao.AppDao;
import ra.edu.business.model.candidate.Candidate;

public interface CandidateDao extends AppDao<Candidate> {
    int changePassword(int userId, String oldPassword, String newPassword);
    boolean lockUnlockAccount(int candidateId, boolean lockStatus);
    boolean searchByName(String name);
    boolean filterByExperience(int experience);
    boolean filterByAge(int age);
    boolean filterByGender(String gender);
    boolean filterByTechnology(String technology);
}