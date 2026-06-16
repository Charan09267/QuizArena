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
    -- STUDENT, LEARNER, INSTITUTION, COMPANY , TEST

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
-- CONTEST SYSTEM
-- ==========================================================

CREATE TABLE contests (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,

                          title VARCHAR(255) NOT NULL,

                          description TEXT,

                          created_by BIGINT NOT NULL,

                          visibility VARCHAR(20) DEFAULT 'PUBLIC',
    -- PUBLIC, PRIVATE

                          start_time TIMESTAMP NOT NULL,

                          end_time TIMESTAMP NOT NULL,

                          duration_seconds INT,
    -- optional per-user duration

                          max_participants INT,

                          contest_type VARCHAR(50),
    -- PRACTICE, HIRING, CAMPUS, OPEN

                          status VARCHAR(20) DEFAULT 'UPCOMING',
    -- DRAFT, UPCOMING, LIVE,
    -- COMPLETED, CANCELLED

                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                              ON UPDATE CURRENT_TIMESTAMP,

                          CONSTRAINT fk_contests_created_by
                              FOREIGN KEY (created_by)
                                  REFERENCES users(id)
                                  ON DELETE RESTRICT
) ENGINE=InnoDB;


-- ==========================================================
-- TAGS
-- ==========================================================

CREATE TABLE tags (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,

                      name VARCHAR(100) NOT NULL,

                      category VARCHAR(50),
    -- TECH, APTITUDE, FRAMEWORK, EXAM

                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                      CONSTRAINT uk_tags_name UNIQUE (name)
) ENGINE=InnoDB;


-- ==========================================================
-- CONTEST TAGS (M:N Mapping)
-- ==========================================================

CREATE TABLE contest_tags (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,

                              contest_id BIGINT NOT NULL,

                              tag_id BIGINT NOT NULL,

                              CONSTRAINT fk_contest_tags_contest
                                  FOREIGN KEY (contest_id)
                                      REFERENCES contests(id)
                                      ON DELETE CASCADE,

                              CONSTRAINT fk_contest_tags_tag
                                  FOREIGN KEY (tag_id)
                                      REFERENCES tags(id)
                                      ON DELETE CASCADE,

                              CONSTRAINT uk_contest_tag
                                  UNIQUE (contest_id, tag_id)
) ENGINE=InnoDB;


-- ==========================================================
-- CONTEST PARTICIPANTS
-- ==========================================================

CREATE TABLE contest_participants (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                      contest_id BIGINT NOT NULL,

                                      user_id BIGINT NOT NULL,

                                      joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                      status VARCHAR(20) DEFAULT 'JOINED',
    -- JOINED, STARTED,
    -- SUBMITTED, DISQUALIFIED

                                      CONSTRAINT fk_contest_participant_contest
                                          FOREIGN KEY (contest_id)
                                              REFERENCES contests(id)
                                              ON DELETE CASCADE,

                                      CONSTRAINT fk_contest_participant_user
                                          FOREIGN KEY (user_id)
                                              REFERENCES users(id)
                                              ON DELETE RESTRICT,

                                      CONSTRAINT uk_contest_user
                                          UNIQUE (contest_id, user_id)
) ENGINE=InnoDB;


-- ==========================================================
-- CONTEST QUESTIONS
-- ==========================================================

CREATE TABLE contest_questions (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                   contest_id BIGINT NOT NULL,

                                   question_text TEXT NOT NULL,

                                   question_type VARCHAR(50),
    -- MCQ, TRUE_FALSE, MULTI_SELECT

                                   marks INT DEFAULT 1,

                                   explanation TEXT,

                                   order_index INT,

                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                   CONSTRAINT fk_contest_questions_contest
                                       FOREIGN KEY (contest_id)
                                           REFERENCES contests(id)
                                           ON DELETE CASCADE
) ENGINE=InnoDB;


-- ==========================================================
-- CONTEST OPTIONS
-- ==========================================================

