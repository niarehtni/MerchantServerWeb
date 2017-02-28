# --- !Ups

CREATE TABLE ACCOUNT (
  name     VARCHAR(15) NOT NULL PRIMARY KEY,
  password VARCHAR(10) NOT NULL,
  role     VARCHAR(10) NOT NULL
);

CREATE TABLE TRAN_LS (
  merchantNo VARCHAR(15)    NOT NULL,
  terminalNo VARCHAR(8)     NOT NULL,
  tranAmt    DECIMAL(18, 2) NOT NULL,
  tranDate   CHAR(8)        NOT NULL,
  tranTime   CHAR(6)        NOT NULL,
  slafAmt    DECIMAL(18, 2),
  feeAmt     DECIMAL(18, 2),
  rrn        VARCHAR(32),
  channel    CHAR(1)        NOT NULL,
  PRIMARY KEY (merchantNo, terminalNo, tranAmt, tranDate, tranTime, rrn, channel)
);

CREATE TABLE TRAN_LS_SUM (
  merchantNo VARCHAR(15)    NOT NULL,
  tranCount  INT            NOT NULL,
  tranFee    DECIMAL(18, 2) NOT NULL,
  tranAmt    DECIMAL(18, 2) NOT NULL,
  tranAmt1   DECIMAL(18, 2) NOT NULL,
  tranAmt2   DECIMAL(18, 2) NOT NULL,
  tranAmt3   DECIMAL(18, 2) NOT NULL,
  tranAmt4   DECIMAL(18, 2) NOT NULL,
  tranAmt5   DECIMAL(18, 2) NOT NULL,
  tranAmt6   DECIMAL(18, 2) NOT NULL,
  tranDate   CHAR(8)        NOT NULL,
  PRIMARY KEY (merchantNo, tranDate)
);

# --- !Downs


    # --- !Ups
INSERT INTO ACCOUNT VALUES ('Admin', 'Admin', 'Admin');
INSERT INTO TRAN_LS
VALUES ('111111111111111', '11111111', 100.00, '20160101', '094621', 98.00, 2.00, '1111111111', '1');
INSERT INTO TRAN_LS
VALUES ('111111111111111', '11111111', 100.00, '20160101', '094621', 98.00, 2.00, '1111111112', '1');
INSERT INTO TRAN_LS_SUM
VALUES ('Admin', 100, 999, 120000, 110000, 160000, 150000, 90000, 30000, 200000, '20170228');
# --- !Downs


DROP TABLE ACCOUNT;
DROP TABLE TRAN_LS;
DROP Table TRAN_LS_SUM;