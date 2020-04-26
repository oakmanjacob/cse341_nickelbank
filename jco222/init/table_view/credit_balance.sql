CREATE VIEW CREDIT_BALANCE
AS SELECT
    c.card_id, COALESCE(b.balance, 0) as balance
FROM
    card c
LEFT JOIN
(
SELECT
    bal.card_id, SUM(bal.amt) as balance
FROM
(SELECT
    te.card_id, t.amount as amt
FROM
    transaction t
INNER JOIN
    transaction_external te
ON
    t.transaction_id = te.transaction_id
INNER JOIN
    card c
ON
    te.card_id = c.card_id
WHERE
    t.status = 'fulfilled'
AND
    c.type = 'credit'
UNION
SELECT
    tpc.card_id, 0 - t.amount as amt
FROM
    transaction t
INNER JOIN
    transaction_payment_credit tpc
ON
    t.transaction_id = tpc.transaction_id
WHERE
    t.status = 'fulfilled') bal
GROUP BY
    bal.card_id) b
ON
    c.card_id = b.card_id
WHERE
    c.type = 'credit'
ORDER BY
    c.card_id;
