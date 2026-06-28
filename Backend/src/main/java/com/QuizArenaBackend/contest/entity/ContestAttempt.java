package com.QuizArenaBackend.contest.entity;

import com.QuizArenaBackend.contest.entity.enums.AttemptStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "contest_attempts",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = "participant_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContestAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    private ContestParticipant participant;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal score;

    @Column(name = "total_questions")
    private Integer totalQuestions;

    @Column(name = "correct_answers")
    private Integer correctAnswers;

    @CreationTimestamp
    @Column(name = "started_at", updatable = false)
    private LocalDateTime startedAt;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AttemptStatus status;

    @Column(name = "time_taken_seconds")
    private Integer timeTakenSeconds;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @OneToMany(
            mappedBy = "contestAttempt",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<ContestQuestionAttempt> questionAttempts =
            new ArrayList<>();

    @Column(name = "wrong_answers", nullable = false)
    private Integer wrongAnswers;

    @Column(name = "unanswered_questions", nullable = false)
    private Integer unansweredQuestions;

}
