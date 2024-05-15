package com.movieland.dto;

import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
public class UserDto {

    private int id;

    private String nickname;

    public static void main(String[] args) {
        PasswordEncoder ps = new BCryptPasswordEncoder();
        System.out.println(ps.encode("paco"));
    }
}
