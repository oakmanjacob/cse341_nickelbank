create table transaction_external
    (transaction_id numeric(12),
    card_id numeric(12) not null,
    vendor_id numeric(8) not null,
    primary key (transaction_id),
    foreign key (card_id) references card,
    foreign key (vendor_id) references vendor);