package ra.edu.business.service.candidateService;

import ra.edu.business.model.candidate.Candidate;

import java.util.Scanner;

public interface CandidateService {
    int findAll(int pageNumber, int pageSize);
    int save(Candidate candidate);
    int update(Scanner scanner);
    int delete(Scanner scanner);
    int changePassword(int userId, String oldPassword, String newPassword);
    boolean lockUnlockAccount(int candidateId);
    boolean searchByName(String name);
    boolean filterByExperience(int experience);
    boolean filterByAge(int age);
    boolean filterByGender(String gender);
    boolean filterByTechnology(String technology);
}