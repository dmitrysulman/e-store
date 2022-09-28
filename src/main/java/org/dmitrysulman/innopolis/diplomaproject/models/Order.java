package org.dmitrysulman.innopolis.diplomaproject.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

//TODO add status ENUM
//TODO add messages
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "Order date should not be empty")
    @Temporal(TemporalType.DATE)
    @Column(name = "order_date")
    private Date orderDate;

    @Min(value = 1, message = "Products amount should be greater than 0")
    @Column(name = "products_amount")
    private int productsAmount;

    @Min(value = 1, message = "Order amount should be greater than 0")
    @Column(name = "order_amount")
    private int orderAmount;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private Product user;

    public Order() {
    }

    public Order(int id, Date orderDate, int productsAmount, int orderAmount, Product product, Product user) {
        this.id = id;
        this.orderDate = orderDate;
        this.productsAmount = productsAmount;
        this.orderAmount = orderAmount;
        this.product = product;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getProductsAmount() {
        return productsAmount;
    }

    public void setProductsAmount(int productsAmount) {
        this.productsAmount = productsAmount;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Product getUser() {
        return user;
    }

    public void setUser(Product user) {
        this.user = user;
    }
}
