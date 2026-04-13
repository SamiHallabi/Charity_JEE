package com.ag.charity.entities.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.management.relation.Role;
import java.time.LocalDateTime;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;


    @Column(nullable = false)
    private String FirstName;

    @Column(nullable = false)
    private String LastName;

    private String oauthProvider;

    private String oauthId;

    private Role role;

    private LocalDateTime created;

    @OneToOne(mappedBy = "ADMIN", cascade = CascadeType.ALL)
    private Organization organization;

    @OneToMany(mappedBy = "USER", cascade = CascadeType.ALL)
    private List<Donation> donations;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Participation> participations;


}
