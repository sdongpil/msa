package com.sparta.msa.exam.auth;

import com.sparta.msa.exam.auth.dto.UserCreateRequestDto;
import com.sparta.msa.exam.auth.dto.UserCreateResponseDto;
import com.sparta.msa.exam.auth.dto.UserSignInRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/auth/signIn")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserSignInRequestDto requestDto){
        return ResponseEntity.ok(new AuthResponse(authService.createAccessToken(requestDto)));
    }

    @PostMapping("/auth/signUp")
    public ResponseEntity<?> createUser(@RequestBody UserCreateRequestDto requestDto){
        UserCreateResponseDto responseDto = authService.createUser(requestDto);

        return ResponseEntity.ok(responseDto);
    }


    /**
     * JWT 액세스 토큰을 포함하는 응답 객체입니다.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class AuthResponse {
        private String access_token;

    }
}