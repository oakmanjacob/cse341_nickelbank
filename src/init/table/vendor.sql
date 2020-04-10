create table vendor
    (vendor_id numeric(8) generated always as identity,
    name varchar(64) not null,
    primary key (vendor_id));