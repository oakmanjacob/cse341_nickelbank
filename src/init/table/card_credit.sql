create table card_credit
    (card_id numeric(12),
    credit_limit numeric(10,2) default 0 not null,
    interest_rate numeric(5,3) default 0 not null,
    primary key (card_id),
    foreign key (card_id) references card
    on delete cascade);