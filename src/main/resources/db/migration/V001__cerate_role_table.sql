DROP TABLE IF EXISTS tbl_role;
create table tbl_role
(
    id   bigint       not null auto_increment primary key,
    name varchar(255) not null unique
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;