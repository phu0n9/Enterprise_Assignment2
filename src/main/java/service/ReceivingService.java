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
public class ReceivingService {
    @Autowired
    private SessionFactory sessionFactory;

    public List<ReceivingNote> saveReceivingNote(List<ReceivingNote> receivingNote){
        for(ReceivingNote receivingNote1: receivingNote){
            sessionFactory.getCurrentSession().save(receivingNote1);
        }
        return receivingNote;
    }

    public ReceivingNote updateReceivingNote(ReceivingNote receivingNote){
        sessionFactory.getCurrentSession().update(receivingNote);
        return receivingNote;
    }

    public ReceivingNote getReceivingNote(int id){
        Query query = sessionFactory.getCurrentSession().createQuery("from ReceivingNote where id=:id");
        query.setInteger("id",id);
        return (ReceivingNote) query.uniqueResult();
    }

    public int deleteReceivingNote(int id){
        ReceivingNote receivingNote = getReceivingNote(id);
        this.sessionFactory.getCurrentSession().delete(receivingNote);
        return id;
    }

    public List<ReceivingNoteDetails> saveReceivingNoteDetails(List<ReceivingNoteDetails> receivingNoteDetails){
        for(ReceivingNoteDetails receivingNoteDetails1: receivingNoteDetails){
            sessionFactory.getCurrentSession().save(receivingNoteDetails1);
        }
        return receivingNoteDetails;
    }

    public List<ReceivingNoteDetails> getReceivingNoteDetailsByNote(int noteId){
        return this.sessionFactory.getCurrentSession().createQuery("from ReceivingNoteDetails where received.id=:id")
                .setInteger("id",noteId).list();
    }

    public List<ReceivingNote> multipleSearchByDate(Date date,int limit,int start){
        Query query = sessionFactory.getCurrentSession().createQuery("from ReceivingNote where receivingDate=:date").setFirstResult(start).setMaxResults(limit);
        query.setDate("date",date);
        return query.list();
    }


    public List<Staff> getReceivingNoteByStaff(int staffId){
        return this.sessionFactory.getCurrentSession()
                .createQuery("from ReceivingNote where staff.id=:id")
                .setInteger("id",staffId).list();
    }

    public List<Order> getReceivingNoteByOrder(int orderId){
        return this.sessionFactory.getCurrentSession()
                .createQuery("from ReceivingNote where order.id=:id")
                .setInteger("id",orderId).list();
    }

    public List<ReceivingNote> searchByPeriodOfDate(Date startDate, Date endDate, int limit, int start){
        Query query = sessionFactory.getCurrentSession().createQuery("from ReceivingNote where receivingDate between :fromDate and :toDate").setFirstResult(start).setMaxResults(limit);
        query.setParameter("fromDate",startDate);
        query.setParameter("toDate",endDate);
        return query.list();
    }

    public Query searchReceivedSumQuantityByPeriod(Date startDate,Date endDate,int id){
        Query query = sessionFactory.getCurrentSession().createQuery("select sum(r.quantity)\n" +
                "from ReceivingNoteDetails r join r.received \n" +
                "where receivingDate between :startDate and :endDate\n" +
                "and product_id =:id\n");
        query.setInteger("id",id);
        query.setParameter("startDate",startDate);
        query.setParameter("endDate",endDate);
        return query;
    }

}
