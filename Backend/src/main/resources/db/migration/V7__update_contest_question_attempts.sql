-- Set default value for marks_awarded
ALTER TABLE contest_question_attempts
    MODIFY COLUMN marks_awarded DECIMAL(5,2) NOT NULL DEFAULT 0.00;

-- Add unique constraint to prevent duplicate answers
ALTER TABLE contest_question_attempts
    ADD CONSTRAINT uk_attempt_question
        UNIQUE (contest_attempt_id, question_id);