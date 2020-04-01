create table loan_secured
  (loan_id numeric(12),
  address_id numeric(12),
  primary key (loan_id),
  foreign key (loan_id) references loan
  on delete cascade,
  foreign key (address_id) references address
  on delete set null);