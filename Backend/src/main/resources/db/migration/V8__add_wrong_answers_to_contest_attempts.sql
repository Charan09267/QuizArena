ALTER TABLE contest_attempts
    ADD COLUMN wrong_answers INT NOT NULL DEFAULT 0
    AFTER correct_answers;

ALTER TABLE contest_attempts
    ADD COLUMN unanswered_questions INT NOT NULL DEFAULT 0
    AFTER wrong_answers;