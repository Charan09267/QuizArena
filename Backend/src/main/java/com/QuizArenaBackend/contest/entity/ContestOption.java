package com.QuizArenaBackend.contest.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contest_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContestOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "question_id",
            nullable = false
    )
    private ContestQuestion question;

    @Column(
            name = "option_text",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String optionText;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;
}