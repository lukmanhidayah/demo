package com.example.demo.models;

import javax.persistence.*;

@Entity
@Table(
        name = "orderitems"
)
public class Orderitems {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "order_id")
    private long order_id;

    @Column(name = "product_id")
    private long product_id;

    @Column(name = "discount", columnDefinition="Decimal(4,2) default '0.0'")
    private float discount;

    @Column(name = "final_price")
    private Double final_price;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public Double getFinal_price() {
        return final_price;
    }

    public void setFinal_price(Double final_price) {
        this.final_price = final_price;
    }
}
