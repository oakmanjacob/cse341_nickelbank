create table person_account
    (person_id numeric(12),
    account_id numeric(12),
    primary key (person_id, account_id),
    foreign key (person_id) references person,
    foreign key (account_id) references account);