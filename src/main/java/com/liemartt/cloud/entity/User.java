package com.liemartt.cloud.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users", indexes = @Index(name = "username_index", columnList = "username"))
public class User {
    @Id
    @GeneratedValue
    private Integer id;
    
    @Column(unique = true)
    private String username;
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role;
}
