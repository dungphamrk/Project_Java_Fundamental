package ra.edu.business.service.candidateService;

import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.service.AppService;

import java.util.List;

public interface CandidateService extends AppService<Candidate, Integer> {
    int changePassword(Integer userId, String oldPassword, String newPassword);
    boolean lockUnlockAccount(Integer candidateId);
    List<Candidate> searchByName(String name);
    List<Candidate> filterByExperience(int experience);
    List<Candidate> filterByAge(int age);
    List<Candidate> filterByGender(String gender);
    List<Candidate> filterByTechnology(String technology);
    int resetPassword(Integer userId, String newPassword);
    Candidate getCandidateById(Integer userId);
    String generateRandomPassword();
}