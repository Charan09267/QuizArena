-- Drop the old foreign key
ALTER TABLE contest_attempts
DROP FOREIGN KEY fk_attempt_user;

-- Drop the user_id column
ALTER TABLE contest_attempts
DROP COLUMN user_id;

-- Add participant_id
ALTER TABLE contest_attempts
ADD COLUMN participant_id BIGINT NOT NULL AFTER contest_id;

-- Add the foreign key
ALTER TABLE contest_attempts
ADD CONSTRAINT fk_attempt_participant
FOREIGN KEY (participant_id)
REFERENCES contest_participants(id)
ON DELETE CASCADE;

-- Prevent multiple attempts for the same participant
ALTER TABLE contest_attempts
ADD CONSTRAINT uk_attempt_participant
UNIQUE(participant_id);


