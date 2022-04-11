INSERT INTO account(id, country, customer_id, created_by, created_at)
VALUES (9211, 'EST', 8211, 'test', now());
INSERT INTO account(id, country, customer_id, created_by, created_at)
VALUES (9212, 'EST', 8211, 'test', now());

INSERT INTO balance(account_id, amount, currency, created_by, created_at)
VALUES (9211, 140.23, 'EUR', 'test', now());