CREATE TABLE contest_options (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                 question_id BIGINT NOT NULL,

                                 option_text TEXT NOT NULL,

                                 is_correct BOOLEAN DEFAULT FALSE,

                                 CONSTRAINT fk_contest_options_question
                                     FOREIGN KEY (question_id)
                                         REFERENCES contest_questions(id)
                                         ON DELETE CASCADE
) ENGINE=InnoDB;


-- ==========================================================
-- CONTEST ATTEMPTS
-- ==========================================================

CREATE TABLE contest_attempts (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                  contest_id BIGINT NOT NULL,

                                  user_id BIGINT NOT NULL,

                                  score DECIMAL(10,2) DEFAULT 0.00,

                                  total_questions INT DEFAULT 0,

                                  correct_answers INT DEFAULT 0,

                                  started_at TIMESTAMP NULL,

                                  submitted_at TIMESTAMP NULL,

                                  expires_at TIMESTAMP NULL,
    -- calculated from duration_seconds

                                  status VARCHAR(20),
    -- STARTED, SUBMITTED, TIME_EXPIRED

                                  CONSTRAINT fk_attempt_contest
                                      FOREIGN KEY (contest_id)
                                          REFERENCES contests(id)
                                          ON DELETE CASCADE,

                                  CONSTRAINT fk_attempt_user
                                      FOREIGN KEY (user_id)
                                          REFERENCES users(id)
                                          ON DELETE RESTRICT
) ENGINE=InnoDB;


-- ==========================================================
-- QUESTION ATTEMPTS
-- ==========================================================

CREATE TABLE contest_question_attempts (
                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                           contest_attempt_id BIGINT NOT NULL,

                                           question_id BIGINT NOT NULL,

                                           selected_option_id BIGINT,

                                           is_correct BOOLEAN,

                                           marks_awarded DECIMAL(5,2),

                                           answered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                           CONSTRAINT fk_question_attempt_attempt
                                               FOREIGN KEY (contest_attempt_id)
                                                   REFERENCES contest_attempts(id)
                                                   ON DELETE CASCADE,

                                           CONSTRAINT fk_question_attempt_question
                                               FOREIGN KEY (question_id)
                                                   REFERENCES contest_questions(id)
                                                   ON DELETE CASCADE,

                                           CONSTRAINT fk_question_attempt_option
                                               FOREIGN KEY (selected_option_id)
                                                   REFERENCES contest_options(id)
                                                   ON DELETE SET NULL
) ENGINE=InnoDB;


-- ==========================================================
-- LEADERBOARDS
-- ==========================================================

CREATE TABLE leaderboards (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,

                              contest_id BIGINT NOT NULL,

                              user_id BIGINT NOT NULL,

                              score DECIMAL(10,2) DEFAULT 0.00,

                              rank_position INT,

                              completion_time_seconds INT,

                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                                  ON UPDATE CURRENT_TIMESTAMP,

                              CONSTRAINT fk_leaderboard_contest
                                  FOREIGN KEY (contest_id)
                                      REFERENCES contests(id)
                                      ON DELETE CASCADE,

                              CONSTRAINT fk_leaderboard_user
                                  FOREIGN KEY (user_id)
                                      REFERENCES users(id)
                                      ON DELETE RESTRICT,

                              CONSTRAINT uk_leaderboard_contest_user
                                  UNIQUE (contest_id, user_id)
) ENGINE=InnoDB;








-- ==========================================================
-- LIVE QUIZ SYSTEM
-- ==========================================================

CREATE TABLE quizzes (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,

                         title VARCHAR(255) NOT NULL,

                         description TEXT,

                         thumbnail_url TEXT,

                         created_by BIGINT NOT NULL,

                         visibility VARCHAR(20) DEFAULT 'PUBLIC',
    -- PUBLIC, PRIVATE

                         difficulty VARCHAR(20),
    -- EASY, MEDIUM, HARD

                         total_questions INT DEFAULT 0,

                         total_marks INT DEFAULT 0,

                         is_ai_generated BOOLEAN DEFAULT FALSE,

                         status VARCHAR(20) DEFAULT 'DRAFT',
    -- DRAFT, UPCOMING,
    -- LIVE, COMPLETED

                         start_time TIMESTAMP NULL,

                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                             ON UPDATE CURRENT_TIMESTAMP,

                         CONSTRAINT fk_quizzes_created_by
                             FOREIGN KEY (created_by)
                                 REFERENCES users(id)
                                 ON DELETE RESTRICT
) ENGINE=InnoDB;


