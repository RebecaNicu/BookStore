DROP TABLE IF EXISTS tbl_book;

create table tbl_book
(
    id          bigint       not null auto_increment primary key,
    isbn        varchar(40)  not null unique,
    title       varchar(60)  not null,
    author      varchar(60)  not null unique,
    details     varchar(145),
    price       double       not null,
    category_id bigint       not null
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;