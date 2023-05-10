package com.example.restapi.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 30, message = "Имя должно содержать от 2 до 30 символов")
    private String firstName;

    @Column(name = "last_name")
    @Size(min = 2, max = 30, message = "Имя должно содержать от 2 до 30 символов")
    private String lastName;

    @Column(name = "email", unique = true)
    @NotEmpty(message = "Поле не должно быть пустым")
    @Email(message = "Невалидный адрес почты")
    private String email;

    @Column(name = "age")
    @Max(value = 125, message = "Возраст должен быть мньше 126")
    @Positive
    private Byte age;

    @Column(name = "password")
    @Size(min = 4, message = "Пароль должен содержать от 4 до 20 символов")
    private String password;

    @ManyToMany
    @JoinTable(name = "users_role",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonManagedReference
    @JsonIgnore
    private List<Role> userRoles;

    public User() {
    }

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(String firstName, String lastName, String email, Byte age, String password, List<Role> userRoles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.password = password;
        this.userRoles = userRoles;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Byte getAge() {
        return age;
    }

    // Гетеры сетеры роли ************

    public List<Role> getRole() {
        return userRoles;
    }

    public void setRole(List<Role> userRoles) {
        this.userRoles = userRoles;
    }

    //*************************

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(Byte age) {
        this.age = age;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
    public String getStringRole() {
        return userRoles.stream().map(Object::toString)
                .collect(Collectors.joining(" "));
    }

    // Методы интерфейса UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRole();
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return firstName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
