CREATE TABLE users_role
(
    user_id             BIGINT  NOT NULL,
    theory_task_id BIGINT,
    text           VARCHAR(255),
    is_right       BOOLEAN NOT NULL,
    CONSTRAINT pk_theory_task_probably_answer PRIMARY KEY (id)
);