DROP TABLE IF EXISTS tbl_order;

create table tbl_order
(
    id               bigint      not null auto_increment primary key,
    uuid             varchar(40) not null unique,
    user_uuid        varchar(40) not null,
    billing_address  varchar(145),
    delivery_address varchar(145),
    creation_date    datetime    not null,
    user_id          bigint
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;