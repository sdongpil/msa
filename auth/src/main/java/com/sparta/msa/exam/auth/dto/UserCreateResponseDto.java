package com.sparta.msa.exam.auth.dto;

import com.sparta.msa.exam.auth.domain.User;

public record UserCreateResponseDto(
        Long userId,
        String name
) {

    public static UserCreateResponseDto fromEntity(User user) {
        return new UserCreateResponseDto(
                user.getUserId(),
                user.getName()
        );
    }
}
