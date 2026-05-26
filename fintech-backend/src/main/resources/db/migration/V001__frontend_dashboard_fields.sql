-- Execute uma vez no schema Oracle existente antes de consumir metas e desafios pela API.

ALTER TABLE goal ADD (
    title VARCHAR2(120),
    saved_amount NUMBER(12, 2) DEFAULT 0 NOT NULL
);

UPDATE goal
SET title = 'Meta financeira #' || id
WHERE title IS NULL;

ALTER TABLE goal MODIFY (title NOT NULL);

ALTER TABLE challenge ADD (
    title VARCHAR2(120),
    progress NUMBER(3) DEFAULT 0 NOT NULL
);

UPDATE challenge
SET title = 'Desafio financeiro #' || id
WHERE title IS NULL;

ALTER TABLE challenge MODIFY (title NOT NULL);

ALTER TABLE challenge ADD CONSTRAINT ck_challenge_progress
    CHECK (progress BETWEEN 0 AND 100);
