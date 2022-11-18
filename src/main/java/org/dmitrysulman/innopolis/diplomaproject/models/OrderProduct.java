package org.dmitrysulman.innopolis.diplomaproject.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
//TODO messages
@Entity
@Table(name = "orders_products")
public class OrderProduct {
    @EmbeddedId
    private OrderProductId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    private Product product;

    @Min(value = 1, message = "Price should be greater than 0")
    @Column(name = "product_price")
    private int productPrice;

    @Min(value = 1, message = "Amount should be greater than 0")
    @Column(name = "product_amount")
    private int productAmount;

    public OrderProduct() {
    }

    public OrderProduct(Order order, Product product, int productPrice, int productAmount) {
        this.id = new OrderProductId(order.getId(), product.getId());
        this.product = product;
        this.order = order;
        this.productPrice = productPrice;
        this.productAmount = productAmount;
    }

    public OrderProductId getId() {
        return id;
    }

    public void setId(OrderProductId id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    public int getTotalPrice() {
        return product.getPrice() * productAmount;
    }
}
