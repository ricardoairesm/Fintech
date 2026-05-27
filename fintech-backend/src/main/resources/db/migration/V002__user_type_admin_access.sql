-- Execute uma vez no schema Oracle antes de utilizar login, cadastro ou painel administrativo.

ALTER TABLE users ADD (
    user_type VARCHAR2(20) DEFAULT 'USER' NOT NULL
);

ALTER TABLE users ADD CONSTRAINT ck_users_user_type
    CHECK (user_type IN ('USER', 'ADMIN'));

-- Defina manualmente quais contas poderão acessar o painel administrativo:
-- UPDATE users SET user_type = 'ADMIN' WHERE email = 'admin@finup.com';
-- COMMIT;
