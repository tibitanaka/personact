# 1.sql

# --- !Ups

CREATE TABLE User (
    USERID varchar(50) NOT NULL,
    PASSWORD varchar(255) NOT NULL,
    PRIMARY KEY (userId)
);

# --- !Downs

DROP TABLE User;