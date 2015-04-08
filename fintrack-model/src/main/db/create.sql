--
-- users table 
--
CREATE TABLE users (
    user_id     text,
    passwd      text,

	PRIMARY KEY (user_id)
);

--
-- roles table 
--
CREATE TABLE user_roles (
    id          int,
    user_id     text, 
    role        text,

	PRIMARY KEY (id),
	FOREIGN KEY (user_id) REFERENCES users (user_id)
);
CREATE SEQUENCE user_role_seq START WITH 10 INCREMENT BY 1 NO MAXVALUE NO MINVALUE CACHE 1;

--
-- categories table 
--
CREATE TABLE categories (
	category_id	varchar(2) NOT NULL UNIQUE,
	name		text,
	name_short	text,
    order_pos   int NOT NULL DEFAULT 0,

	PRIMARY KEY (category_id)
);

--
-- expenses table
--
CREATE TABLE expenses (
	expense_id	int NOT NULL,
	category_id	varchar(2) NOT NULL,
	user_id		varchar(10) NOT NULL DEFAULT 'none',
	create_date	timestamp,
	amount		float4,
	descr		text,
	
	PRIMARY KEY (expense_id),
	FOREIGN KEY (category_id) REFERENCES categories (category_id)
);

CREATE SEQUENCE expense_seq START WITH 10000 INCREMENT BY 1 NO MAXVALUE NO MINVALUE CACHE 1;
--SELECT pg_catalog.setval('expense_seq', 10000, false);

--
-- incomes table
--
CREATE TABLE incomes (
	income_id	int NOT NULL,
	user_id		varchar(10) NOT NULL DEFAULT 'none',
	create_date	timestamp,
	amount		float4,
	descr		text,

	PRIMARY KEY (income_id)
);

CREATE SEQUENCE income_seq START WITH 5000 INCREMENT BY 1 NO MAXVALUE NO MINVALUE CACHE 1;
--SELECT pg_catalog.setval('income_seq', 10000, false);

