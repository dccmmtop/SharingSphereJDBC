drop table if exists dict;
create table dict
(
    did  BIGINT(20) PRIMARY KEY,
    type tinyint NOT NULL,
    keyword int          NOT NULL,
    value VARCHAR(50) NOT NULL
);
