-- ==========================================================
-- V2 : UPDATE USERS TABLE
-- ==========================================================

-- Remove bio column
ALTER TABLE users
DROP COLUMN bio;


-- Rename full_name → first_name
ALTER TABLE users
    CHANGE COLUMN full_name first_name VARCHAR(100);


-- Add last_name column
ALTER TABLE users
    ADD COLUMN last_name VARCHAR(100)
    AFTER first_name;


-- Change default role to LEARNER
ALTER TABLE users
    ALTER COLUMN role SET DEFAULT 'LEARNER';