package ra.edu.business.dao.technology;

import ra.edu.business.dao.AppDao;
import ra.edu.business.model.recruitmentPosition.RecruitmentPosition;
import ra.edu.business.model.technology.Technology;

import java.util.List;

public interface TechnologyDao extends AppDao<Technology, Integer> {
    Technology findById(Integer id);
    List<Technology> searchByName(String keyword, int pageNumber, int pageSize);
    List<Technology> getCandidateTechnologies(Integer candidateId);
    int addCandidateTechnology(Integer candidateId, Integer technologyId);
    int removeCandidateTechnology(Integer candidateId, Integer technologyId);
    int updateField(Integer id, String fieldName, Object newValue);
    List<Technology> findAllTechnologiesByCandidate(int pageNumber, int pageSize);
}