-- ==========================================================
-- INVITE CODES (ONE-TO-ONE WITH QUIZ)
-- ==========================================================

CREATE TABLE invite_codes (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,

                              code VARCHAR(30) NOT NULL,

                              quiz_id BIGINT NOT NULL,

                              created_by BIGINT NOT NULL,

                              expires_at TIMESTAMP NULL,

                              max_uses INT,

                              current_uses INT DEFAULT 0,

                              is_active BOOLEAN DEFAULT TRUE,

                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                              CONSTRAINT uk_invite_code UNIQUE (code),

                              CONSTRAINT uk_invite_quiz
                                  UNIQUE (quiz_id),

                              CONSTRAINT fk_invite_quiz
                                  FOREIGN KEY (quiz_id)
                                      REFERENCES quizzes(id)
                                      ON DELETE CASCADE,

                              CONSTRAINT fk_invite_created_by
                                  FOREIGN KEY (created_by)
                                      REFERENCES users(id)
                                      ON DELETE RESTRICT
) ENGINE=InnoDB;


-- ==========================================================
-- QUIZ QUESTIONS
-- ==========================================================

CREATE TABLE questions (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,

                           quiz_id BIGINT NOT NULL,

                           question_text TEXT NOT NULL,

                           question_type VARCHAR(50),
    -- MCQ, MULTI_SELECT, TRUE_FALSE

                           marks INT DEFAULT 1,

                           explanation TEXT,

                           order_index INT NOT NULL,

                           time_limit_seconds INT NOT NULL,

                           CONSTRAINT fk_questions_quiz
                               FOREIGN KEY (quiz_id)
                                   REFERENCES quizzes(id)
                                   ON DELETE CASCADE
) ENGINE=InnoDB;


-- ==========================================================
-- QUESTION OPTIONS
-- ==========================================================

CREATE TABLE options (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,

                         question_id BIGINT NOT NULL,

                         option_text TEXT NOT NULL,

                         is_correct BOOLEAN DEFAULT FALSE,

                         CONSTRAINT fk_options_question
                             FOREIGN KEY (question_id)
                                 REFERENCES questions(id)
                                 ON DELETE CASCADE
) ENGINE=InnoDB;


-- ==========================================================
-- QUIZ PARTICIPANTS
-- ==========================================================

CREATE TABLE quiz_participants (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                   quiz_id BIGINT NOT NULL,

                                   user_id BIGINT NOT NULL,

                                   joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                   status VARCHAR(20) DEFAULT 'JOINED',
    -- JOINED, LEFT, DISQUALIFIED

                                   CONSTRAINT fk_quiz_participant_quiz
                                       FOREIGN KEY (quiz_id)
                                           REFERENCES quizzes(id)
                                           ON DELETE CASCADE,

                                   CONSTRAINT fk_quiz_participant_user
                                       FOREIGN KEY (user_id)
                                           REFERENCES users(id)
                                           ON DELETE RESTRICT,

                                   CONSTRAINT uk_quiz_user
                                       UNIQUE (quiz_id, user_id)
) ENGINE=InnoDB;


-- ==========================================================
-- LIVE QUIZ SESSION
-- ==========================================================

CREATE TABLE quiz_sessions (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,

                               quiz_id BIGINT NOT NULL,

                               current_question_order INT DEFAULT 1,

                               status VARCHAR(20) DEFAULT 'WAITING',
    -- WAITING, LIVE,
    -- PAUSED, COMPLETED

                               started_at TIMESTAMP NULL,

                               ended_at TIMESTAMP NULL,

                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT fk_quiz_session_quiz
                                   FOREIGN KEY (quiz_id)
                                       REFERENCES quizzes(id)
                                       ON DELETE CASCADE
) ENGINE=InnoDB;


-- ==========================================================
-- QUIZ ANSWERS
-- ==========================================================

