create table address
    (address_id numeric(12) generated always as identity,
    line_1 varchar(255) not null,
    line_2 varchar(255),
    city varchar(128) not null,
    state varchar(16) not null,
    zip varchar(16) not null,
    primary key (address_id));