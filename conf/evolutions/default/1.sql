# --- !Ups

CREATE TABLE ACCOUNT(
  name VARCHAR(10) NOT NULL PRIMARY KEY,
  password VARCHAR(10) NOT NULL,
  role VARCHAR(10) NOT NULL
);
# --- !Downs

drop table ACCOUNT;