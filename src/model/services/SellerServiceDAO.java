package model.services;

import model.db.DB;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.DbException;
import model.repositories.SellerDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SellerServiceDAO implements SellerDAO {

    private final Connection conn;

    public SellerServiceDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {

    }

    @Override
    public void update(Seller seller) {

    }

    @Override
    public void deleteById(int id) {


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
                    "FROM seller, department " +
                    "WHERE seller.departmentid = department.id");
            rt = pst.executeQuery();
            List<Seller> sellers = new ArrayList<>();

            while (rt.next()) {
                Department department = instantiateDepartment(rt);
                Seller seller = instantiateSeller(rt, department);
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
}
