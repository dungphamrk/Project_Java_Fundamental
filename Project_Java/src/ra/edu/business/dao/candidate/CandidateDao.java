package ra.edu.business.dao.candidate;

import ra.edu.business.dao.AppDao;
import ra.edu.business.model.candidate.Candidate;

import java.util.List;

public interface CandidateDao extends AppDao<Candidate, Integer> {
    int changePassword(Integer userId, String oldPassword, String newPassword, String phone);
    boolean lockUnlockAccount(int candidateId);
    List<Candidate> searchByName(String name, int pageNumber, int pageSize); // Thêm phân trang
    List<Candidate> filterByExperience(int experience, int pageNumber, int pageSize); // Thêm phân trang
    List<Candidate> filterByAge(int age, int pageNumber, int pageSize); // Thêm phân trang
    List<Candidate> filterByGender(String gender, int pageNumber, int pageSize); // Thêm phân trang
    List<Candidate> filterByTechnology(String technology, int pageNumber, int pageSize); // Thêm phân trang
    int resetPassword(int userId, String newPassword);
    Candidate getCandidateById(int userId);
    int getTotalCandidatesCount(); // Thêm để lấy tổng số ứng viên
    int getTotalCandidatesByName(String name); // Thêm để lấy tổng số ứng viên theo tên
    int getTotalCandidatesByExperience(int experience); // Thêm để lấy tổng số ứng viên theo kinh nghiệm
    int getTotalCandidatesByAge(int age); // Thêm để lấy tổng số ứng viên theo tuổi
    int getTotalCandidatesByGender(String gender); // Thêm để lấy tổng số ứng viên theo giới tính
    int getTotalCandidatesByTechnology(String technology); // Thêm để lấy tổng số ứng viên theo công nghệ
}