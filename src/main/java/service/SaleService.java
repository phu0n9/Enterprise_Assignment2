package service;

import model.*;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Transactional
@Service
public class SaleService {
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private DeliveryService deliveryService;

    public List<SaleInvoice> saveSaleInvoice(List<SaleInvoice> saleInvoice){
        for (SaleInvoice saleInvoice1: saleInvoice){
            List<DeliveryNoteDetails> deliveryNoteDetails = deliveryService.getDeliveryNoteDetailsByNote(saleInvoice1.getDeliveryNote().getId());
            List<SaleInvoiceDetails> invoiceDetails = new ArrayList<>();
            int total = 0;
            for(DeliveryNoteDetails details: deliveryNoteDetails){
                SaleInvoiceDetails saleInvoiceDetails = new SaleInvoiceDetails();
                saleInvoiceDetails.setProduct(details.getProduct());
                saleInvoiceDetails.setQuantity(details.getQuantity());
                saleInvoiceDetails.setPrice(details.getProduct().getPrice());
                saleInvoiceDetails.setSaleInvoice(saleInvoice1);
                invoiceDetails.add(saleInvoiceDetails);
                total += details.getProduct().getPrice() * details.getQuantity();
            }
            saleInvoice1.setTotalValue(total);
            saleInvoice1.setSaleInvoiceDetails(invoiceDetails);
            sessionFactory.getCurrentSession().save(saleInvoice1);
        }
        return saleInvoice;
    }

    public SaleInvoice updateSaleInvoice(SaleInvoice saleInvoice){
        sessionFactory.getCurrentSession().update(saleInvoice);
        return saleInvoice;
    }

    public SaleInvoice getSaleInvoice(int id){
        Query query = sessionFactory.getCurrentSession().createQuery("from SaleInvoice where id=:id");
        query.setInteger("id", id);
        return (SaleInvoice) query.uniqueResult();
    }

    public int deleteSaleInvoice(int id){
        SaleInvoice saleInvoice = getSaleInvoice(id);
        this.sessionFactory.getCurrentSession().delete(saleInvoice);
        return id;
    }

    public SaleInvoiceDetails saveSaleInvoiceDetails(SaleInvoiceDetails saleInvoiceDetails){
        sessionFactory.getCurrentSession().save(saleInvoiceDetails);
        return saleInvoiceDetails;
    }

    public List<SaleInvoice> multipleSearchByDate(Date date, int limit, int start){
        Query query = sessionFactory.getCurrentSession().createQuery("from SaleInvoice where invoiceDate=:date").setFirstResult(start).setMaxResults(limit);
        query.setDate("date",date);
        return query.list();
    }

    public List<SaleInvoiceDetails> getSaleDetailsBySaleInvoice(int saleInvoiceId){
        return this.sessionFactory.getCurrentSession()
                .createQuery("from SaleInvoiceDetails where saleInvoice.id=:id")
                .setInteger("id",saleInvoiceId).list();
    }

    public List<Staff> getSaleInvoicesByStaff(int staffId){
        return this.sessionFactory.getCurrentSession()
                .createQuery("from SaleInvoice where staff.id=:id")
                .setInteger("id",staffId).list();
    }


    public List<Customer> getSaleInvoicesByCustomer(int customerId){
        return this.sessionFactory.getCurrentSession()
                .createQuery("from SaleInvoice where customer.id=:id")
                .setInteger("id",customerId).list();
    }

    public List<SaleInvoice> searchByPeriodOfDate(Date startDate, Date endDate,int limit,int start){
        Query query = sessionFactory.getCurrentSession().createQuery("from SaleInvoice where invoiceDate between :fromDate and :toDate").setFirstResult(start).setMaxResults(limit);
        query.setParameter("fromDate",startDate);
        query.setParameter("toDate",endDate);
        return query.list();
    }

    public List<SaleInvoice> searchStaffByPeriodOfDate(int staffId,Date startDate, Date endDate,int limit,int start){
        Query query = sessionFactory.getCurrentSession().createQuery("from SaleInvoice where invoiceDate between :fromDate and :toDate and staff.id=:id").setFirstResult(start).setMaxResults(limit);
        query.setParameter("fromDate",startDate);
        query.setParameter("toDate",endDate);
        query.setInteger("id",staffId);
        return query.list();
    }

    public List<SaleInvoice> searchCustomerByPeriodOfDate(int customerId,Date startDate, Date endDate,int limit,int start){
        Query query = sessionFactory.getCurrentSession().createQuery("from SaleInvoice where invoiceDate between :fromDate and :toDate and customer.id=:id").setFirstResult(start).setMaxResults(limit);
        query.setParameter("fromDate",startDate);
        query.setParameter("toDate",endDate);
        query.setInteger("id",customerId);
        return query.list();
    }

    public List<Map<String,String>> revenueByDateByStaffAndCustomer(int customerId,int staffId,Date startDate,Date endDate){
        List<Query> queryRevenue = new ArrayList<>();
        queryRevenue.add(sessionFactory.getCurrentSession().createQuery("SELECT sum(s.totalValue) from SaleInvoice s where invoiceDate between:fromDate and :toDate and customer.id=:cid and staff.id=:sid"));
        queryRevenue.add(sessionFactory.getCurrentSession().createQuery("SELECT sum(s.totalValue) from SaleInvoice s where invoiceDate between:fromDate and :toDate and staff.id=:sid"));
        queryRevenue.add(sessionFactory.getCurrentSession().createQuery("SELECT sum(s.totalValue) from SaleInvoice s where invoiceDate between:fromDate and :toDate and customer.id=:cid"));
        List<String> stringList = new ArrayList<>();
        for (int i = 0;i<queryRevenue.size();i++) {
            queryRevenue.get(i).setParameter("fromDate", startDate);
            queryRevenue.get(i).setParameter("toDate", endDate);
            queryRevenue.get(0).setInteger("cid", customerId);
            queryRevenue.get(0).setInteger("sid", staffId);
            if(queryRevenue.get(i).getQueryString().contains("cid")){
                queryRevenue.get(i).setInteger("cid", customerId);
            }
            else if(queryRevenue.get(i).getQueryString().contains("sid")){
                queryRevenue.get(i).setInteger("sid", staffId);
            }
            stringList.add(queryRevenue.get(i).list().toString());
        }
        List<Map<String,String>> mapList = new ArrayList<>();
        Map<String,String> stringMap = new HashMap<>();
        stringMap.put("The total revenue from date "+startDate+" to date "+endDate+" by staff with id "+staffId
                +" and customer with id "+customerId+" is ",stringList.get(0));
        stringMap.put("The total revenue from date "+startDate+" to date "+endDate+" by staff with id "+staffId
                +" is ",stringList.get(1));
        stringMap.put("The total revenue from date "+startDate+" to date "+endDate+" by customer with id "+customerId
                +" is ",stringList.get(2));
        mapList.add(stringMap);
       return mapList;
    }

}
