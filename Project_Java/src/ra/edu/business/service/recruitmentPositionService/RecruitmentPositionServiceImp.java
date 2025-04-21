package ra.edu.business.service.recruitmentPositionService;

import ra.edu.business.dao.recruitmentPosition.RecruitmentPositionDao;
import ra.edu.business.dao.recruitmentPosition.RecruitmentPositionDaoImp;
import ra.edu.business.model.recruitmentPosition.RecruitmentPosition;

import java.util.Scanner;

public class RecruitmentPositionServiceImp implements RecruitmentPositionService {
    private final RecruitmentPositionDao recruitmentPositionDao;

    public RecruitmentPositionServiceImp() {
        this.recruitmentPositionDao = new RecruitmentPositionDaoImp();
    }

    @Override
    public int findAll(int pageNumber, int pageSize) {
        return recruitmentPositionDao.findAll(pageNumber, pageSize);
    }

    @Override
    public int save(RecruitmentPosition position) {
        return recruitmentPositionDao.save(position);
    }

    @Override
    public int update(Scanner scanner) {
        return recruitmentPositionDao.update(scanner);
    }

    @Override
    public int delete(Scanner scanner) {
        return recruitmentPositionDao.delete(scanner);
    }
}