package model.repositories;

import model.db.DB;
import model.services.DepartmentServiceDAO;
import model.services.SellerServiceDAO;

public class DaoFactory {
    public static SellerDAO createSellerDao() {
        return new SellerServiceDAO(DB.getConnection());
    }

    public static DepartmentDAO createDepartmentDao(){
        return new DepartmentServiceDAO(DB.getConnection());
    }
}
