# --- !Ups

CREATE TABLE ACCOUNT(
  name VARCHAR(10) NOT NULL PRIMARY KEY,
  password VARCHAR(10) NOT NULL,
  role VARCHAR(10) NOT NULL
);

CREATE TABLE TRAN_LS(
  merchantNo VARCHAR(15) NOT NULL,
  terminalNo VARCHAR(8) NOT NULL ,
  tranAmt DECIMAL(18,2) NOT NULL ,
  tranDate CHAR(8) NOT NULL ,
  tranTime CHAR(6) NOT NULL ,
  slafAmt DECIMAL(18,2),
  feeAmt DECIMAL(18,2),
  rrn VARCHAR(18),
  batchNo VARCHAR(10),
  tiackNo VARCHAR(18),
  traceNo VARCHAR(32),
  channel CHAR(1) NOT NULL ,
  PRIMARY KEY (merchantNo,terminalNo,tranAmt,tranDate,tranTime,channel)
);

# --- !Downs


# --- !Ups
insert into ACCOUNT values('Admin','Admin','Admin');
insert into TRAN_LS values('111111111111111','11111111',100.00,'20160101','094621',98.00,2.00,'1111111111','000001','0000001','11111111','1');
# --- !Downs


drop table ACCOUNT;