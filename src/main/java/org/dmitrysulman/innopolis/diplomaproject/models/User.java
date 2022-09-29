package org.dmitrysulman.innopolis.diplomaproject.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
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
    @Size(min = 2, max = 50, message = "{errors.user.password.size}")
    private String repeatPassword;

    @NotEmpty(message = "{errors.user.address.notempty}")
    @Size(min = 2, max = 500, message = "{errors.user.address.size}")
    @Column(name = "address")
    private String address;

    @Column(name = "is_admin")
    private Boolean isAdmin;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Order> orders;

    public User() {
    }

    public User(int id, String firstName, String secondName, String email, String password, String repeatPassword, String address, Boolean isAdmin, List<Order> orders) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.repeatPassword = repeatPassword;
        this.password = password;
        this.address = address;
        this.isAdmin = isAdmin;
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

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
