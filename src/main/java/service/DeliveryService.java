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
public class DeliveryService {
    @Autowired
    private SessionFactory sessionFactory;

    public DeliveryService(){}

    public List<DeliveryNote> saveDeliveryNote(List<DeliveryNote> deliveryNote){
        for(DeliveryNote deliveryNote1: deliveryNote){
            sessionFactory.getCurrentSession().save(deliveryNote1);
        }
        return deliveryNote;
    }

    public DeliveryNote updateDeliveryNote(DeliveryNote deliveryNote){
        sessionFactory.getCurrentSession().update(deliveryNote);
        return deliveryNote;
    }

    public DeliveryNote getDeliveryNote(int id){
        Query query = sessionFactory.getCurrentSession().createQuery("from DeliveryNote where id=:id");
        query.setInteger("id", id);
        return (DeliveryNote) query.uniqueResult();
    }

    public int deleteDeliveryNote(int id){
        DeliveryNote deliveryNote = getDeliveryNote(id);
        this.sessionFactory.getCurrentSession().delete(deliveryNote);
        return id;
    }

    public List<DeliveryNoteDetails> saveDeliveryNoteDetails(List<DeliveryNoteDetails> deliveryNoteDetails){
        for (DeliveryNoteDetails deliveryNoteDetails1: deliveryNoteDetails){
            sessionFactory.getCurrentSession().save(deliveryNoteDetails1);
        }
        return deliveryNoteDetails;
    }

    public List<DeliveryNoteDetails> getDeliveryNoteDetailsByNote(int noteId){
        return this.sessionFactory.getCurrentSession().createQuery("from DeliveryNoteDetails where delivery.id=:id")
                .setInteger("id",noteId).list();
    }

    public List<DeliveryNote> multipleSearchByDate(Date date,int limit,int start){
        Query query = sessionFactory.getCurrentSession().createQuery("from DeliveryNote where date=:date").setFirstResult(start).setMaxResults(limit);
        query.setDate("date",date);
        return query.list();
    }

    public List<Staff> getDeliveryNoteByStaff(int staffId){
        return this.sessionFactory.getCurrentSession()
                .createQuery("from DeliveryNote where staff.id=:id")
                .setInteger("id",staffId).list();
    }

    public List<SaleInvoice> getDeliveryNoteBySaleInvoice(int saleInvoiceId){
        return this.sessionFactory.getCurrentSession()
                .createQuery("from DeliveryNote where saleInvoice.id=:id")
                .setInteger("id",saleInvoiceId).list();
    }

    public List<DeliveryNote> searchByPeriodOfDate(Date startDate, Date endDate, int limit, int start){
        Query query = sessionFactory.getCurrentSession().createQuery("from DeliveryNote where date between :fromDate and :toDate").setFirstResult(start).setMaxResults(limit);
        query.setParameter("fromDate",startDate);
        query.setParameter("toDate",endDate);
        return query.list();
    }

    public Query searchDeliveredSumQuantityByPeriod(Date startDate,Date endDate,int id){
        Query query = sessionFactory.getCurrentSession().createQuery("select sum(d.quantity)\n" +
                "from DeliveryNoteDetails d join d.delivery \n" +
                "where date between :startDate and :endDate\n" +
                "and product_id =:id\n");
        query.setInteger("id",id);
        query.setParameter("startDate",startDate);
        query.setParameter("endDate",endDate);
        return query;
    }


}
