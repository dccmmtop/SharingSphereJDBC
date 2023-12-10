drop database if exists course_1;
create table course_1
(
    cid     BIGINT(20) PRIMARY KEY,
    cname   VARCHAR(50) NOT NULL,
    user_id BIGINT(20)  NOT NULL,
    cstatus varchar(10) NOT NULL
);
drop database if exists course_2;
create table course_2
(
    cid     BIGINT(20) PRIMARY KEY,
    cname   VARCHAR(50) NOT NULL,
    user_id BIGINT(20)  NOT NULL,
    cstatus varchar(10) NOT NULL
);
