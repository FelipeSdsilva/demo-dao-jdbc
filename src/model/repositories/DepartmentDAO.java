package model.repositories;

import model.entities.Department;

import java.util.List;

public interface DepartmentDAO {
    public void insert(Department department);

    public void update(Department department);

    public void deleteById(int id);

    public Department findById(int id);

    public List<Department> findAll();
}
