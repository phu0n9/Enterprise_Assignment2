package model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "sale_invoice")
public class SaleInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private Date invoiceDate;

    @ManyToOne
    private Staff staff;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private DeliveryNote deliveryNote;

    @OneToMany(mappedBy = "saleInvoice",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<SaleInvoiceDetails> saleInvoiceDetails;

    public List<SaleInvoiceDetails> getSaleInvoiceDetails() {
        return saleInvoiceDetails;
    }

    public void setSaleInvoiceDetails(List<SaleInvoiceDetails> saleInvoiceDetails) {
        this.saleInvoiceDetails = saleInvoiceDetails;
    }

    public DeliveryNote getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(DeliveryNote deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    private int totalValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
    }
}
