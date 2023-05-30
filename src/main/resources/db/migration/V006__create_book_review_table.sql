DROP TABLE IF EXISTS tbl_book_review;

create table tbl_book_review
(
    id      bigint      not null auto_increment primary key,
    uuid    varchar(40) not null unique,
    title   varchar(40) not null,
    body    varchar(255),
    star    float,
    user_id bigint,
    book_id bigint,
    foreign key (user_id) references tbl_user (id) ON DELETE CASCADE ,
    foreign key (book_id) references tbl_book (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;