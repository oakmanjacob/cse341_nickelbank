create table person
	(person_id varchar(12),
	first_name varchar(64) not null,
	last_name varchar(64) not null,
	email varchar(255) not null,
	phone varchar(16) not null,
	birth_date date not null,
	created timestamp with time zone default current_timestamp not null,
	primary key (person_id));

create table address
	(address_id varchar(12),
	line_1 varchar(255) not null,
	line_2 varchar(255),
	city varchar(128) not null,
	state varchar(16) not null,
	zip varchar(16) not null,
	primary key (address_id));

create table property
(person_id varchar(12),
	address_id varchar(12),
	primary key (person_id, address_id),
	foreign key (person_id) references person
on delete cascade,
	foreign key (address_id) references address
on delete cascade);

create table account
	(account_id varchar(12),
account_number varchar(20) not null,
type varchar(10) default 'checking' not null,
interest_rate numeric(2, 3) default 0 not null,
min_balance numeric(4, 0) default 0 not null,
 	created timestamp with time zone default current_timestamp not null,
primary key (account_id),
unique (account_number));

create table person_account
	(person_id varchar(12),
	account_id varchar(12),
	primary key (person_id, account_id),
	foreign key (person_id) references person,
	foreign key (account_id) references account);

create table loan
	(loan_id varchar(12),
	person_id varchar(12),
	type varchar(10) not null default 'unsecured',
	amount numeric(8,2) not null,
interest_rate numeric(3,2) not null default 0,
monthly_payment numeric(8,2) not null default 0,
created timestamp with time zone default current_timestamp not null,
primary key (loan_id),
foreign key (person_id) references person);

create table loan_secured
	(loan_id varchar(12),
	address_id varchar(12),
	primary key (loan_id),
	foreign key (loan_id) references loan
		on delete cascade,
foreign key (address_id) references address
        		on delete set null);

create table branch
	(branch_id varchar(8),
	address_id varchar(12) not null,
	type varchar(8) default 'full' not null,
	primary key (branch_id),
	foreign key (address_id) references address);


create table card
	(card_id varchar(12),
	person_id varchar(12) not null,
	type varchar(8) default 'debit' not null,
	card_number varchar(16) not null,
	cvc varchar(4) not null,
	status varchar(16) not null,
	created timestamp with time zone default current_timestamp not null,
	modified timestamp with time zone,
	primary key (card_id),
	foreign key (person_id) references person);

create table card_credit
	(card_id varchar(12),
	credit_limit numeric(8,2) default 0 not null,
	interest_rate numeric(2,3) default 0 not null,
	primary key (card_id),
	foreign key (card_id) references card
		on delete cascade);

create table card_debit
	(card_id varchar(12),
	account_id varchar(12),
	primary key (card_id),
	foreign key (card_id) references card
		on delete cascade,
	foreign key (account_id) references account
		on delete set null);

create table transaction
	(transaction_id varchar(12),
	parent_transaction_id varchar(12),
	person_id varchar(12),
	branch_id varchar(12),
	amount numeric(8,2) not null,
	type varchar(16) not null,
	status varchar(16) default 'fulfilled' not null,
	created timestamp with time zone default current_timestamp not null,
	primary key (transaction_id),
	foreign key (parent_transaction_id) references transaction(transaction_id)
		on delete set null,
	foreign key (person_id) references person,
	foreign key (branch_id) references branch
		on delete set null);

create table transaction_transfer
	(transaction_id varchar(12),
	from_account_id varchar(12),
	to_account_id varchar(12),
	primary key (transaction_id),
	foreign key (transaction_id) references transaction
		on delete cascade,
	foreign key (from_account_id) references account(account_id)
        on delete set null,
	foreign key (to_account_id) references account(account_id)
		on delete set null);


create table transaction_payment_loan
	(transaction_id varchar(12),
	loan_id varchar(12) not null,
	primary key (transaction_id),
	foreign key (transaction_id) references transaction
		on delete cascade,
	foreign key (loan_id) references loan);

create table transaction_payment_credit
	(transaction_id varchar(12),
	card_id varchar(12) not null,
	primary key (transaction_id),
	foreign key (transaction_id) references transaction
		on delete cascade,
	foreign key (card_id) references card_credit);

create table vendor
(vendor_id varchar(8),
	name varchar(64) not null,
	primary key (vendor_id));

create table transaction_external
	(transaction_id varchar(12),
	card_id varchar(12) not null,
	vendor_id varchar(8) not null,
	primary key (transaction_id),
	foreign key (card_id) references card,
	foreign key (vendor_id) references vendor);
