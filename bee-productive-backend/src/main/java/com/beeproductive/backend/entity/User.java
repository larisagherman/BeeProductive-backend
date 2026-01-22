package com.beeproductive.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String fireBaseId;

    @Column(nullable = false)
    private String name;

    @Column
    private int numberOfBees;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserChallengeEnrollment> challengeEnrollments = new HashSet<>();

    @ManyToMany(mappedBy = "users")
    private Set<Group> groups = new HashSet<>();
}
