package com.QuizArenaBackend.user.Entity;

import com.QuizArenaBackend.contest.entity.ContestParticipant;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_users_username",
                        columnNames = "username"
                ),
                @UniqueConstraint(
                        name = "uk_users_email",
                        columnNames = "email"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "username",
            nullable = false,
            length = 50
    )
    private String username;

    @Column(
            name = "email",
            nullable = false,
            length = 100
    )
    private String email;

    @Column(
            name = "password_hash",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String passwordHash;

    @Column(
            name = "first_name",
            length = 100
    )
    private String firstName;

    @Column(
            name = "last_name",
            length = 100
    )
    private String lastName;

    @Column(
            name = "profile_pic_url",
            columnDefinition = "TEXT"
    )
    private String profilePicUrl;

    @Column(
            name = "role",
            nullable = false,
            length = 30
    )
    private String role = "LEARNER";

    @Column(
            name = "provider",
            nullable = false,
            length = 20
    )
    private String provider = "LOCAL";

    @Column(
            name = "is_verified",
            nullable = false
    )
    private Boolean isVerified = false;

    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    private String status = "ACTIVE";

    @Column(
            name = "profile_completed",
            nullable = false
    )
    private Boolean profileCompleted = false;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;

        if (this.role == null) {
            this.role = "LEARNER";
        }

        if (this.provider == null) {
            this.provider = "LOCAL";
        }

        if (this.status == null) {
            this.status = "ACTIVE";
        }

        if (this.isVerified == null) {
            this.isVerified = false;
        }

        if (this.profileCompleted == null) {
            this.profileCompleted = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}