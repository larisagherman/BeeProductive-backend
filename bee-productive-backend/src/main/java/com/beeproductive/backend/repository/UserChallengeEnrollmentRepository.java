package com.beeproductive.backend.repository;

import com.beeproductive.backend.entity.UserChallengeEnrollment;
import com.beeproductive.backend.entity.UserChallengeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserChallengeEnrollmentRepository extends JpaRepository<UserChallengeEnrollment, UserChallengeKey> {
    List<UserChallengeEnrollment> findByUser_Id(Long userId);
    Optional<UserChallengeEnrollment> findByUser_IdAndChallenge_Id(Long userId, Long challengeId);
    boolean existsByUser_IdAndChallenge_Id(Long userId, Long challengeId);
}

