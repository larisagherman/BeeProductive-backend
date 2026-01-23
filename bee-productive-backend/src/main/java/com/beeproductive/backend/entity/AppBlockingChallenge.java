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
@Table(name = "app_blocking_challenge")
@DiscriminatorValue("APP_BLOCKING")
public class AppBlockingChallenge extends Challenge {

    @ElementCollection
    @CollectionTable(name = "blocked_apps", joinColumns = @JoinColumn(name = "challenge_id"))
    @Column(name = "app_name")
    private Set<String> blockedApps = new HashSet<>(); // List of app names/package names to avoid
}

