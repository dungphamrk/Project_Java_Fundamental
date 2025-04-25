package ra.edu.business.service.application;

import ra.edu.business.model.application.Application;
import ra.edu.business.model.application.Progress;
import ra.edu.business.service.AppService;

import java.time.LocalDateTime;
import java.util.List;

public interface ApplicationService extends AppService<Application, Integer> {
    List<Application> getApplicationsByCandidateId(int candidateId, int pageNumber, int pageSize);
    int cancelApplication(int applicationId, int candidateId, String destroyReason, Progress progress);
    int cancelApplicationByAdmin(int applicationId, String destroyReason, Progress progress);
    List<Application> findAll(int pageNumber, int pageSize);
    List<Application> findByProgress(Progress progress, int pageNumber, int pageSize);
    int updateInterviewRequest(int applicationId, LocalDateTime interviewRequestDate, String interviewRequestResult);
    int updateInterviewDetails(int applicationId, String interviewLink, LocalDateTime interviewTime);
    int updateInterviewResult(int applicationId, String interviewResult, String interviewResultNote, String progress);
    Application findById(int id);
    int cancelAllApplicationById(int userId);
}