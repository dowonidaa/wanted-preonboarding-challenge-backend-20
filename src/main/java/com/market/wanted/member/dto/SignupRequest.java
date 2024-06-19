package com.market.wanted.member.dto;

import com.market.wanted.member.entity.Member;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class SignupRequest {

    @NotNull
    private String username;

    @NotNull
    private String password;
    private String name;

    @Builder
    public SignupRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .name(name)
                .role("ROLE_USER")
                .build();
    }
}
