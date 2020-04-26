CREATE VIEW ACCOUNT_BALANCE
AS SELECT
    a.account_id, COALESCE(bal.balance, 0) as balance
FROM
    account a
LEFT JOIN
(
SELECT
    tr.account_id, SUM(tr.amt) as balance
FROM
(SELECT
    tt.to_account_id as account_id, t.amount as amt
FROM
    transaction t
INNER JOIN
    transaction_transfer tt
ON
    t.transaction_id = tt.transaction_id
WHERE
    tt.to_account_id  IS NOT NULL
AND
    t.status = 'fulfilled'
UNION
SELECT
    tt.from_account_id as account_id, 0 - t.amount as amt
FROM
    transaction t
INNER JOIN
    transaction_transfer tt
ON
    t.transaction_id = tt.transaction_id
WHERE
    tt.from_account_id IS NOT NULL
AND
    t.status = 'fulfilled') tr
GROUP BY
    tr.account_id
    ) bal
ON
    a.account_id = bal.account_id
ORDER BY
    a.account_id;
