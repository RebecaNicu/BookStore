DROP TABLE IF EXISTS tbl_order_book;

create table tbl_order_book
(
    id         bigint not null auto_increment primary key,
    order_id   bigint not null,
    book_id bigint not null,
    foreign key (order_id) references tbl_order (id),
    foreign key (book_id) references tbl_book (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;