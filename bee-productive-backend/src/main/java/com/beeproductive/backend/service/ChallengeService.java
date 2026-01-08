package com.beeproductive.backend.service;

import com.beeproductive.backend.dto.ChallengeRequestDto;
import com.beeproductive.backend.dto.ChallengeResponseDto;
import com.beeproductive.backend.entity.Challenge;
import com.beeproductive.backend.mapper.ChallengeMapper;
import com.beeproductive.backend.repository.ChallengeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final ChallengeMapper challengeMapper;

    public void createChallenge(ChallengeRequestDto createChallengeRequestDto) {
        Challenge newChallenge = new Challenge();
        newChallenge.setName(createChallengeRequestDto.getName());
        newChallenge.setDescription(createChallengeRequestDto.getDescription());
        challengeRepository.save(newChallenge);
    }
    public ChallengeResponseDto getChallengeById(Long id) {
        Challenge challenge = challengeRepository.findById(id).orElseThrow(()-> new RuntimeException("Challenge not found"));
        return challengeMapper.challengeToChallengeResponseDto(challenge);
    }
    public List<ChallengeResponseDto> getAllChallenges() {
        List<Challenge> challenges=challengeRepository.findAll();
        return challengeMapper.challengeToChallengeResponseDtoList(challenges);
    }
    public void updateChallenge(Long id, ChallengeRequestDto updateChallengeRequestDto) {
        Challenge updateChallenge = challengeRepository.findById(id).orElseThrow(()-> new RuntimeException("Challenge not found"));
        updateChallenge.setName(updateChallengeRequestDto.getName());
        updateChallenge.setDescription(updateChallengeRequestDto.getDescription());
        challengeRepository.save(updateChallenge);
    }
    public void deleteChallenge(Long id) {
        Challenge challenge = challengeRepository.findById(id).orElseThrow(()-> new RuntimeException("Challenge not found"));
        challengeRepository.delete(challenge);

    }
}
