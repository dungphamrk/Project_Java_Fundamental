package ra.edu.business.service.recruitmentPositionService;

import ra.edu.business.dao.recruitmentPosition.RecruitmentPositionDao;
import ra.edu.business.dao.recruitmentPosition.RecruitmentPositionDaoImp;
import ra.edu.business.model.recruitmentPosition.RecruitmentPosition;
import ra.edu.business.model.recruitmentPosition.Status;
import ra.edu.business.model.technology.Technology;

import java.time.LocalDate;
import java.util.List;

public class RecruitmentPositionServiceImp implements RecruitmentPositionService {
    private final RecruitmentPositionDao recruitmentPositionDao;

    public RecruitmentPositionServiceImp() {
        this.recruitmentPositionDao = new RecruitmentPositionDaoImp();
    }

    @Override
    public List<RecruitmentPosition> findAll(int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 1) {
            return List.of();
        }
        return recruitmentPositionDao.findAll(pageNumber, pageSize);
    }

    @Override
    public List<RecruitmentPosition> getAllPositions(int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 1) {
            return List.of();
        }
        List<RecruitmentPosition> positions = recruitmentPositionDao.getAllPositions(pageNumber, pageSize);
        // Lấy danh sách công nghệ cho từng vị trí
        for (RecruitmentPosition position : positions) {
            position.setTechnologies(recruitmentPositionDao.getPositionTechnologies(position.getId()));
        }
        return positions;
    }

    @Override
    public List<RecruitmentPosition> getActivePositions(int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 1) {
            return List.of();
        }
        List<RecruitmentPosition> positions = recruitmentPositionDao.getActivePositions(pageNumber, pageSize);
        // Lấy danh sách công nghệ cho từng vị trí
        for (RecruitmentPosition position : positions) {
            position.setTechnologies(recruitmentPositionDao.getPositionTechnologies(position.getId()));
        }
        return positions;
    }

    @Override
    public boolean isPositionActive(int positionId) {
        if (positionId <= 0) {
            return false;
        }
        return recruitmentPositionDao.isPositionActive(positionId);
    }

    @Override
    public int save(RecruitmentPosition position) {
        if (position.getName() == null || position.getName().trim().isEmpty()) {
            return 1; // Tên không hợp lệ
        }
        if (position.getMinSalary() < 0 || position.getMaxSalary() < position.getMinSalary()) {
            return 2; // Lương không hợp lệ
        }
        if (position.getMinExperience() < 0) {
            return 3; // Kinh nghiệm không hợp lệ
        }
        if (position.getStatus() == null) {
            return 4; // Trạng thái không hợp lệ
        }
        if (position.getTechnologies() == null || position.getTechnologies().isEmpty()) {
            return 8; // Phải có ít nhất một công nghệ
        }

        // Gọi DAO để lưu vị trí và công nghệ
        return recruitmentPositionDao.save(position);
    }

    @Override
    public int updateField(Integer id, String fieldName, Object newValue) {
        if (id <= 0) {
            return 1; // ID không hợp lệ
        }
        if (newValue == null || (newValue instanceof String && ((String) newValue).trim().isEmpty())) {
            return 2; // Giá trị rỗng
        }
        if (!List.of("name", "description", "minSalary", "maxSalary", "minExperience", "expiredDate", "status")
                .contains(fieldName)) {
            return 3; // Trường không hợp lệ
        }
        if (fieldName.equals("minSalary") || fieldName.equals("maxSalary")) {
            try {
                double salary = Double.parseDouble(newValue.toString());
                if (salary < 0) {
                    return 4; // Lương không hợp lệ
                }
            } catch (NumberFormatException e) {
                return 4; // Lương không hợp lệ
            }
        }
        if (fieldName.equals("minExperience")) {
            try {
                int experience = Integer.parseInt(newValue.toString());
                if (experience < 0) {
                    return 5; // Kinh nghiệm không hợp lệ
                }
            } catch (NumberFormatException e) {
                return 5; // Kinh nghiệm không hợp lệ
            }
        }
        if (fieldName.equals("status")) {
            try {
                Status.valueOf(newValue.toString());
            } catch (IllegalArgumentException e) {
                return 6;
            }
        }
        if (fieldName.equals("expiredDate") && !(newValue instanceof LocalDate)) {
            return 7; // Ngày hết hạn không hợp lệ
        }
        return recruitmentPositionDao.updateField(id, fieldName, newValue);
    }

    @Override
    public int delete(Integer id) {
        if (id <= 0) {
            return 1;
        }
        if (!recruitmentPositionDao.isPositionActive(id)) {
            return 2;
        }
        return updateField(id, "status", Status.inactive);
    }

    @Override
    public List<Technology> getPositionTechnologies(Integer positionId) {
        if (positionId < 0) {
            return List.of();
        }
        return recruitmentPositionDao.getPositionTechnologies(positionId);
    }

    @Override
    public int addPositionTechnology(Integer positionId, Integer technologyId) {
        if (positionId <= 0 || technologyId <= 0) {
            return 1; // ID không hợp lệ
        }
        if (!recruitmentPositionDao.isPositionActive(positionId)) {
            return 2; // Vị trí không tồn tại hoặc đã inactive
        }
        return recruitmentPositionDao.addPositionTechnology(positionId, technologyId);
    }

    @Override
    public int removePositionTechnology(Integer positionId, Integer technologyId) {
        if (positionId <= 0 || technologyId <= 0) {
            return 1; // ID không hợp lệ
        }
        return recruitmentPositionDao.removePositionTechnology(positionId, technologyId);
    }

    @Override
    public int getTotalPositionsCount() {
        return recruitmentPositionDao.getTotalPositionsCount();
    }

    @Override
    public int getActivePositionsCount() {
        return recruitmentPositionDao.getActivePositionsCount();
    }

    @Override
    public List<RecruitmentPosition> getFilteredPositionsByTechnologies(List<Integer> technologyIds, int pageNumber, int pageSize) {
        if (technologyIds == null || technologyIds.isEmpty() || pageNumber < 1 || pageSize < 1) {
            return List.of();
        }
        List<RecruitmentPosition> positions = recruitmentPositionDao.getFilteredPositionsByTechnologies(technologyIds, pageNumber, pageSize);
        for (RecruitmentPosition position : positions) {
            position.setTechnologies(recruitmentPositionDao.getPositionTechnologies(position.getId()));
        }
        return positions;
    }

    @Override
    public int getFilteredPositionsCountByTechnologies(List<Integer> technologyIds) {
        if (technologyIds == null || technologyIds.isEmpty()) {
            return 0;
        }
        return recruitmentPositionDao.getFilteredPositionsCountByTechnologies(technologyIds);
    }
}