create table transaction
    (transaction_id numeric(12) generated always as identity,
    parent_transaction_id numeric(12),
    person_id numeric(12),
    branch_id numeric(8),
    amount numeric(10,2) not null,
    type varchar(16) not null,
    status varchar(16) default 'fulfilled' not null,
    created timestamp with time zone default current_timestamp not null,
    primary key (transaction_id),
    foreign key (parent_transaction_id) references transaction(transaction_id)
    on delete set null,
    foreign key (person_id) references person,
    foreign key (branch_id) references branch
    on delete set null);