package com.sparta.msa.exam.auth.dto;

public record UserSignInRequestDto (
        String name,
        String password
) {
}
