package com.order.product.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String email;

    private String phoneNumber;

    private String password;

    private String firstName;

    private String lastName;

    private String username;

    private Date birthday;

    private Date dateCreated;

    private int gender;

    private Date lastLogin;

    private String avatar;

    private boolean active;

    private String address;

    private String codeActive;

    private boolean isBlock;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_id")
    private Set<Role> roles;
}
