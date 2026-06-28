package com.QuizArenaBackend.contest.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "contest_question_attempts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_attempt_question",
                        columnNames = {
                                "contest_attempt_id",
                                "question_id"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContestQuestionAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "contest_attempt_id",
            nullable = false
    )
    private ContestAttempt contestAttempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "question_id",
            nullable = false
    )
    private ContestQuestion question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_option_id")
    private ContestOption selectedOption;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(
            name = "marks_awarded",
            nullable = false
    )
    private BigDecimal marksAwarded;

    @CreationTimestamp
    @Column(
            name = "answered_at",
            updatable = false
    )
    private LocalDateTime answeredAt;
}
