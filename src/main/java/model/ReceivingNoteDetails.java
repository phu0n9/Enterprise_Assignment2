package model;

import javax.persistence.*;

@Entity
@Table(name = "receiving_note_details")
public class ReceivingNoteDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "received_id")
    private ReceivingNote received;

    @Column
    private int quantity;

    public ReceivingNoteDetails(){}

    public int getId() {
        return id;
    }

    public ReceivingNote getReceived() {
        return received;
    }

    public void setReceived(ReceivingNote received) {
        this.received = received;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
