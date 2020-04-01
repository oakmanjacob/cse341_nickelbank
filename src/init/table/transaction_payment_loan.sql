create table transaction_payment_loan
    (transaction_id numeric(12),
    loan_id numeric(12) not null,
    primary key (transaction_id),
    foreign key (transaction_id) references transaction
    on delete cascade,
    foreign key (loan_id) references loan);