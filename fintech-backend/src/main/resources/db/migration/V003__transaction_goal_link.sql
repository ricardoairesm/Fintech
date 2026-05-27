-- Execute uma vez no schema Oracle para vincular transações de investimento a metas financeiras.

ALTER TABLE transactions ADD (goal_id NUMBER);

ALTER TABLE transactions ADD CONSTRAINT fk_transactions_goal
    FOREIGN KEY (goal_id) REFERENCES goal (id);
