# --- !Ups

CREATE TABLE ACCOUNT(
  name VARCHAR(10) NOT NULL PRIMARY KEY,
  password VARCHAR(10) NOT NULL,
  role VARCHAR(10) NOT NULL
);

# --- !Downs


# --- !Ups
insert into ACCOUNT values('Admin','Admin','Admin');
# --- !Downs


drop table ACCOUNT;