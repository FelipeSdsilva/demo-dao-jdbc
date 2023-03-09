package model.services;

import model.db.DB;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.DbException;
import model.exceptions.DbIntegrityException;
import model.repositories.SellerDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerServiceDAO implements SellerDAO {

    private final Connection conn;

    public SellerServiceDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement pst = null;

        try {
            pst = conn.prepareStatement("INSERT INTO seller " +
                    "(name, email, birthdate, basesalary, departmentid ) " +
                    "VALUES ( ?, ?, ?, ?, ? ) ", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, seller.getName());
            pst.setString(2, seller.getEmail());
            pst.setTimestamp(3, Timestamp.valueOf(seller.getBirthDate()));
            pst.setDouble(4, seller.getBaseSalary());
            pst.setInt(5, seller.getDepartment().getId());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rt = pst.getGeneratedKeys();
                if (rt.next()) {
                    int id = rt.getInt(1);
                    seller.setId(id);
                }
                DB.closeResultSet(rt);

                System.out.println("Inserted successful!");
            } else {
                throw new DbException("Unexpected error! Not rows affected!");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(pst);
        }

    }

    @Override
    public void update(Seller seller) {

    }

    @Override
    public void deleteById(int id) {
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement("DELETE FROM seller " +
                    "WHERE seller.id = ? ");
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
    public Seller findById(int id) {
        PreparedStatement pst = null;
        ResultSet rt = null;

        try {
            pst = conn.prepareStatement("SELECT seller.*, department.name as departmentName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.departmentid = departmentid " +
                    "WHERE seller.id = ?");
            pst.setInt(1, id);
            rt = pst.executeQuery();

            if (rt.next()) {
                Department department = instantiateDepartment(rt);
                return instantiateSeller(rt, department);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(pst);
            DB.closeResultSet(rt);
        }
    }

    @Override
    public List<Seller> findAll() {

        PreparedStatement pst = null;
        ResultSet rt = null;
        try {
            pst = conn.prepareStatement("SELECT DISTINCT seller.*, department.name as departmentName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.departmentid = department.id " +
                    "ORDER BY id");

            rt = pst.executeQuery();

            List<Seller> sellers = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rt.next()) {

                Department dep = map.get(rt.getInt("departmentid"));

                if (dep == null) {
                    dep = instantiateDepartment(rt);
                    map.put(rt.getInt("departmentid"), dep);
                }

                Seller seller = instantiateSeller(rt, dep);
                sellers.add(seller);
            }

            if (sellers.size() > 0) {
                return sellers.stream().toList();
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(pst);
            DB.closeResultSet(rt);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement pst = null;
        ResultSet rt = null;

        try {
            pst = conn.prepareStatement("SELECT seller.*, department.name as departmentName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.departmentid = department.id " +
                    "WHERE seller.departmentid = ? " +
                    "ORDER BY id");

            pst.setInt(1, department.getId());
            rt = pst.executeQuery();

            List<Seller> sellers = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rt.next()) {

                Department dep = map.get(rt.getInt("departmentid"));

                if (dep == null) {
                    dep = instantiateDepartment(rt);
                    map.put(rt.getInt("departmentid"), dep);
                }

                Seller seller = instantiateSeller(rt, dep);
                sellers.add(seller);
            }
            return sellers;

        } catch (SQLException e) {

        }


        return null;
    }

    private Seller instantiateSeller(ResultSet rt, Department department) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rt.getInt("id"));
        seller.setName(rt.getString("name"));
        seller.setEmail(rt.getString("email"));
        seller.setBirthDate(rt.getTimestamp("birthdate").toLocalDateTime());
        seller.setBaseSalary(rt.getDouble("basesalary"));
        seller.setDepartment(department);
        return seller;
    }

    private Department instantiateDepartment(ResultSet rt) throws SQLException {
        Department department = new Department();
        department.setId(rt.getInt("departmentid"));
        department.setName(rt.getString("departmentName"));
        return department;
    }

    private Seller updateOrInsert(PreparedStatement pst) throws SQLException {
        Seller seller = new Seller();

        return seller;
    }
}
