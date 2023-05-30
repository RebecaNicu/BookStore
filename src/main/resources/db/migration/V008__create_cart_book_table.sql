DROP TABLE IF EXISTS tbl_cart_book;

create table tbl_cart_book
(
    id         bigint not null auto_increment primary key,
    cart_id    bigint not null,
    book_id bigint not null,
    foreign key (cart_id) references tbl_cart(id),
    foreign key (book_id) references tbl_book(id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;