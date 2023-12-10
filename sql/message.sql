drop table message_1;
create table message_1
(
    mid          BIGINT(20) PRIMARY KEY,
    body         VARCHAR(500) NOT NULL,
    created_time BIGINT(20)   NOT NULL,
    created_time_str char(19)   NOT NULL,
    type         BIGINT(20)   NOT NULL
);
drop table message_2;
create table message_2
(
    mid          BIGINT(20) PRIMARY KEY,
    body         VARCHAR(500) NOT NULL,
    type         BIGINT(20)   NOT NULL,
    created_time_str char(19)   NOT NULL,
    created_time BIGINT(20)   NOT NULL
);

drop table message_3;
create table message_3
(
    mid          BIGINT(20) PRIMARY KEY,
    body         VARCHAR(500) NOT NULL,
    type         BIGINT(20)   NOT NULL,
    created_time_str char(19)   NOT NULL,
    created_time BIGINT(20)   NOT NULL
);
