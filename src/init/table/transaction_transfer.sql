create table transaction_transfer
    (transaction_id numeric(12),
    from_account_id numeric(12),
    to_account_id numeric(12),
    primary key (transaction_id),
    foreign key (transaction_id) references transaction
    on delete cascade,
    foreign key (from_account_id) references account(account_id)
    on delete set null,
    foreign key (to_account_id) references account(account_id)
    on delete set null);