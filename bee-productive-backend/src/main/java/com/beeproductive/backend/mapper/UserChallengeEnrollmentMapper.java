package com.beeproductive.backend.mapper;

import com.beeproductive.backend.dto.UserChallengeEnrollmentResponseDto;
import com.beeproductive.backend.entity.UserChallengeEnrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserChallengeEnrollmentMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "challengeId", source = "challenge.id")
    UserChallengeEnrollmentResponseDto toDto(UserChallengeEnrollment enrollment);

    List<UserChallengeEnrollmentResponseDto> toDtoList(List<UserChallengeEnrollment> enrollments);
}

