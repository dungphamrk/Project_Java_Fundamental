package ra.edu.business.service.candidateService;

import ra.edu.business.dao.candidate.CandidateDao;
import ra.edu.business.dao.candidate.CandidateDaoImp;
import ra.edu.business.model.candidate.Candidate;

import java.security.SecureRandom;
import java.util.List;

public class CandidateServiceImp implements CandidateService {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
    private static final int PASSWORD_LENGTH = 12;
    private final CandidateDao candidateDao;

    public CandidateServiceImp() {
        this.candidateDao = new CandidateDaoImp();
    }

    @Override
    public List<Candidate> findAll(int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 1) {
            return List.of();
        }
        return candidateDao.findAll(pageNumber, pageSize);
    }

    @Override
    public int save(Candidate candidate) {
        if (candidate.getName() == null || candidate.getName().trim().isEmpty()) {
            return 1; // Mã lỗi: tên không hợp lệ
        }
        if (candidate.getEmail() == null || !candidate.getEmail().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return 2; // Mã lỗi: email không hợp lệ
        }
        return candidateDao.save(candidate);
    }

    @Override
    public int updateField(Integer userId, String fieldChoice, Object newValue) {
        String fieldName;
        switch (fieldChoice) {
            case "1":
                fieldName = "name";
                break;
            case "2":
                fieldName = "email";
                break;
            case "3":
                fieldName = "phone";
                break;
            case "4":
                fieldName = "experience";
                break;
            case "5":
                fieldName = "gender";
                break;
            case "6":
                fieldName = "dob";
                break;
            case "7":
                fieldName = "description";
                break;
            default:
                return 6; // Mã lỗi: trường không hợp lệ
        }

        if (newValue == null || newValue.toString().trim().isEmpty()) {
            return 1; // Mã lỗi: giá trị rỗng
        }
        switch (fieldName) {
            case "email":
                if (!newValue.toString().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    return 2; // Mã lỗi: email không hợp lệ
                }
                break;
            case "experience":
                try {
                    Integer.parseInt(newValue.toString());
                } catch (NumberFormatException e) {
                    return 3; // Mã lỗi: kinh nghiệm không phải số
                }
                break;
            case "gender":
                if (!newValue.toString().equalsIgnoreCase("male") && !newValue.toString().equalsIgnoreCase("female")) {
                    return 4; // Mã lỗi: giới tính không hợp lệ
                }
                break;
            case "dob":
                try {
                    java.sql.Date.valueOf(newValue.toString());
                } catch (IllegalArgumentException e) {
                    return 5; // Mã lỗi: ngày sinh không hợp lệ
                }
                break;
        }
        return candidateDao.updateField(userId, fieldName, newValue);
    }

    @Override
    public int delete(Integer id) {
        Candidate candidate = candidateDao.getCandidateById(id);
        if (candidate == null) {
            return 1; // Mã lỗi: không tìm thấy ứng viên
        }
        return candidateDao.delete(id);
    }

    @Override
    public int changePassword(Integer userId, String oldPassword, String newPassword, String phone) {
        if (newPassword == null || newPassword.length() < 6) {
            return 3; // Mã lỗi: mật khẩu mới không hợp lệ
        }
        return candidateDao.changePassword(userId, oldPassword, newPassword, phone);
    }

    @Override
    public boolean lockUnlockAccount(Integer candidateId) {
        Candidate candidate = candidateDao.getCandidateById(candidateId);
        if (candidate == null) {
            return false;
        }
        return candidateDao.lockUnlockAccount(candidateId);
    }

    @Override
    public int resetPassword(Integer userId, String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            return 1; // Mã lỗi: mật khẩu mới không hợp lệ
        }
        return candidateDao.resetPassword(userId, newPassword);
    }

    @Override
    public List<Candidate> searchByName(String name, int pageNumber, int pageSize) {
        if (name == null || name.trim().isEmpty() || pageNumber < 1 || pageSize < 1) {
            return List.of();
        }
        return candidateDao.searchByName(name, pageNumber, pageSize);
    }

    @Override
    public List<Candidate> filterByExperience(int experience, int pageNumber, int pageSize) {
        if (experience < 0 || pageNumber < 1 || pageSize < 1) {
            return List.of();
        }
        return candidateDao.filterByExperience(experience, pageNumber, pageSize);
    }

    @Override
    public List<Candidate> filterByAge(int age, int pageNumber, int pageSize) {
        if (age < 18 || pageNumber < 1 || pageSize < 1) {
            return List.of();
        }
        return candidateDao.filterByAge(age, pageNumber, pageSize);
    }

    @Override
    public List<Candidate> filterByGender(String gender, int pageNumber, int pageSize) {
        if (gender == null || (!gender.equalsIgnoreCase("male") && !gender.equalsIgnoreCase("female")) || pageNumber < 1 || pageSize < 1) {
            return List.of();
        }
        return candidateDao.filterByGender(gender, pageNumber, pageSize);
    }

    @Override
    public List<Candidate> filterByTechnology(String technology, int pageNumber, int pageSize) {
        if (technology == null || technology.trim().isEmpty() || pageNumber < 1 || pageSize < 1) {
            return List.of();
        }
        return candidateDao.filterByTechnology(technology, pageNumber, pageSize);
    }

    @Override
    public Candidate getCandidateById(Integer userId) {
        return candidateDao.getCandidateById(userId);
    }

    @Override
    public String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }

    @Override
    public int getTotalCandidatesCount() {
        return candidateDao.getTotalCandidatesCount();
    }

    @Override
    public int getTotalCandidatesByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return 0;
        }
        return candidateDao.getTotalCandidatesByName(name);
    }

    @Override
    public int getTotalCandidatesByExperience(int experience) {
        if (experience < 0) {
            return 0;
        }
        return candidateDao.getTotalCandidatesByExperience(experience);
    }

    @Override
    public int getTotalCandidatesByAge(int age) {
        if (age < 18) {
            return 0;
        }
        return candidateDao.getTotalCandidatesByAge(age);
    }

    @Override
    public int getTotalCandidatesByGender(String gender) {
        if (gender == null || (!gender.equalsIgnoreCase("male") && !gender.equalsIgnoreCase("female"))) {
            return 0;
        }
        return candidateDao.getTotalCandidatesByGender(gender);
    }

    @Override
    public int getTotalCandidatesByTechnology(String technology) {
        if (technology == null || technology.trim().isEmpty()) {
            return 0;
        }
        return candidateDao.getTotalCandidatesByTechnology(technology);
    }
}