CREATE TABLE questions
(
    id             INT PRIMARY KEY,
    type           VARCHAR(50) NOT NULL,
    question_text  TEXT        NOT NULL,
    reference_type VARCHAR(50),
    is_required    BOOLEAN     NOT NULL
);


CREATE TABLE options
(
    id          INT PRIMARY KEY,
    question_id INT  NOT NULL,
    text        TEXT NOT NULL,
    CONSTRAINT fk_options_question
        FOREIGN KEY (question_id)
            REFERENCES questions (id)
            ON DELETE CASCADE
);