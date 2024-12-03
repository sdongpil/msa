package com.sparta.msa.exam.auth.dto;

import com.sparta.msa.exam.auth.domain.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public record UserCreateRequestDto(
        String name,
        String password
) {
    public User toEntity(BCryptPasswordEncoder encoder) {
        return User.builder()
                .password(encoder.encode(password))
                .name(name)
                .build();
    }

}
