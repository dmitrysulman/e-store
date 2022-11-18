package org.dmitrysulman.innopolis.diplomaproject.models;

import javax.persistence.*;
import javax.validation.constraints.Min;

//TODO messages
@Entity
@Table(name = "carts_items")
public class CartItem {
    @EmbeddedId
    private CartItemId id;

    @ManyToOne
    @MapsId("productId")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("cartId")
    private Cart cart;

    @Min(value = 1, message = "Amount should be greater than 0")
    @Column(name = "product_amount")
    private int productAmount;

    public CartItem() {
    }

    public CartItem(Product product, int productAmount, Cart cart) {
        this.id = new CartItemId(cart.getId(), product.getId());
        this.product = product;
        this.productAmount = productAmount;
        this.cart = cart;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public int getTotalPrice() {
        return product.getPrice() * productAmount;
    }

    public CartItemId getId() {
        return id;
    }

    public void setId(CartItemId id) {
        this.id = id;
    }
}
