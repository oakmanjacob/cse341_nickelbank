create table card_debit
    (card_id numeric(12),
    account_id numeric(12),
    primary key (card_id),
    foreign key (card_id) references card
    on delete cascade,
    foreign key (account_id) references account
    on delete set null);