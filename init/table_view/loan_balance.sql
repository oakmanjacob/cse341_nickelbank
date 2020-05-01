CREATE VIEW LOAN_BALANCE
AS SELECT
    l.loan_id, l.amount - tr.balance as balance
FROM
    loan l
LEFT JOIN
(
SELECT
    tpl.loan_id, COALESCE(SUM(t.amount), 0) as balance
FROM
    transaction t
LEFT JOIN
    transaction_payment_loan tpl
ON
    t.transaction_id = tpl.transaction_id
WHERE
    t.status = 'fulfilled'
GROUP BY
    tpl.loan_id
ORDER BY
    tpl.loan_id) tr
ON
    l.loan_id = tr.loan_id;