DROP TABLE IF EXISTS tbl_user;
create table tbl_user
(
    id         bigint       not null AUTO_INCREMENT primary key,
    uuid       varchar(45)  not null,
    username  varchar(255)  not null unique,
    password   varchar(255) not null,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    email      varchar(255) not null unique,
    address    varchar(255) not null,
    phone      varchar(255) not null unique,
    role_id    bigint       not null,
    foreign key (role_id) references tbl_role (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;