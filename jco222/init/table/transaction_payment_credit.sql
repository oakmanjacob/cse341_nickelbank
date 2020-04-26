create table transaction_payment_credit
    (transaction_id numeric(12),
    card_id numeric(12) not null,
    primary key (transaction_id),
    foreign key (transaction_id) references transaction
    on delete cascade,
    foreign key (card_id) references card_credit);