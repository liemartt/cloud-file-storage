package com.liemartt.cloud.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "users", indexes = @Index(name = "username_index", columnList = "username"))
public class User {
    @Id
    @GeneratedValue
    private Integer id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @NotNull
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role;
}
