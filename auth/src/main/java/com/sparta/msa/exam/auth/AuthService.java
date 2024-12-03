package com.sparta.msa.exam.auth;

import com.sparta.msa.exam.auth.domain.User;
import com.sparta.msa.exam.auth.domain.UserRepository;
import com.sparta.msa.exam.auth.dto.UserCreateRequestDto;
import com.sparta.msa.exam.auth.dto.UserCreateResponseDto;
import com.sparta.msa.exam.auth.dto.UserSignInRequestDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class AuthService {

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;
    private final SecretKey secretKey;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * AuthService 생성자.
     * Base64 URL 인코딩된 비밀 키를 디코딩하여 HMAC-SHA 알고리즘에 적합한 SecretKey 객체를 생성합니다.
     *
     * @param secretKey Base64 URL 인코딩된 비밀 키
     */
    public AuthService(@Value("${service.jwt.secret-key}") String secretKey, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public String createAccessToken(UserSignInRequestDto requestDto) {
        User user = userRepository.findByName(requestDto.name()).orElseThrow();
        boolean matches = passwordEncoder.matches(requestDto.password(), user.getPassword());
        if (!matches) {
            throw new IllegalArgumentException("비밀번호 불일치");
        }

        return Jwts.builder()
                // 사용자 ID를 클레임으로 설정
                .claim("name", user.getName())
                .claim("role", "user")
                // JWT 발행자를 설정
                .issuer(issuer)
                // JWT 발행 시간을 현재 시간으로 설정
                .issuedAt(new Date(System.currentTimeMillis()))
                // JWT 만료 시간을 설정
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                // SecretKey를 사용하여 HMAC-SHA512 알고리즘으로 서명
                .signWith(secretKey, io.jsonwebtoken.SignatureAlgorithm.HS512)
                // JWT 문자열로 컴팩트하게 변환
                .compact();
    }

    @Transactional
    public UserCreateResponseDto createUser(UserCreateRequestDto requestDto) {
        if (userRepository.existsByName(requestDto.name())) {
            throw new IllegalArgumentException("중복된 이름입니다.");
        }

        User entity = requestDto.toEntity(passwordEncoder);

        User user = userRepository.save(entity);

        return UserCreateResponseDto.fromEntity(user);
    }
}