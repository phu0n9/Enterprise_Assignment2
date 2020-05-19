package service;

import model.Customer;
import model.Customer;
import model.Staff;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class CustomerService {
    @Autowired
    private SessionFactory sessionFactory;

    public List<Customer> saveCustomer(List<Customer> customer){
        for(Customer customer1: customer){
            sessionFactory.getCurrentSession().save(customer1);
        }
        return customer;
    }

    public Customer updateCustomer(Customer customer){
        sessionFactory.getCurrentSession().update(customer);
        return customer;
    }

    public Customer getCustomer(int id){
        Query query = sessionFactory.getCurrentSession().createQuery("from Customer where id=:id");
        query.setInteger("id", id);
        return (Customer) query.uniqueResult();
    }

    public int deleteCustomer(int id){
        Customer customer = getCustomer(id);
        this.sessionFactory.getCurrentSession().delete(customer);
        return id;
    }

    public List<Customer> pagingCustomerSearch(int i, int j){
        Query query = sessionFactory.getCurrentSession().createQuery("from Customer order by id").setMaxResults(i).setFirstResult(j);
        return query.list();
    }

    public List<Customer> multipleSearchByName(String name, int limit,int start){
        Query query = sessionFactory.getCurrentSession().createQuery("from Customer c where c.name like:name").setFirstResult(start).setMaxResults(limit);
        query.setString("name", "%"+name+"%");
        return query.list();
    }

    public List<Customer> multipleSearchByAddress(String address, int limit,int start){
        Query query = sessionFactory.getCurrentSession().createQuery("from Customer c where c.address like:address").setFirstResult(start).setMaxResults(limit);
        query.setString("address", "%"+address+"%");
        return query.list();
    }

    public List<Customer> multipleSearchByPhone(int phone, int limit,int start){
        Query query = sessionFactory.getCurrentSession().createQuery("from Customer c where c.phone=:phone").setFirstResult(start).setMaxResults(limit);
        query.setInteger("phone", phone);
        return query.list();
    }

}
