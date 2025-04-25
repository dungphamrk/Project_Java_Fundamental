package ra.edu.business.dao.application;

import ra.edu.business.dao.AppDao;
import ra.edu.business.model.application.Application;
import ra.edu.business.model.application.Progress;

import java.util.List;

public interface ApplicationDao extends AppDao<Application, Integer> {
    List<Application> getApplicationsByCandidateId(int candidateId, int pageNumber, int pageSize);
    int cancelApplication(int applicationId, int candidateId, String destroyReason, Progress progress);
    int cancelApplicationByAdmin(int applicationId, String destroyReason, Progress progress);
    List<Application> findByProgress(Progress progress, int pageNumber, int pageSize);
    Application findById(int id);
    int cancelAllApplicationById(int userId);
}