package com.beeproductive.backend.service;

import com.beeproductive.backend.dto.ChallengeDto;
import com.beeproductive.backend.dto.JoinChallengeRequestDto;
import com.beeproductive.backend.dto.JoinChallengeResponseDto;
import com.beeproductive.backend.dto.UpdateChallengeStatusRequestDto;
import com.beeproductive.backend.dto.UpdateChallengeStatusResponseDto;
import com.beeproductive.backend.dto.UserChallengeEnrollmentDto;
import com.beeproductive.backend.entity.*;
import com.beeproductive.backend.exception.AlreadyEnrolledException;
import com.beeproductive.backend.exception.ChallengeNotFoundException;
import com.beeproductive.backend.repository.ChallengeRepository;
import com.beeproductive.backend.repository.UserChallengeEnrollmentRepository;
import com.beeproductive.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;
    private final UserChallengeEnrollmentRepository enrollmentRepository;

    @Transactional
    public JoinChallengeResponseDto joinChallenge(JoinChallengeRequestDto requestDto) {
        // Validate challenge exists
        Challenge challenge = challengeRepository.findById(requestDto.getChallengeId())
                .orElseThrow(() -> new ChallengeNotFoundException(
                        "Challenge with ID " + requestDto.getChallengeId() + " does not exist"
                ));

        // Find or create user
        User user = userRepository.findByFireBaseId(requestDto.getUserUid())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setFireBaseId(requestDto.getUserUid());
                    newUser.setName("User");
                    newUser.setNumberOfBees(0);
                    return userRepository.save(newUser);
                });

        // Check if user is already enrolled
        if (enrollmentRepository.existsByUser_IdAndChallenge_Id(user.getId(), challenge.getId())) {
            throw new AlreadyEnrolledException("You are already enrolled in this challenge");
        }

        // Create enrollment
        UserChallengeKey key = new UserChallengeKey();
        key.setUserId(user.getId());
        key.setChallengeId(challenge.getId());

        UserChallengeEnrollment enrollment = new UserChallengeEnrollment();
        enrollment.setId(key);
        enrollment.setUser(user);
        enrollment.setChallenge(challenge);
        enrollment.setStatus(ChallengeEnrollmentStatus.ENROLLED);

        enrollmentRepository.save(enrollment);

        return new JoinChallengeResponseDto(
                "Successfully joined the challenge",
                challenge.getId(),
                challenge.getName(),
                ChallengeEnrollmentStatus.ENROLLED
        );
    }

    @Transactional(readOnly = true)
    public List<ChallengeDto> getAllChallenges() {
        return challengeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ChallengeDto getChallengeById(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ChallengeNotFoundException(
                        "Challenge with ID " + challengeId + " does not exist"
                ));
        return convertToDto(challenge);
    }

    private ChallengeDto convertToDto(Challenge challenge) {
        ChallengeDto dto = new ChallengeDto();
        dto.setId(challenge.getId());
        dto.setName(challenge.getName());
        dto.setDescription(challenge.getDescription());
        dto.setType(challenge.getType());
        dto.setStartDate(challenge.getStartDate());
        dto.setEndDate(challenge.getEndDate());
        dto.setRewardBees(challenge.getRewardBees());

        // Set type-specific fields
        switch (challenge) {
            case ScreenTimeReductionChallenge specific -> dto.setReductionPercentage(specific.getReductionPercentage());
            case AppBlockingChallenge specific -> dto.setBlockedApps(specific.getBlockedApps());
            case DailyLimitChallenge specific -> dto.setMaxDailyMinutes(specific.getMaxDailyMinutes());
            default -> {
            }
        }

        return dto;
    }

    @Transactional(readOnly = true)
    public List<UserChallengeEnrollmentDto> getMyEnrolledChallenges(String userUid) {
        // Find user
        User user = userRepository.findByFireBaseId(userUid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get all enrollments for this user
        List<UserChallengeEnrollment> enrollments = enrollmentRepository.findByUser_Id(user.getId());

        // Convert to DTOs
        return enrollments.stream()
                .map(this::convertEnrollmentToDto)
                .collect(Collectors.toList());
    }

    private UserChallengeEnrollmentDto convertEnrollmentToDto(UserChallengeEnrollment enrollment) {
        Challenge challenge = enrollment.getChallenge();

        UserChallengeEnrollmentDto dto = new UserChallengeEnrollmentDto();
        dto.setChallengeId(challenge.getId());
        dto.setChallengeName(challenge.getName());
        dto.setChallengeDescription(challenge.getDescription());
        dto.setChallengeType(challenge.getType());
        dto.setStartDate(challenge.getStartDate());
        dto.setEndDate(challenge.getEndDate());
        dto.setRewardBees(challenge.getRewardBees());
        dto.setStatus(enrollment.getStatus());
        dto.setEnrolledAt(enrollment.getEnrolledAt());
        dto.setUpdatedAt(enrollment.getUpdatedAt());

        // Set type-specific fields
        switch (challenge) {
            case ScreenTimeReductionChallenge specific -> dto.setReductionPercentage(specific.getReductionPercentage());
            case AppBlockingChallenge specific -> dto.setBlockedApps(specific.getBlockedApps());
            case DailyLimitChallenge specific -> dto.setMaxDailyMinutes(specific.getMaxDailyMinutes());
            default -> {
            }
        }

        return dto;
    }

    @Transactional
    public UpdateChallengeStatusResponseDto updateChallengeStatus(UpdateChallengeStatusRequestDto requestDto) {
        // Validate status is either COMPLETED or FAILED
        if (requestDto.getStatus() != ChallengeEnrollmentStatus.COMPLETED &&
            requestDto.getStatus() != ChallengeEnrollmentStatus.FAILED) {
            throw new IllegalArgumentException("Status must be either COMPLETED or FAILED");
        }

        // Find user
        User user = userRepository.findByFireBaseId(requestDto.getUserUid())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find challenge
        Challenge challenge = challengeRepository.findById(requestDto.getChallengeId())
                .orElseThrow(() -> new ChallengeNotFoundException(
                        "Challenge with ID " + requestDto.getChallengeId() + " does not exist"
                ));

        // Find enrollment
        UserChallengeEnrollment enrollment = enrollmentRepository
                .findByUser_IdAndChallenge_Id(user.getId(), challenge.getId())
                .orElseThrow(() -> new RuntimeException("You are not enrolled in this challenge"));

        // Check if already completed or failed
        if (enrollment.getStatus() != ChallengeEnrollmentStatus.ENROLLED) {
            throw new IllegalStateException("Challenge status has already been set to: " + enrollment.getStatus());
        }

        // Update enrollment status
        enrollment.setStatus(requestDto.getStatus());
        enrollmentRepository.save(enrollment);

        // Award bees if completed
        Integer beesAwarded = null;
        if (requestDto.getStatus() == ChallengeEnrollmentStatus.COMPLETED) {
            beesAwarded = challenge.getRewardBees();
            user.setNumberOfBees(user.getNumberOfBees() + beesAwarded);
            userRepository.save(user);
        }

        String message = requestDto.getStatus() == ChallengeEnrollmentStatus.COMPLETED
                ? "Congratulations! You completed the challenge and earned " + beesAwarded + " bees!"
                : "Challenge failed. Better luck next time!";

        return new UpdateChallengeStatusResponseDto(
                message,
                challenge.getId(),
                challenge.getName(),
                requestDto.getStatus(),
                beesAwarded
        );
    }

    /**
     * Creates sample challenges for testing/debugging purposes.
     * This method should only be called during development or from a secure admin endpoint.
     * @return List of created challenge IDs
     */
    @Transactional
    public List<Long> createSampleChallenges() {
        List<Long> createdIds = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusWeeks(1);

        // 1. Screen Time Reduction Challenge
        ScreenTimeReductionChallenge screenTimeChallenge = new ScreenTimeReductionChallenge();
        screenTimeChallenge.setName("Screen Time Warrior");
        screenTimeChallenge.setDescription("Reduce your screen time by 20% this week and earn bees!");
        screenTimeChallenge.setType(ChallengeType.SCREEN_TIME_REDUCTION);
        screenTimeChallenge.setStartDate(today);
        screenTimeChallenge.setEndDate(nextWeek);
        screenTimeChallenge.setRewardBees(50);
        screenTimeChallenge.setReductionPercentage(20);
        screenTimeChallenge.setBaselineScreenTime(null); // Will be calculated per user
        Challenge saved1 = challengeRepository.save(screenTimeChallenge);
        createdIds.add(saved1.getId());

        // 2. App Blocking Challenge - Social Media Detox
        AppBlockingChallenge socialMediaDetox = new AppBlockingChallenge();
        socialMediaDetox.setName("Social Media Detox");
        socialMediaDetox.setDescription("Stay away from social media apps for a week");
        socialMediaDetox.setType(ChallengeType.APP_BLOCKING);
        socialMediaDetox.setStartDate(today);
        socialMediaDetox.setEndDate(nextWeek);
        socialMediaDetox.setRewardBees(75);
        socialMediaDetox.setBlockedApps(Set.of(
                "com.instagram.android",
                "com.zhiliaoapp.musically",  // TikTok
                "com.facebook.katana",
                "com.twitter.android",
                "com.snapchat.android"
        ));
        Challenge saved2 = challengeRepository.save(socialMediaDetox);
        createdIds.add(saved2.getId());

        // 3. App Blocking Challenge - Gaming Break
        AppBlockingChallenge gamingBreak = new AppBlockingChallenge();
        gamingBreak.setName("Gaming Break");
        gamingBreak.setDescription("Take a break from mobile games this week");
        gamingBreak.setType(ChallengeType.APP_BLOCKING);
        gamingBreak.setStartDate(today);
        gamingBreak.setEndDate(nextWeek);
        gamingBreak.setRewardBees(60);
        gamingBreak.setBlockedApps(Set.of(
                "com.king.candycrushsaga",
                "com.tencent.ig",  // PUBG Mobile
                "com.activision.callofduty.shooter",
                "com.supercell.clashofclans",
                "com.innersloth.spacemafia"  // Among Us
        ));
        Challenge saved3 = challengeRepository.save(gamingBreak);
        createdIds.add(saved3.getId());

        // 4. Daily Limit Challenge - 2 Hours
        DailyLimitChallenge twoHourLimit = new DailyLimitChallenge();
        twoHourLimit.setName("2 Hour Daily Limit");
        twoHourLimit.setDescription("Keep your daily screen time under 2 hours");
        twoHourLimit.setType(ChallengeType.DAILY_LIMIT);
        twoHourLimit.setStartDate(today);
        twoHourLimit.setEndDate(nextWeek);
        twoHourLimit.setRewardBees(100);
        twoHourLimit.setMaxDailyMinutes(120);
        Challenge saved4 = challengeRepository.save(twoHourLimit);
        createdIds.add(saved4.getId());

        // 5. Daily Limit Challenge - 3 Hours
        DailyLimitChallenge threeHourLimit = new DailyLimitChallenge();
        threeHourLimit.setName("3 Hour Daily Limit");
        threeHourLimit.setDescription("Keep your daily screen time under 3 hours - A good starting point!");
        threeHourLimit.setType(ChallengeType.DAILY_LIMIT);
        threeHourLimit.setStartDate(today);
        threeHourLimit.setEndDate(nextWeek);
        threeHourLimit.setRewardBees(75);
        threeHourLimit.setMaxDailyMinutes(180);
        Challenge saved5 = challengeRepository.save(threeHourLimit);
        createdIds.add(saved5.getId());

        // 6. Screen Time Reduction Challenge - Aggressive
        ScreenTimeReductionChallenge aggressiveReduction = new ScreenTimeReductionChallenge();
        aggressiveReduction.setName("Screen Time Ninja");
        aggressiveReduction.setDescription("Cut your screen time in half this week!");
        aggressiveReduction.setType(ChallengeType.SCREEN_TIME_REDUCTION);
        aggressiveReduction.setStartDate(today);
        aggressiveReduction.setEndDate(nextWeek);
        aggressiveReduction.setRewardBees(150);
        aggressiveReduction.setReductionPercentage(50);
        aggressiveReduction.setBaselineScreenTime(null);
        Challenge saved6 = challengeRepository.save(aggressiveReduction);
        createdIds.add(saved6.getId());

        // 7. Daily Limit Challenge - 1 Hour (Hard Mode)
        DailyLimitChallenge oneHourLimit = new DailyLimitChallenge();
        oneHourLimit.setName("Minimalist Challenge");
        oneHourLimit.setDescription("Only 1 hour of screen time per day - Are you up for it?");
        oneHourLimit.setType(ChallengeType.DAILY_LIMIT);
        oneHourLimit.setStartDate(today);
        oneHourLimit.setEndDate(nextWeek);
        oneHourLimit.setRewardBees(200);
        oneHourLimit.setMaxDailyMinutes(60);
        Challenge saved7 = challengeRepository.save(oneHourLimit);
        createdIds.add(saved7.getId());

        return createdIds;
    }
}
