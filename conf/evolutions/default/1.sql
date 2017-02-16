#Users schema

# --- !Ups

create table ACCOUNT(
  name VARCHAR NOT NULL PRIMARY KEY,
  password VARCHAR NOT NULL,
  role VARCHAR NOT NULL
);

# --- !Downs

drop table ACCOUNT;