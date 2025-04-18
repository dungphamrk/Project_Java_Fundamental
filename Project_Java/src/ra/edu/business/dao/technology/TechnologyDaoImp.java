package ra.edu.business.dao.technology;
import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.technology.Technology;

import java.sql.*;
import java.util.Scanner;

public class TechnologyDaoImp implements TechnologyDao {

    @Override
    public int findAll() {
        Connection conn = null;
        CallableStatement callStmt = null;
        int count = 0;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_FindAllTechnologies()}");
            ResultSet rs = callStmt.executeQuery();
            System.out.println("Danh sách công nghệ:");
            System.out.println("--------------------------------------------------");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Tên: " + rs.getString("name") +
                        ", Trạng thái: " + rs.getString("status"));
                count++;
            }
            if (count == 0) {
                System.out.println("Không có công nghệ nào.");
            }
            return count;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách công nghệ: " + e.getMessage());
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int save(Scanner scanner) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            Technology technology = new Technology();
            technology.inputData(scanner);
            callStmt = conn.prepareCall("{call sp_CreateTechnology(?,?,?)}");
            callStmt.setString(1, technology.getName());
            callStmt.setString(2, technology.getStatus().name().toUpperCase());
            callStmt.registerOutParameter(3, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(3);
            conn.commit();
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi thêm công nghệ: " + e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
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
            Technology technology = new Technology();
            System.out.println("Nhập ID công nghệ cần sửa:");
            technology.setId(Integer.parseInt(scanner.nextLine()));
            technology.inputData(scanner);
            callStmt = conn.prepareCall("{call sp_UpdateTechnology(?,?,?,?)}");
            callStmt.setInt(1, technology.getId());
            callStmt.setString(2, technology.getName());
            callStmt.setString(3, technology.getStatus().name().toUpperCase());
            callStmt.registerOutParameter(4, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(4);
            conn.commit();
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi cập nhật công nghệ: " + e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
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
            callStmt = conn.prepareCall("{call sp_DeleteTechnology(?,?)}");
            System.out.println("Nhập ID công nghệ cần xóa:");
            int technologyId = Integer.parseInt(scanner.nextLine());
            callStmt.setInt(1, technologyId);
            callStmt.registerOutParameter(2, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(2);
            conn.commit();
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi xóa công nghệ: " + e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }
}