CREATE TABLE quiz_answers (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,

                              quiz_session_id BIGINT NOT NULL,

                              user_id BIGINT NOT NULL,

                              question_id BIGINT NOT NULL,

                              selected_option_id BIGINT NULL,

                              is_correct BOOLEAN,

                              marks_awarded DECIMAL(5,2),

                              response_time_ms INT,

                              answered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                              CONSTRAINT fk_quiz_answers_session
                                  FOREIGN KEY (quiz_session_id)
                                      REFERENCES quiz_sessions(id)
                                      ON DELETE CASCADE,

                              CONSTRAINT fk_quiz_answers_user
                                  FOREIGN KEY (user_id)
                                      REFERENCES users(id)
                                      ON DELETE RESTRICT,

                              CONSTRAINT fk_quiz_answers_question
                                  FOREIGN KEY (question_id)
                                      REFERENCES questions(id)
                                      ON DELETE CASCADE,

                              CONSTRAINT fk_quiz_answers_option
                                  FOREIGN KEY (selected_option_id)
                                      REFERENCES options(id)
                                      ON DELETE SET NULL
) ENGINE=InnoDB;


-- ==========================================================
-- LEADERBOARD SNAPSHOTS
-- ==========================================================

CREATE TABLE leaderboard_snapshots (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                       quiz_session_id BIGINT NOT NULL,

                                       question_order INT NOT NULL,

                                       user_id BIGINT NOT NULL,

                                       score DECIMAL(10,2) DEFAULT 0.00,

                                       rank_position INT,

                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                       CONSTRAINT fk_snapshot_session
                                           FOREIGN KEY (quiz_session_id)
                                               REFERENCES quiz_sessions(id)
                                               ON DELETE CASCADE,

                                       CONSTRAINT fk_snapshot_user
                                           FOREIGN KEY (user_id)
                                               REFERENCES users(id)
                                               ON DELETE RESTRICT
) ENGINE=InnoDB;


-- ==========================================================
-- INDEXES
-- ==========================================================

CREATE INDEX idx_quizzes_created_by
    ON quizzes(created_by);

CREATE INDEX idx_quizzes_status
    ON quizzes(status);

CREATE INDEX idx_quizzes_visibility
    ON quizzes(visibility);

CREATE INDEX idx_questions_quiz
    ON questions(quiz_id);

CREATE INDEX idx_questions_order
    ON questions(order_index);

CREATE INDEX idx_options_question
    ON options(question_id);

CREATE INDEX idx_quiz_participants_quiz
    ON quiz_participants(quiz_id);

CREATE INDEX idx_quiz_participants_user
    ON quiz_participants(user_id);

CREATE INDEX idx_quiz_sessions_quiz
    ON quiz_sessions(quiz_id);

CREATE INDEX idx_quiz_answers_user
    ON quiz_answers(user_id);

CREATE INDEX idx_quiz_answers_session
    ON quiz_answers(quiz_session_id);

CREATE INDEX idx_quiz_answers_question
    ON quiz_answers(question_id);

CREATE INDEX idx_snapshots_session
    ON leaderboard_snapshots(quiz_session_id);

CREATE INDEX idx_snapshots_rank
    ON leaderboard_snapshots(rank_position);

-- ==========================================================
-- INDEXES
-- ==========================================================

CREATE INDEX idx_contests_created_by
    ON contests(created_by);

CREATE INDEX idx_contests_status
    ON contests(status);

CREATE INDEX idx_contests_visibility
    ON contests(visibility);

CREATE INDEX idx_contest_participants_contest
    ON contest_participants(contest_id);

CREATE INDEX idx_contest_participants_user
    ON contest_participants(user_id);

CREATE INDEX idx_contest_questions_contest
    ON contest_questions(contest_id);

CREATE INDEX idx_contest_attempts_user
    ON contest_attempts(user_id);

CREATE INDEX idx_contest_attempts_contest
    ON contest_attempts(contest_id);

CREATE INDEX idx_leaderboards_contest
    ON leaderboards(contest_id);

CREATE INDEX idx_leaderboards_rank
    ON leaderboards(rank_position);

CREATE INDEX idx_tags_name
    ON tags(name);


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