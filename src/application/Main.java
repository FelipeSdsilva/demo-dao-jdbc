package application;

import model.entities.Department;
import model.entities.Seller;
import model.repositories.DaoFactory;
import model.repositories.DepartmentDAO;
import model.repositories.SellerDAO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        SellerDAO sellerDAO = DaoFactory.createSellerDao();
        DepartmentDAO departmentDAO = DaoFactory.createDepartmentDao();

        System.out.println("============= Test insert Seller =====================");
        Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", LocalDateTime.parse("09/07/1930 00:00", formatter), 3000.00, new Department(1, null));
        DaoFactory.createSellerDao().insert(newSeller);

        System.out.println("============= Test update Seller ======================");
        newSeller = sellerDAO.findById(2);
        newSeller.setName("Test");
        DaoFactory.createSellerDao().update(newSeller);

        System.out.println("============= Test findById Seller =====================");
        Seller seller = sellerDAO.findById(1);
        System.out.println(seller);

        System.out.println("============= Test findAll Seller =====================");
        List<Seller> sellers = DaoFactory.createSellerDao().findAll();
        sellers.forEach(System.out::println);

        System.out.println("============= Test findByDepartment Seller ============");

        Department department = new Department(1, null);
        sellers = sellerDAO.findByDepartment(department);
        sellers.forEach(System.out::println);


        System.out.println("============= Test findById Department =====================");
        Department dep = DaoFactory.createDepartmentDao().findById(1);
        System.out.println(dep);

        System.out.println("============= Test findAll Department =====================");
        List<Department> departments = DaoFactory.createDepartmentDao().findAll();
        departments.forEach(System.out::println);

        System.out.println("============= Test Insert Department =====================");
        Department department1 = new Department(null, "Cars");
        departmentDAO.insert(department1);

        System.out.println("============= Test Update Department =====================");
        department1 = departmentDAO.findById(2);
        department1.setName("Test");
        departmentDAO.update(department1);

        System.out.println("============= Test Delete Department =====================");
        departmentDAO.deleteById(9);


    }
}