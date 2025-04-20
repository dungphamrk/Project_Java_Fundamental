package ra.edu.business.dao.technology;

import ra.edu.business.dao.AppDao;
import ra.edu.business.model.technology.Technology;

import java.util.List;

public interface TechnologyDao extends AppDao<Technology> {
    Technology findById(int id);
    int searchByName(String keyword, int pageNumber, int pageSize);
    List<Technology> getCandidateTechnologies(int candidateId);
    int addCandidateTechnology(int candidateId, int technologyId);
    int removeCandidateTechnology(int candidateId, int technologyId);
}