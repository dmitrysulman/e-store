package org.dmitrysulman.innopolis.diplomaproject.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

//TODO add messages
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "Product name should not be empty")
    @Size(min = 2, max = 255, message = "Product name should be between 2 and 255 characters")
    @Column(name = "name")
    private String name;

    @Min(value = 1, message = "Price should be greater than 0")
    @Column(name = "price")
    private int price;

    @Min(value = 0, message = "Amount should be positive or 0")
    @Column(name = "amount")
    private int amount;

    public Product() {
    }

    public Product(int id, String name, int price, int amount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
