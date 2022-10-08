package org.dmitrysulman.innopolis.diplomaproject.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "{errors.user.firstname.notempty}")
    @Size(min = 2, max = 30, message = "{errors.user.firstname.size}")
    @Column(name = "first_name")
    private String firstName;

    @NotEmpty(message = "{errors.user.secondname.notempty}")
    @Size(min = 2, max = 30, message = "{errors.user.secondname.size}")
    @Column(name = "second_name")
    private String secondName;

    @NotEmpty(message = "{errors.user.email.notempty}")
    @Email(message = "{errors.user.email.email}")
    @Column(name = "email")
    private String email;

    @NotEmpty(message = "{errors.user.password.notempty}")
    @Size(min = 2, max = 255, message = "{errors.user.password.size}")
    @Column(name = "password")
    private String password;

    @Transient
    @NotEmpty(message = "{errors.user.password.notempty}")
    @Size(min = 2, max = 255, message = "{errors.user.password.size}")
    private String repeatPassword;

    @NotEmpty(message = "{errors.user.address.notempty}")
    @Size(min = 2, max = 500, message = "{errors.user.address.size}")
    @Column(name = "address")
    private String address;

    @Column(name = "is_admin")
    private Boolean isAdmin;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "user",
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @OrderBy("orderDate")
    private List<Order> orders;

    public User() {
    }

    public User(int id, String firstName, String secondName, String email, String password, String repeatPassword, String address, Boolean isAdmin, Instant createdAt, Instant updatedAt, List<Order> orders) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.repeatPassword = repeatPassword;
        this.password = password;
        this.address = address;
        this.isAdmin = isAdmin;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.orders = orders;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public String getFullName() {
        return firstName + ' ' + secondName;
    }
}
