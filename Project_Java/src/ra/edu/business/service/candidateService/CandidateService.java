package ra.edu.business.service.candidateService;

import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.service.AppService;

import java.util.List;

public interface CandidateService extends AppService<Candidate, Integer> {
    int changePassword(Integer userId, String oldPassword, String newPassword, String phone);
    boolean lockUnlockAccount(Integer candidateId);
    List<Candidate> searchByName(String name, int pageNumber, int pageSize); // Thêm phân trang
    List<Candidate> filterByExperience(int experience, int pageNumber, int pageSize); // Thêm phân trang
    List<Candidate> filterByAge(int age, int pageNumber, int pageSize); // Thêm phân trang
    List<Candidate> filterByGender(String gender, int pageNumber, int pageSize); // Thêm phân trang
    List<Candidate> filterByTechnology(String technology, int pageNumber, int pageSize); // Thêm phân trang
    int resetPassword(Integer userId, String newPassword);
    Candidate getCandidateById(Integer userId);
    String generateRandomPassword();
    int getTotalCandidatesCount(); // Thêm để lấy tổng số ứng viên
    int getTotalCandidatesByName(String name); // Thêm để lấy tổng số ứng viên theo tên
    int getTotalCandidatesByExperience(int experience); // Thêm để lấy tổng số ứng viên theo kinh nghiệm
    int getTotalCandidatesByAge(int age); // Thêm để lấy tổng số ứng viên theo tuổi
    int getTotalCandidatesByGender(String gender); // Thêm để lấy tổng số ứng viên theo giới tính
    int getTotalCandidatesByTechnology(String technology); // Thêm để lấy tổng số ứng viên theo công nghệ
}