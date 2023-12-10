drop table if exists dept_1;
create table dept_1
(
    did      BIGINT(20) PRIMARY KEY,
    name     VARCHAR(10) NOT NULL,
    user_num int         NOT NULL default 0
);
drop table if exists dept_2;
create table dept_2
(
    did      BIGINT(20) PRIMARY KEY,
    name     VARCHAR(10) NOT NULL,
    user_num int         NOT NULL default 0
);
