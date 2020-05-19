package service;

import model.*;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Transactional
@Service
public class OrderService {
    @Autowired
    private SessionFactory sessionFactory;

    public List<Order> saveOrder(List<Order> orders){
        for(Order order: orders){
            sessionFactory.getCurrentSession().save(order);
        }
        return orders;
    }

    public Order updateOrder(Order order){
        sessionFactory.getCurrentSession().update(order);
        return order;
    }

    public Order getOrder(int id){
        Query query = sessionFactory.getCurrentSession().createQuery("from Order where id=:id");
        query.setInteger("id", id);
        return (Order) query.uniqueResult();
    }

    public int deleteOrder(int id){
        Order order = getOrder(id);
        this.sessionFactory.getCurrentSession().delete(order);
        return id;
    }

    public List<OrderDetails> saveOrderDetails(List<OrderDetails> orderDetails){
        for (OrderDetails orderDetails1: orderDetails){
            sessionFactory.getCurrentSession().save(orderDetails1);
        }
        return orderDetails;
    }

    public List<OrderDetails> getOrderDetailsByOrder(int orderId){
        return this.sessionFactory.getCurrentSession()
                .createQuery("from OrderDetails where order.id=:id")
                .setInteger("id",orderId).list();
    }


    public List<Order> multipleSearchByDate(Date date, int limit, int start){
        Query query = sessionFactory.getCurrentSession().createQuery("from Order where date=:date").setFirstResult(start).setMaxResults(limit);
        query.setDate("date",date);
        return query.list();
    }

    public List<Staff> getOrdersByStaff(int staffId){
        return this.sessionFactory.getCurrentSession()
                .createQuery("from Order where staff.id=:id")
                .setInteger("id",staffId).list();
    }

    public List<Provider> getOrdersByProvider(int providerId){
        return this.sessionFactory.getCurrentSession()
                .createQuery("from Order where provider.id=:id")
                .setInteger("id",providerId).list();
    }



}
