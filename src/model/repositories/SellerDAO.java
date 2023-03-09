package model.repositories;

import model.entities.Department;
import model.entities.Seller;

import java.util.List;

public interface SellerDAO {
    public void insert(Seller seller);

    public void update(Seller seller);

    public void deleteById(int id);

    public Seller findById(int id);

    public List<Seller> findAll();

    public List<Seller> findByDepartment(Department department);
}
