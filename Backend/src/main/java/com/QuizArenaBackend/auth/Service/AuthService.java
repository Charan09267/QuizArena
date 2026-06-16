package com.QuizArenaBackend.auth.Service;

import com.QuizArenaBackend.auth.DTO.RegisterRequest;
import com.QuizArenaBackend.auth.DTO.RegisterResponse;
import com.QuizArenaBackend.user.Entity.UserEntity;
import com.QuizArenaBackend.user.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public RegisterResponse createProfile(
            RegisterRequest request
    ) {

        if (userRepository.existsByEmail(
                request.getEmail())) {

            return RegisterResponse.builder()
                    .success(false)
                    .message("Email already exists")
                    .build();
        }

        if (userRepository.existsByUsername(
                request.getUserName())) {

            return RegisterResponse.builder()
                    .success(false)
                    .message("Username already exists")
                    .build();
        }

        UserEntity newProfile =
                userRepository.save(
                        convertToUserEntity(request)
                );

        return RegisterResponse.builder()
                .success(true)
                .userId(newProfile.getId())
                .username(newProfile.getUsername())
                .email(newProfile.getEmail())
                .role(newProfile.getRole())
                .message("User registered successfully")
                .build();
    }

    private UserEntity convertToUserEntity(
            RegisterRequest request
    ) {

        return UserEntity.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .passwordHash(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .username(request.getUserName())
                .role(request.getRole())
                .isVerified(Boolean.TRUE)
                .build();
    }
}