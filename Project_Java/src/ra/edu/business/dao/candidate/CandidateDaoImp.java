package ra.edu.business.dao.candidate;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.model.user.User;

import java.sql.*;
import java.util.Scanner;

public class CandidateDaoImp implements CandidateDao {

    @Override
    public int findAll(int pageNumber,int  pageSize) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int count = 0;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_FindAllCandidates()}");

            ResultSet rs = callStmt.executeQuery();
            System.out.println("Danh sách ứng viên:");
            System.out.println("--------------------------------------------------");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Tên: " + rs.getString("name") +
                        ", Email: " + rs.getString("email") +
                        ", Vai trò: " + rs.getString("role"));
                count++;
            }
            if (count == 0) {
                System.out.println("Không có ứng viên nào.");
            }
            return count;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách ứng viên: " + e.getMessage());
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int update(Scanner scanner) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{call sp_UpdateCandidate(?,?,?,?,?,?,?,?,?)}");
            // Giả định nhập dữ liệu từ Scanner hoặc Candidate object
            Candidate candidate = new Candidate();
            candidate.inputData(scanner);
            callStmt.setInt(1, candidate.getId());
            callStmt.setString(2, candidate.getName());
            callStmt.setString(3, candidate.getEmail());
            callStmt.setString(4, candidate.getPhone());
            callStmt.setInt(5, candidate.getExperience());
            callStmt.setString(6, candidate.getGender().name().toLowerCase());
            callStmt.setString(7, candidate.getDescription());
            if (candidate.getDob() != null) {
                callStmt.setDate(8, java.sql.Date.valueOf(candidate.getDob()));
            } else {
                callStmt.setNull(8, Types.DATE);
            }
            callStmt.registerOutParameter(9, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(9);
            conn.commit();
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi cập nhật ứng viên: " + e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int delete(Scanner scanner) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{call sp_DeleteCandidate(?,?)}");
            System.out.println("Nhập ID ứng viên cần xóa:");
            int candidateId = Integer.parseInt(scanner.next());
            callStmt.setInt(1, candidateId);
            callStmt.registerOutParameter(2, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(2);
            conn.commit();
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi xóa ứng viên: " + e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int save(Candidate newCandidate) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{call sp_RegisterCandidate(?,?,?,?,?,?,?,?,?)}");
            callStmt.setInt(1, newCandidate.getId());
            callStmt.setString(2, newCandidate.getName());
            callStmt.setString(3, newCandidate.getEmail());
            callStmt.setString(4, newCandidate.getPhone());
            callStmt.setInt(5, newCandidate.getExperience());
            callStmt.setString(6, newCandidate.getGender().name().toLowerCase());
            callStmt.setString(7, newCandidate.getDescription());
            if (newCandidate.getDob() != null) {
                callStmt.setDate(8, java.sql.Date.valueOf(newCandidate.getDob()));
            } else {
                callStmt.setNull(8, Types.DATE);
            }
            callStmt.registerOutParameter(9, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(9);
            conn.commit();
        } catch (SQLException e) {
            System.err.println("Có lỗi SQL khi đăng ký: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi khác khi đăng ký: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
        return returnCode;
    }
}
