package com.beeproductive.backend.service;

import com.beeproductive.backend.dto.UserChallengeEnrollmentResponseDto;
import com.beeproductive.backend.entity.*;
import com.beeproductive.backend.mapper.UserChallengeEnrollmentMapper;
import com.beeproductive.backend.repository.ChallengeRepository;
import com.beeproductive.backend.repository.UserChallengeEnrollmentRepository;
import com.beeproductive.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class UserChallengeEnrollmentService {

    private final UserChallengeEnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final UserChallengeEnrollmentMapper enrollmentMapper;

    @Transactional
    public UserChallengeEnrollmentResponseDto enroll(Long userId, Long challengeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("Challenge not found"));

        UserChallengeKey key = new UserChallengeKey(userId, challengeId);
        if (enrollmentRepository.existsById(key)) {
            throw new RuntimeException("User is already enrolled in this challenge");
        }

        UserChallengeEnrollment enrollment = new UserChallengeEnrollment();
        enrollment.setId(key);
        enrollment.setUser(user);
        enrollment.setChallenge(challenge);
        enrollment.setStatus(ChallengeEnrollmentStatus.ENROLLED);

        enrollmentRepository.save(enrollment);
        return enrollmentMapper.toDto(enrollment);
    }

    @Transactional
    public UserChallengeEnrollmentResponseDto updateStatus(Long userId, Long challengeId, ChallengeEnrollmentStatus status) {
        UserChallengeKey key = new UserChallengeKey(userId, challengeId);
        UserChallengeEnrollment enrollment = enrollmentRepository.findById(key)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setStatus(status);
        enrollmentRepository.save(enrollment);

        return enrollmentMapper.toDto(enrollment);
    }

    @Transactional(readOnly = true)
    public List<UserChallengeEnrollmentResponseDto> getEnrollmentsForUser(Long userId) {
        return enrollmentMapper.toDtoList(enrollmentRepository.findByUser_Id(userId));
    }

    @Transactional
    public void unenroll(Long userId, Long challengeId) {
        UserChallengeKey key = new UserChallengeKey(userId, challengeId);
        if (!enrollmentRepository.existsById(key)) {
            throw new RuntimeException("Enrollment not found");
        }
        enrollmentRepository.deleteById(key);
    }
}

