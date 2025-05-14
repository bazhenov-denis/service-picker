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
    question_id INT  NOT NULL
        REFERENCES questions (id)
            ON DELETE CASCADE,
    id          INT  NOT NULL,
    option_text TEXT NOT NULL,
    PRIMARY KEY (question_id, id)
);