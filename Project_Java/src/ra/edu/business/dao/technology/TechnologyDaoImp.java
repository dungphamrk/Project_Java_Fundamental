package ra.edu.business.dao.technology;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.technology.Status;
import ra.edu.business.model.technology.Technology;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TechnologyDaoImp implements TechnologyDao {

    @Override
    public int findAll(int pageNumber, int pageSize) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int count = 0;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_FindAllTechnologies(?,?,?)}");
            callStmt.setInt(1, pageNumber);
            callStmt.setInt(2, pageSize);
            callStmt.registerOutParameter(3, Types.INTEGER);
            ResultSet rs = callStmt.executeQuery();
            int totalPages = callStmt.getInt(3);
            System.out.println("Danh sách công nghệ (Trang " + pageNumber + "/" + totalPages + "):");
            System.out.println("--------------------------------------------------");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Tên: " + rs.getString("name") +
                        ", Trạng thái: " + rs.getString("status"));
                count++;
            }
            if (count == 0) {
                System.out.println("Không có công nghệ nào trên trang này.");
            }
            return totalPages;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách công nghệ: " + e.getMessage());
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int save(Technology technology) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{call sp_CreateTechnology(?,?,?)}");
            callStmt.setString(1, technology.getName());
            callStmt.setString(2, technology.getStatus().name().toUpperCase());
            callStmt.registerOutParameter(3, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(3);
            if (returnCode == 1) {
                conn.commit();
            } else {
                conn.rollback();
            }
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi thêm công nghệ: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            return 0;
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
            if (returnCode == 1) {
                conn.commit();
            } else {
                conn.rollback();
            }
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi cập nhật công nghệ: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            return 0;
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
            callStmt = conn.prepareCall("{call sp_DeleteTechnology(?,?)}");
            System.out.println("Nhập ID công nghệ cần xóa:");
            int technologyId = Integer.parseInt(scanner.nextLine());
            callStmt.setInt(1, technologyId);
            callStmt.registerOutParameter(2, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(2);
            if (returnCode == 1) {
                conn.commit();
            } else {
                conn.rollback();
            }
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi xóa công nghệ: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            return 0;
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
    public Technology findById(int id) {
        Connection conn = null;
        CallableStatement callStmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_FindTechnologyById(?)}");
            callStmt.setInt(1, id);

            boolean hasResults = callStmt.execute();

            int found = 0;
            if (hasResults) {
                rs = callStmt.getResultSet();
                if (rs.next()) {
                    found = rs.getInt("found");
                }
                rs.close();
            }

            // Xử lý ResultSet thứ hai (thông tin công nghệ)
            if (found > 0 && callStmt.getMoreResults()) {
                rs = callStmt.getResultSet();
                if (rs.next()) {
                    Technology technology = new Technology();
                    technology.setId(rs.getInt("id"));
                    technology.setName(rs.getString("name"));
                    technology.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));
                    return technology;
                }
            }

            System.out.println("Không tìm thấy công nghệ với ID: " + id);
            return null;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi tìm kiếm công nghệ theo ID: " + e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            System.err.println("Trạng thái công nghệ không hợp lệ: " + e.getMessage());
            return null;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.err.println("Lỗi khi đóng ResultSet: " + e.getMessage());
                }
            }
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int searchByName(String keyword, int pageNumber, int pageSize) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int count = 0;
        int totalPages = 0;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_SearchTechnologies(?,?,?)}");
            callStmt.setString(1, "%" + keyword + "%");
            callStmt.setInt(2, pageNumber);
            callStmt.setInt(3, pageSize);

            boolean hasResults = callStmt.execute();

            if (hasResults) {
                ResultSet rs = callStmt.getResultSet();
                if (rs.next()) {
                    totalPages = rs.getInt("totalPages");
                }
                rs.close();
            }

            // Xử lý ResultSet thứ hai (danh sách công nghệ)
            if (callStmt.getMoreResults()) {
                ResultSet rs = callStmt.getResultSet();
                System.out.println("Kết quả tìm kiếm (Trang " + pageNumber + "/" + totalPages + "):");
                System.out.println("--------------------------------------------------");
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id") +
                            ", Tên: " + rs.getString("name") +
                            ", Trạng thái: " + rs.getString("status"));
                    count++;
                }
                rs.close();
                if (count == 0) {
                    System.out.println("Không tìm thấy công nghệ nào trên trang này.");
                }
            }

            return totalPages;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi tìm kiếm công nghệ: " + e.getMessage());
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public List<Technology> getCandidateTechnologies(int candidateId) {
        Connection conn = null;
        CallableStatement callStmt = null;
        List<Technology> technologies = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_GetCandidateTechnologies(?)}");
            callStmt.setInt(1, candidateId);
            ResultSet rs = callStmt.executeQuery();
            while (rs.next()) {
                Technology technology = new Technology();
                technology.setId(rs.getInt("id"));
                technology.setName(rs.getString("name"));
                technology.setStatus(Status.valueOf(rs.getString("status")));
                technologies.add(technology);
            }
            return technologies;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách công nghệ của ứng viên: " + e.getMessage());
            return null;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int addCandidateTechnology(int candidateId, int technologyId) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{call sp_AddCandidateTechnology(?,?,?)}");
            callStmt.setInt(1, candidateId);
            callStmt.setInt(2, technologyId);
            callStmt.registerOutParameter(3, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(3);
            if (returnCode == 1) {
                conn.commit();
            } else {
                conn.rollback();
            }
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi thêm công nghệ cho ứng viên: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            return 0;
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
    public int removeCandidateTechnology(int candidateId, int technologyId) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{call sp_RemoveCandidateTechnology(?,?,?)}");
            callStmt.setInt(1, candidateId);
            callStmt.setInt(2, technologyId);
            callStmt.registerOutParameter(3, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(3);
            if (returnCode == 1) {
                conn.commit();
            } else {
                conn.rollback();
            }
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi xóa công nghệ của ứng viên: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            return 0;
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