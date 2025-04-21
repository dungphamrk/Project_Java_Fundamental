package ra.edu.business.dao.recruitmentPosition;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.recruitmentPosition.RecruitmentPosition;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Scanner;

public class RecruitmentPositionDaoImp implements RecruitmentPositionDao {

    @Override
    public int findAll(int pageNumber, int pageSize) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int count = 0;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_FindAllRecruitmentPositions(?,?)}");
            callStmt.setInt(1, pageNumber);
            callStmt.setInt(2, pageSize);
            ResultSet rs = callStmt.executeQuery();
            System.out.println("Danh sách vị trí tuyển dụng:");
            System.out.println("--------------------------------------------------");
            while (rs.next()) {
                LocalDate createdDate = rs.getTimestamp("createdDate") != null ?
                        rs.getTimestamp("createdDate").toLocalDateTime().toLocalDate() : null;
                LocalDate expiredDate = rs.getTimestamp("expiredDate") != null ?
                        rs.getTimestamp("expiredDate").toLocalDateTime().toLocalDate() : null;

                System.out.println("ID: " + rs.getInt("id") +
                        ", Tên: " + rs.getString("name") +
                        ", Mô tả: " + rs.getString("description") +
                        ", Lương tối thiểu: " + rs.getDouble("minSalary") +
                        ", Lương tối đa: " + rs.getDouble("maxSalary") +
                        ", Kinh nghiệm tối thiểu: " + rs.getInt("minExperience") +
                        ", Ngày tạo: " + createdDate +
                        ", Ngày hết hạn: " + expiredDate);
                count++;
            }
            if (count == 0) {
                System.out.println("Không có vị trí tuyển dụng nào.");
            }
            return count;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách vị trí tuyển dụng: " + e.getMessage());
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int save(RecruitmentPosition position) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{call sp_CreateRecruitmentPosition(?,?,?,?,?,?,?)}");
            callStmt.setString(1, position.getName());
            callStmt.setString(2, position.getDescription());
            callStmt.setDouble(3, position.getMinSalary());
            callStmt.setDouble(4, position.getMaxSalary());
            callStmt.setInt(5, position.getMinExperience());
            if (position.getExpiredDate() != null) {
                callStmt.setTimestamp(6, Timestamp.valueOf(position.getExpiredDate().atStartOfDay()));
            } else {
                callStmt.setNull(6, Types.TIMESTAMP);
            }
            callStmt.registerOutParameter(7, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(7);
            if (returnCode == 0) {
                conn.commit();
            } else {
                conn.rollback();
            }
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi tạo vị trí tuyển dụng: " + e.getMessage());
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
    public int update(Scanner scanner) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            System.out.println("Nhập ID vị trí tuyển dụng cần cập nhật:");
            int positionId = Integer.parseInt(scanner.nextLine());
            RecruitmentPosition position = new RecruitmentPosition();
            position.setId(positionId);
            position.inputData(scanner);
            callStmt = conn.prepareCall("{call sp_UpdateRecruitmentPosition(?,?,?,?,?,?,?,?)}");
            callStmt.setInt(1, position.getId());
            callStmt.setString(2, position.getName());
            callStmt.setString(3, position.getDescription());
            callStmt.setDouble(4, position.getMinSalary());
            callStmt.setDouble(5, position.getMaxSalary());
            callStmt.setInt(6, position.getMinExperience());
            if (position.getExpiredDate() != null) {
                callStmt.setTimestamp(7, Timestamp.valueOf(position.getExpiredDate().atStartOfDay()));
            } else {
                callStmt.setNull(7, Types.TIMESTAMP);
            }
            callStmt.registerOutParameter(8, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(8);
            if (returnCode == 0) {
                conn.commit();
                System.out.println("Cập nhật vị trí tuyển dụng thành công.");
            } else {
                conn.rollback();
                System.out.println("Cập nhật vị trí tuyển dụng thất bại.");
            }
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi cập nhật vị trí tuyển dụng: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            return 1;
        } catch (NumberFormatException e) {
            System.err.println("ID phải là số nguyên.");
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
    public int delete(Scanner scanner) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            System.out.println("Nhập ID vị trí tuyển dụng cần xóa:");
            int positionId = Integer.parseInt(scanner.nextLine());
            callStmt = conn.prepareCall("{call sp_DeleteRecruitmentPosition(?,?)}");
            callStmt.setInt(1, positionId);
            callStmt.registerOutParameter(2, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(2);
            if (returnCode == 0) {
                conn.commit();
                System.out.println("Xóa vị trí tuyển dụng thành công.");
            } else {
                conn.rollback();
                System.out.println("Xóa vị trí tuyển dụng thất bại.");
            }
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi xóa vị trí tuyển dụng: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            return 1;
        } catch (NumberFormatException e) {
            System.err.println("ID phải là số nguyên.");
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
}