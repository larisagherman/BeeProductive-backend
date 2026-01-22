package com.beeproductive.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_challenge_enrollment")
public class UserChallengeEnrollment {

    @EmbeddedId
    private UserChallengeKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("challengeId")
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChallengeEnrollmentStatus status = ChallengeEnrollmentStatus.ENROLLED;

    @Column(name = "enrolled_at", nullable = false)
    private Instant enrolledAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}

