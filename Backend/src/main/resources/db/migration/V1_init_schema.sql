-- ==========================================================
-- QUIZAREANA DATABASE INITIALIZATION
-- V1__init_schema.sql
-- SECTION 1 : USERS + SYSTEM TABLES
-- ==========================================================

-- ==========================================================
-- USERS
-- ==========================================================

CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,

                       username VARCHAR(50) NOT NULL,
                       email VARCHAR(100) NOT NULL,
                       password_hash TEXT NOT NULL,

                       full_name VARCHAR(100),

                       profile_pic_url TEXT,

                       bio TEXT,

                       role VARCHAR(30) NOT NULL,
    -- STUDENT, LEARNER, INSTITUTION, COMPANY

                       provider VARCHAR(20) DEFAULT 'LOCAL',
    -- LOCAL, GOOGLE, GITHUB, LINKEDIN

                       is_verified BOOLEAN DEFAULT FALSE,

                       status VARCHAR(20) DEFAULT 'ACTIVE',
    -- ACTIVE, SUSPENDED, DELETED

                       last_login_at TIMESTAMP NULL,

                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                           ON UPDATE CURRENT_TIMESTAMP,

                       CONSTRAINT uk_users_username UNIQUE (username),
                       CONSTRAINT uk_users_email UNIQUE (email)
) ENGINE=InnoDB;


-- ==========================================================
-- FEED EVENTS
-- ==========================================================

CREATE TABLE feed_events (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,

                             event_type VARCHAR(50) NOT NULL,
    -- QUIZ_CREATED, CONTEST_CREATED

                             entity_id BIGINT NOT NULL,

                             created_by BIGINT NULL,

                             metadata TEXT,

                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                             CONSTRAINT fk_feed_events_created_by
                                 FOREIGN KEY (created_by)
                                     REFERENCES users(id)
                                     ON DELETE SET NULL
) ENGINE=InnoDB;


-- ==========================================================
-- NOTIFICATIONS
-- ==========================================================

CREATE TABLE notifications (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,

                               user_id BIGINT NOT NULL,

                               title VARCHAR(255),

                               message TEXT,

                               type VARCHAR(50),
    -- SYSTEM, QUIZ, CONTEST, FOLLOW, REMINDER

                               is_read BOOLEAN DEFAULT FALSE,

                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT fk_notifications_user
                                   FOREIGN KEY (user_id)
                                       REFERENCES users(id)
                                       ON DELETE CASCADE
) ENGINE=InnoDB;


-- ==========================================================
-- AI REQUEST LOGS
-- ==========================================================

CREATE TABLE ai_request_logs (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                 user_id BIGINT NULL,

                                 request_type VARCHAR(50),
    -- QUIZ_GENERATION, CHAT_ASSISTANT

                                 model_name VARCHAR(50),

                                 tokens_used INT,

                                 response_time_ms INT,

                                 estimated_cost DECIMAL(10,4),

                                 status VARCHAR(20),
    -- SUCCESS, FAILED

                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                 CONSTRAINT fk_ai_logs_user
                                     FOREIGN KEY (user_id)
                                         REFERENCES users(id)
                                         ON DELETE SET NULL
) ENGINE=InnoDB;


-- ==========================================================
-- INDEXES
-- ==========================================================

CREATE INDEX idx_users_email
    ON users(email);

CREATE INDEX idx_users_username
    ON users(username);

CREATE INDEX idx_users_status
    ON users(status);

CREATE INDEX idx_users_role
    ON users(role);

CREATE INDEX idx_feed_events_created_by
    ON feed_events(created_by);

CREATE INDEX idx_feed_events_entity
    ON feed_events(entity_id);

CREATE INDEX idx_notifications_user
    ON notifications(user_id);

CREATE INDEX idx_notifications_read
    ON notifications(is_read);

CREATE INDEX idx_ai_logs_user
    ON ai_request_logs(user_id);

CREATE INDEX idx_ai_logs_request_type
    ON ai_request_logs(request_type);