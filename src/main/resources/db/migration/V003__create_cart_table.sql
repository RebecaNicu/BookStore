DROP TABLE IF EXISTS tbl_cart;

create table tbl_cart
(
    id        bigint      not null auto_increment primary key,
    uuid      varchar(40) not null,
    user_id   bigint not null,
    foreign key (user_id) references tbl_user(id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;