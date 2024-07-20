package me.Dimashi.Exchange_Rate.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "persons")
public class MyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String password;
    private String roles;

    public String getRole() {
        return roles;
    }

    public void setRole(String roles) {
        this.roles = roles;
    }
}