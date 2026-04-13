package com.ag.charity.entities.jpa;

import com.ag.charity.entities.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;   // ← minuscule

    @Column(nullable = false)
    private String lastName;    // ← minuscule

    private String oauthProvider;
    private String oauthId;

    @Enumerated(EnumType.STRING)  // ← obligatoire pour stocker "USER" et non "0"
    @Column(nullable = false)
    private Role role;

    @Column(updatable = false)
    private LocalDateTime createdAt;  // ← nom cohérent

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();  // ← auto-rempli à la création
    }

//    // mappedBy = nom EXACT du champ dans Organisation qui pointe vers User
//    @OneToOne(mappedBy = "admin", cascade = CascadeType.ALL)
//    private Organisation organisation;
//
//    // mappedBy = nom EXACT du champ dans Donation qui pointe vers User
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Donation> donations;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Participation> participations;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Notification> notifications;
}