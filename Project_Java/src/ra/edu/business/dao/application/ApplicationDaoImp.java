package ra.edu.business.dao.application;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.application.Application;
import ra.edu.business.model.application.Progress;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDaoImp implements ApplicationDao {
    private Timestamp toTimestamp(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return Timestamp.valueOf(localDateTime);
    }

    private LocalDateTime toLocalDateTime(Timestamp timestamp) {
        if (timestamp == null) return null;
        return timestamp.toLocalDateTime();
    }

    @Override
    public int save(Application application) {
        if (application == null) {
            throw new IllegalArgumentException("Application cannot be null");
        }
        Connection conn = null;
        CallableStatement callStmt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{call sp_CreateApplication(?,?,?)}");
            callStmt.setInt(1, application.getCandidateId());
            callStmt.setInt(2, application.getRecruitmentPositionId());
            callStmt.setString(3, application.getCvUrl());
            callStmt.execute();
            conn.commit();
            return 0;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                // Log lỗi rollback
            }
            throw new RuntimeException("Lỗi khi tạo đơn ứng tuyển: " + e.getMessage(), e);
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public List<Application> getApplicationsByCandidateId(int candidateId, int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 1) {
            throw new IllegalArgumentException("pageNumber and pageSize must be positive");
        }
        Connection conn = null;
        CallableStatement callStmt = null;
        ResultSet rs = null;
        List<Application> applications = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_GetApplicationsByCandidateId(?,?,?)}");
            callStmt.setInt(1, candidateId);
            callStmt.setInt(2, pageNumber);
            callStmt.setInt(3, pageSize);
            rs = callStmt.executeQuery();
            while (rs.next()) {
                Application app = new Application();
                app.setId(rs.getInt("id"));
                app.setCandidateId(rs.getInt("candidateId"));
                app.setRecruitmentPositionId(rs.getInt("recruitmentPositionId"));
                app.setCvUrl(rs.getString("cvUrl"));
                app.setProgress(Progress.valueOf(rs.getString("progress").toUpperCase()));
                app.setInterviewRequestDate(toLocalDateTime(rs.getTimestamp("interviewRequestDate")));
                app.setInterviewRequestResult(rs.getString("interviewRequestResult"));
                app.setInterviewLink(rs.getString("interviewLink"));
                app.setInterviewTime(toLocalDateTime(rs.getTimestamp("interviewTime")));
                app.setInterviewResult(rs.getString("interviewResult"));
                app.setInterviewResultNote(rs.getString("interviewResultNote"));
                app.setDestroyAt(toLocalDateTime(rs.getTimestamp("destroyAt")));
                app.setCreateAt(toLocalDateTime(rs.getTimestamp("createAt")));
                app.setUpdateAt(toLocalDateTime(rs.getTimestamp("updateAt")));
                app.setDestroyReason(rs.getString("destroyReason"));
                applications.add(app);
            }
            return applications;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách đơn ứng tuyển: " + e.getMessage(), e);
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int cancelApplication(int applicationId, int candidateId, String destroyReason, Progress progress) {
        if (destroyReason == null || destroyReason.trim().isEmpty()) {
            throw new IllegalArgumentException("destroyReason cannot be empty");
        }
        if (progress == null) {
            throw new IllegalArgumentException("progress cannot be null");
        }
        Connection conn = null;
        CallableStatement callStmt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{call sp_CancelApplication(?,?,?,?,?)}");
            callStmt.setInt(1, applicationId);
            callStmt.setInt(2, candidateId);
            callStmt.setString(3, destroyReason);
            callStmt.setString(4, progress.name().toLowerCase());
            callStmt.registerOutParameter(5, Types.INTEGER);
            callStmt.execute();
            int returnCode = callStmt.getInt(5);
            if (returnCode == 0) {
                conn.commit();
            } else {
                conn.rollback();
            }
            return returnCode;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                // Log lỗi rollback
            }
            throw new RuntimeException("Lỗi khi hủy đơn ứng tuyển: " + e.getMessage(), e);
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                // Log lỗi khôi phục auto-commit
            }
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int cancelApplicationByAdmin(int applicationId, String destroyReason, Progress progress) {
        if (destroyReason == null || destroyReason.trim().isEmpty()) {
            throw new IllegalArgumentException("destroyReason cannot be empty");
        }
        if (progress == null) {
            throw new IllegalArgumentException("progress cannot be null");
        }
        Connection conn = null;
        CallableStatement callStmt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{call sp_CancelApplicationByAdmin(?,?,?,?)}");
            callStmt.setInt(1, applicationId);
            callStmt.setString(2, destroyReason);
            callStmt.setString(3, progress.name().toLowerCase());
            callStmt.registerOutParameter(4, Types.INTEGER);
            callStmt.execute();
            int returnCode = callStmt.getInt(4);
            if (returnCode == 0) {
                conn.commit();
            } else {
                conn.rollback();
            }
            return returnCode;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                // Log lỗi rollback
            }
            throw new RuntimeException("Lỗi khi hủy đơn ứng tuyển: " + e.getMessage(), e);
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                // Log lỗi khôi phục auto-commit
            }
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public List<Application> findAll(int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 1) {
            throw new IllegalArgumentException("pageNumber and pageSize must be positive");
        }
        Connection conn = null;
        CallableStatement callStmt = null;
        ResultSet rs = null;
        List<Application> applications = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_FindAllApplications(?,?)}");
            callStmt.setInt(1, pageNumber);
            callStmt.setInt(2, pageSize);
            rs = callStmt.executeQuery();
            while (rs.next()) {
                Application app = new Application();
                app.setId(rs.getInt("id"));
                app.setCandidateId(rs.getInt("candidateId"));
                app.setRecruitmentPositionId(rs.getInt("recruitmentPositionId"));
                app.setCvUrl(rs.getString("cvUrl"));
                app.setProgress(Progress.valueOf(rs.getString("progress").toUpperCase()));
                app.setInterviewRequestDate(toLocalDateTime(rs.getTimestamp("interviewRequestDate")));
                app.setInterviewRequestResult(rs.getString("interviewRequestResult"));
                app.setInterviewLink(rs.getString("interviewLink"));
                app.setInterviewTime(toLocalDateTime(rs.getTimestamp("interviewTime")));
                app.setInterviewResult(rs.getString("interviewResult"));
                app.setInterviewResultNote(rs.getString("interviewResultNote"));
                app.setDestroyAt(toLocalDateTime(rs.getTimestamp("destroyAt")));
                app.setCreateAt(toLocalDateTime(rs.getTimestamp("createAt")));
                app.setUpdateAt(toLocalDateTime(rs.getTimestamp("updateAt")));
                app.setDestroyReason(rs.getString("destroyReason"));
                applications.add(app);
            }
            return applications;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách đơn ứng tuyển: " + e.getMessage(), e);
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public List<Application> findByProgress(Progress progress, int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 1) {
            throw new IllegalArgumentException("pageNumber and pageSize must be positive");
        }
        Connection conn = null;
        CallableStatement callStmt = null;
        ResultSet rs = null;
        List<Application> applications = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_FindApplicationsByProgress(?,?,?)}");
            callStmt.setString(1, progress.name().toLowerCase());
            callStmt.setInt(2, pageNumber);
            callStmt.setInt(3, pageSize);
            rs = callStmt.executeQuery();
            while (rs.next()) {
                Application app = new Application();
                app.setId(rs.getInt("id"));
                app.setCandidateId(rs.getInt("candidateId"));
                app.setRecruitmentPositionId(rs.getInt("recruitmentPositionId"));
                app.setCvUrl(rs.getString("cvUrl"));
                app.setProgress(Progress.valueOf(rs.getString("progress").toUpperCase()));
                app.setInterviewRequestDate(toLocalDateTime(rs.getTimestamp("interviewRequestDate")));
                app.setInterviewRequestResult(rs.getString("interviewRequestResult"));
                app.setInterviewLink(rs.getString("interviewLink"));
                app.setInterviewTime(toLocalDateTime(rs.getTimestamp("interviewTime")));
                app.setInterviewResult(rs.getString("interviewResult"));
                app.setInterviewResultNote(rs.getString("interviewResultNote"));
                app.setDestroyAt(toLocalDateTime(rs.getTimestamp("destroyAt")));
                app.setCreateAt(toLocalDateTime(rs.getTimestamp("createAt")));
                app.setUpdateAt(toLocalDateTime(rs.getTimestamp("updateAt")));
                app.setDestroyReason(rs.getString("destroyReason"));
                applications.add(app);
            }
            return applications;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách đơn ứng tuyển theo trạng thái: " + e.getMessage(), e);
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public Application findById(int id) {
        Connection conn = null;
        CallableStatement callStmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_FindApplicationById(?)}");
            callStmt.setInt(1, id);
            rs = callStmt.executeQuery();
            if (rs.next()) {
                Application app = new Application();
                app.setId(rs.getInt("id"));
                app.setCandidateId(rs.getInt("candidateId"));
                app.setRecruitmentPositionId(rs.getInt("recruitmentPositionId"));
                app.setCvUrl(rs.getString("cvUrl"));
                app.setProgress(Progress.valueOf(rs.getString("progress").toUpperCase()));
                app.setInterviewRequestDate(toLocalDateTime(rs.getTimestamp("interviewRequestDate")));
                app.setInterviewRequestResult(rs.getString("interviewRequestResult"));
                app.setInterviewLink(rs.getString("interviewLink"));
                app.setInterviewTime(toLocalDateTime(rs.getTimestamp("interviewTime")));
                app.setInterviewResult(rs.getString("interviewResult"));
                app.setInterviewResultNote(rs.getString("interviewResultNote"));
                app.setDestroyAt(toLocalDateTime(rs.getTimestamp("destroyAt")));
                app.setCreateAt(toLocalDateTime(rs.getTimestamp("createAt")));
                app.setUpdateAt(toLocalDateTime(rs.getTimestamp("updateAt")));
                app.setDestroyReason(rs.getString("destroyReason"));
                return app;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm đơn ứng tuyển: " + e.getMessage(), e);
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }



    @Override
    public int updateField(Integer id, String fieldName, Object newValue) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{call sp_UpdateApplicationField(?,?,?,?)}");
            callStmt.setInt(1, id);
            callStmt.setString(2, fieldName);
            if (newValue instanceof LocalDateTime) {
                callStmt.setTimestamp(3, toTimestamp((LocalDateTime) newValue));
            } else if (newValue instanceof LocalDate) {
                callStmt.setTimestamp(3, Timestamp.valueOf(((LocalDate) newValue).atStartOfDay()));
            } else {
                callStmt.setString(3, newValue.toString());
            }
            callStmt.registerOutParameter(4, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(4);
            if (returnCode == 0) {
                conn.commit();
            } else {
                conn.rollback();
            }
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi cập nhật trường: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            return 1;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi khôi phục auto-commit: " + ex.getMessage());
            }
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int delete(Integer id) {
        throw new UnsupportedOperationException("Method not implemented");
    }
}