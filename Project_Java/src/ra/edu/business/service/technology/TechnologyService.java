package ra.edu.business.service.technology;

import ra.edu.business.model.technology.Technology;
import ra.edu.business.service.AppService;

import java.util.List;

public interface TechnologyService extends AppService<Technology, Integer> {
    Technology findById(Integer id);
    List<Technology> searchByName(String keyword, int pageNumber, int pageSize);
    List<Technology> getCandidateTechnologies(Integer candidateId);
    int addCandidateTechnology(Integer candidateId, Integer technologyId);
    int removeCandidateTechnology(Integer candidateId, Integer technologyId);
    List<Technology> findAllTechnologiesByAdmin(int pageNumber, int pageSize);
    int updateField(Integer id, String fieldName, Object newValue);
    List<Technology> findAllTechnologiesByCandidates(int pageNumber, int pageSize);
}
