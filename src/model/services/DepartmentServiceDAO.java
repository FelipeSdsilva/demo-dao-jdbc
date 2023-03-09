package model.services;

import model.db.DB;
import model.entities.Department;
import model.exceptions.DbException;
import model.exceptions.DbIntegrityException;
import model.repositories.DepartmentDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentServiceDAO implements DepartmentDAO {

    private final Connection conn;

    public DepartmentServiceDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department department) {
        PreparedStatement pst = null;

        try {
            pst = conn.prepareStatement("INSERT INTO department (name) " +
                    "VALUES ( ? ) ", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, department.getName());


            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) System.out.println("Inserted successful!");

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(pst);
        }
    }

    @Override
    public void update(Department department) {
        PreparedStatement pst = null;
        ResultSet rt = null;
        try {
            pst = conn.prepareStatement("INSERT INTO department (name) " +
                    "VALUES (?) ");

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(pst);
            DB.closeResultSet(rt);
        }
    }

    @Override
    public void deleteById(int id) {
        PreparedStatement pst = null;

        try {
            pst = conn.prepareStatement("DELETE FROM department " +
                    "WHERE department.id = ? ");
            pst.setInt(1, id);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) System.out.println("Deleted successful!");

        } catch (SQLException e) {
            throw new DbIntegrityException(e.getMessage());
        } finally {
            DB.closeStatement(pst);
        }
    }

    @Override
    public Department findById(int id) {
        PreparedStatement pst = null;
        ResultSet rt = null;

        try {
            pst = conn.prepareStatement("SELECT * FROM department " +
                    "WHERE id = ? ");

            pst.setInt(1, id);
            rt = pst.executeQuery();

            if (rt.next()) return instantiateDepartment(rt);
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(pst);
            DB.closeResultSet(rt);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement pst = null;
        ResultSet rt = null;

        try {
            pst = conn.prepareStatement("SELECT * FROM department ");
            rt = pst.executeQuery();

            List<Department> departments = new ArrayList<>();

            while (rt.next()) {
                departments.add(instantiateDepartment(rt));
            }
            if (departments != null) return departments;

            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(pst);
            DB.closeResultSet(rt);
        }
    }

    private Department instantiateDepartment(ResultSet rt) throws SQLException {
        Department department = new Department();
        department.setId(rt.getInt("id"));
        department.setName(rt.getString("name"));
        return department;
    }
}
