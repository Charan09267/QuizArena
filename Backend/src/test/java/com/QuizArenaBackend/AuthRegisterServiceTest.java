package com.QuizArenaBackend;

import com.QuizArenaBackend.auth.Controller.AuthController;
import com.QuizArenaBackend.auth.DTO.RegisterRequest;
import com.QuizArenaBackend.auth.DTO.RegisterResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class AuthRegisterServiceTest {

    @Autowired
    private AuthController authController;

    @Test
    void registerTest() {

        RegisterRequest request =
                new RegisterRequest(
                        "charan_2609",
                        "charan@gmail.com",
                        "charan@2609",
                        "Charan",
                        "Immati",
                        "LEARNER"
                );

        ResponseEntity<RegisterResponse> response =
                authController.register(request);

        assertNotNull(response);

        assertEquals(
                HttpStatus.CREATED,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertEquals(
                "charan@gmail.com",
                response.getBody().getEmail()
        );

        assertEquals(
                "charan_2609",
                response.getBody().getUsername()
        );

        System.out.println(response.getBody());
    }
}
