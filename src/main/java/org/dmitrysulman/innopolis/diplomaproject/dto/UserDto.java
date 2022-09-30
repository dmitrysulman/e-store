package org.dmitrysulman.innopolis.diplomaproject.dto;

import org.dmitrysulman.innopolis.diplomaproject.models.Order;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

public class UserDto {
    private int id;

    @NotEmpty(message = "{errors.user.firstname.notempty}")
    @Size(min = 2, max = 30, message = "{errors.user.firstname.size}")
    private String firstName;

    @NotEmpty(message = "{errors.user.secondname.notempty}")
    @Size(min = 2, max = 30, message = "{errors.user.secondname.size}")
    private String secondName;

    @NotEmpty(message = "{errors.user.email.notempty}")
    @Email(message = "{errors.user.email.email}")
    private String email;

    @NotEmpty(message = "{errors.user.address.notempty}")
    @Size(min = 2, max = 500, message = "{errors.user.address.size}")
    @Column(name = "address")
    private String address;

    public UserDto() {
    }

    public UserDto(int id, String firstName, String secondName, String email, String address) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFullName() {
        return firstName + ' ' + secondName;
    }
}
