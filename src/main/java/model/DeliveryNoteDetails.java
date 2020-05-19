package model;

import javax.persistence.*;

@Entity
@Table(name = "delivery_note_details")
public class DeliveryNoteDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "delivery_id")
    private DeliveryNote delivery;

    @Column
    private int quantity;

    public DeliveryNoteDetails (){}

    public int getId() {
        return id;
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

    public DeliveryNote getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryNote delivery) {
        this.delivery = delivery;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
