drop table if exists user_1;
create table user_1
(
    uid  BIGINT(20) PRIMARY KEY,
    name VARCHAR(10) NOT NULL,
    age  int         NOT NULL,
    sex  tinyint     NOT NULL
);
drop table if exists user_2;
create table user_2
(
    uid  BIGINT(20) PRIMARY KEY,
    name VARCHAR(10) NOT NULL,
    age  int         NOT NULL,
    sex  tinyint     NOT NULL
);
