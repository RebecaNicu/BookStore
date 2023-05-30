DROP TABLE IF EXISTS tbl_category;

create table tbl_category
(
    id      bigint       not null auto_increment primary key,
    uuid    varchar(45)  not null,
    name    varchar(145) not null unique
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;