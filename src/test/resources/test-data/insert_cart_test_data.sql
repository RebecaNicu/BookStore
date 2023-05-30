insert into tbl_role(name)
values ('admin');
insert into tbl_user(uuid, username, password, first_name, last_name, email, address, phone, role_id)
values ('89e45e7d-3e34-43df-9366-91c66a8cc9ff', 'vlad', 'pass123', 'vlad', 'popescu', 'vlad@gmail.com', 'Craiova',
        '0725984687', 1);
insert into tbl_category (uuid, name)
values ('29e45e7d-3e34-43df-9366-91c66a8cc9ae', 'comedy');

insert into tbl_book (isbn, title, author, details, price, category_id)
values ('33e45e7d-3e34-43df-9366-91c66a8cc9ae', 'Ion', 'Rebreanu', 'details', 37.2, '1');
insert into tbl_book (isbn, title, author, details, price, category_id)
values ('99e45e7d-3e34-43df-9366-91c66a8cc9ae', 'Poezii', 'Eminescu', 'details', 15, '1');

INSERT INTO tbl_cart(uuid, user_id)
VALUES ('11e45e7d-3e34-43df-9366-91c66a8cc9ff', '1');
