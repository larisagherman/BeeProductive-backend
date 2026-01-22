package com.beeproductive.backend.repository;

import com.beeproductive.backend.entity.UserChallengeEnrollment;
import com.beeproductive.backend.entity.UserChallengeKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserChallengeEnrollmentRepository extends JpaRepository<UserChallengeEnrollment, UserChallengeKey> {
    List<UserChallengeEnrollment> findByUser_Id(Long userId);
}

