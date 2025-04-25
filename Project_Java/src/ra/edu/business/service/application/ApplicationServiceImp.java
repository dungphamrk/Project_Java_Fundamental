package ra.edu.business.service.application;

import ra.edu.business.dao.application.ApplicationDao;
import ra.edu.business.dao.application.ApplicationDaoImp;
import ra.edu.business.model.application.Application;
import ra.edu.business.model.application.Progress;

import java.time.LocalDateTime;
import java.util.List;

public class ApplicationServiceImp implements ApplicationService {
    private final ApplicationDao applicationDao;

    public ApplicationServiceImp() {
        this.applicationDao = new ApplicationDaoImp();
    }

    @Override
    public int save(Application application) {
        if (application == null) {
            throw new IllegalArgumentException("Application cannot be null");
        }
        if (application.getCvUrl() == null) {
            throw new IllegalArgumentException("Invalid CV URL");
        }
        try {
            return applicationDao.save(application);
        } catch (RuntimeException e) {
            throw new RuntimeException("Không thể lưu đơn ứng tuyển: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Application> getApplicationsByCandidateId(int candidateId, int pageNumber, int pageSize) {
        if (candidateId <= 0) {
            throw new IllegalArgumentException("candidateId must be positive");
        }
        try {
            return applicationDao.getApplicationsByCandidateId(candidateId, pageNumber, pageSize);
        } catch (RuntimeException e) {
            throw new RuntimeException("Không thể lấy danh sách đơn ứng tuyển: " + e.getMessage(), e);
        }
    }

    @Override
    public int cancelApplication(int applicationId, int candidateId, String destroyReason, Progress progress) {
        if (applicationId <= 0 || candidateId <= 0) {
            throw new IllegalArgumentException("applicationId and candidateId must be positive");
        }
        if (destroyReason == null || destroyReason.trim().isEmpty()) {
            throw new IllegalArgumentException("destroyReason cannot be empty");
        }
        if (progress == null) {
            throw new IllegalArgumentException("progress cannot be null");
        }
        try {
            return applicationDao.cancelApplication(applicationId, candidateId, destroyReason, progress);
        } catch (RuntimeException e) {
            throw new RuntimeException("Không thể hủy đơn ứng tuyển: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Application> findAll(int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 1) {
            throw new IllegalArgumentException("pageNumber and pageSize must be positive");
        }
        try {
            return applicationDao.findAll(pageNumber, pageSize);
        } catch (RuntimeException e) {
            throw new RuntimeException("Không thể lấy danh sách đơn ứng tuyển: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Application> findByProgress(Progress progress, int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 1) {
            throw new IllegalArgumentException("pageNumber and pageSize must be positive");
        }
        try {
            return applicationDao.findByProgress(progress, pageNumber, pageSize);
        } catch (RuntimeException e) {
            throw new RuntimeException("Không thể lấy danh sách đơn ứng tuyển theo trạng thái: " + e.getMessage(), e);
        }
    }

    @Override
    public int cancelApplicationByAdmin(int applicationId, String destroyReason, Progress progress) {
        if (applicationId <= 0) {
            throw new IllegalArgumentException("applicationId must be positive");
        }
        if (destroyReason == null || destroyReason.trim().isEmpty()) {
            throw new IllegalArgumentException("destroyReason cannot be empty");
        }
        if (progress == null) {
            throw new IllegalArgumentException("progress cannot be null");
        }
        try {
            return applicationDao.cancelApplicationByAdmin(applicationId, destroyReason, progress);
        } catch (RuntimeException e) {
            throw new RuntimeException("Không thể hủy đơn ứng tuyển: " + e.getMessage(), e);
        }
    }

    @Override
    public int updateInterviewRequest(int applicationId, LocalDateTime interviewRequestDate, String interviewRequestResult) {
        if (applicationId <= 0) {
            throw new IllegalArgumentException("applicationId must be positive");
        }
        if (interviewRequestResult == null || interviewRequestResult.trim().isEmpty()) {
            throw new IllegalArgumentException("interviewRequestResult cannot be empty");
        }
        try {
            int result1 = applicationDao.updateField(applicationId, "interviewRequestDate", interviewRequestDate);
            int result2 = applicationDao.updateField(applicationId, "interviewRequestResult", interviewRequestResult);
            return result1 == 0 && result2 == 0 ? 0 : 1;
        } catch (RuntimeException e) {
            throw new RuntimeException("Không thể cập nhật yêu cầu phỏng vấn: " + e.getMessage(), e);
        }
    }

    @Override
    public int updateInterviewDetails(int applicationId, String interviewLink, LocalDateTime interviewTime) {
        if (applicationId <= 0) {
            throw new IllegalArgumentException("applicationId must be positive");
        }
        if (interviewLink == null || interviewLink.trim().isEmpty()) {
            throw new IllegalArgumentException("interviewLink cannot be empty");
        }
        try {
            int result1 = applicationDao.updateField(applicationId, "interviewLink", interviewLink);
            int result2 = applicationDao.updateField(applicationId, "interviewTime", interviewTime);
            return result1 == 0 && result2 == 0 ? 0 : 1;
        } catch (RuntimeException e) {
            throw new RuntimeException("Không thể cập nhật chi tiết phỏng vấn: " + e.getMessage(), e);
        }
    }

    @Override
    public int updateInterviewResult(int applicationId, String interviewResult, String interviewResultNote, String progress) {
        if (applicationId <= 0) {
            throw new IllegalArgumentException("applicationId must be positive");
        }
        if (progress == null || progress.trim().isEmpty()) {
            throw new IllegalArgumentException("progress cannot be empty");
        }
        try {
            int result1 = applicationDao.updateField(applicationId, "interviewResult", interviewResult);
            int result2 = applicationDao.updateField(applicationId, "interviewResultNote", interviewResultNote);
            int result3 = applicationDao.updateField(applicationId, "progress", progress.toLowerCase());
            return result1 == 0 && result2 == 0 && result3 == 0 ? 0 : 1;
        } catch (RuntimeException e) {
            throw new RuntimeException("Không thể cập nhật kết quả phỏng vấn: " + e.getMessage(), e);
        }
    }

    @Override
    public Application findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id must be positive");
        }
        try {
            return applicationDao.findById(id);
        } catch (RuntimeException e) {
            throw new RuntimeException("Không thể tìm đơn ứng tuyển: " + e.getMessage(), e);
        }
    }

    @Override
    public int cancelAllApplicationById(int userId) {
        return applicationDao.cancelAllApplicationById(userId);
    }


    @Override
    public int updateField(Integer id, String fieldName, Object newValue) {
        try {
            return applicationDao.updateField(id, fieldName, newValue);
        } catch (RuntimeException e) {
            throw new RuntimeException("Không thể cập nhật trường: " + e.getMessage(), e);
        }
    }

    @Override
    public int delete(Integer id) {
        try {
            return applicationDao.delete(id);
        } catch (RuntimeException e) {
            throw new RuntimeException("Không thể xóa đơn ứng tuyển: " + e.getMessage(), e);
        }
    }
}