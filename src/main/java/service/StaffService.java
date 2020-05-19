package service;

import model.Customer;
import model.Order;
import model.OrderDetails;
import model.Staff;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class StaffService {
    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Staff> saveStaff(List<Staff> staff){
        for(Staff staff1: staff){
            sessionFactory.getCurrentSession().save(staff1);
        }
        return staff;
    }

    public Staff updateStaff(Staff staff){
        sessionFactory.getCurrentSession().update(staff);
        return staff;
    }

    public Staff getStaff(int id){
        Query query = sessionFactory.getCurrentSession().createQuery("from Staff where id=:id");
        query.setInteger("id", id);
        return (Staff) query.uniqueResult();
    }

    public int deleteStaff(int id){
        Staff staff = getStaff(id);
        this.sessionFactory.getCurrentSession().delete(staff);
        return id;
    }

    public List<Staff> pagingStaffSearch(int i,int j){
        Query query = sessionFactory.getCurrentSession().createQuery("from Staff order by id").setMaxResults(i).setFirstResult(j);
        return query.list();
    }


    public List<Staff> multipleSearchByName(String name,int limit, int start){
        Query query = sessionFactory.getCurrentSession().createQuery("from Staff s where s.name like :name").setFirstResult(start).setMaxResults(limit);
        query.setString("name", "%"+name+"%");
        return query.list();
    }

    public List<Staff> multipleSearchByAddress(String address,int limit,int start){
        Query query = sessionFactory.getCurrentSession().createQuery("from Staff s where s.address like :address").setFirstResult(start).setMaxResults(limit);
        query.setString("address", "%"+address+"%");
        return query.list();
    }

    public List<Staff> multipleSearchByEmail(String email,int limit,int start){
        Query query = sessionFactory.getCurrentSession().createQuery("from Staff s where s.email like :email").setFirstResult(start).setMaxResults(limit);
        query.setString("email", "%"+email+"%");
        return query.list();
    }

    public List<Staff> multipleSearchByPhone(int phone,int limit,int start){
        Query query = sessionFactory.getCurrentSession().createQuery("from Staff s where s.phone like:phone").setFirstResult(start).setMaxResults(limit);
        query.setInteger("phone", phone);
        return query.list();
    }

    public List<Staff> findStaffByNameAndAddress(String name,String address){
        Query query = sessionFactory.getCurrentSession().createQuery("from Staff s where s.name like:name and s.address like:address");
        query.setString("name","%"+name+"%");
        query.setString("address","%"+address+"%");
        return query.list();
    }

}
