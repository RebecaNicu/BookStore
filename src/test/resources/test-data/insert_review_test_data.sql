insert into tbl_category (uuid, name)
values ('29e45e7d-3e34-43df-9366-91c66a8cc9ae', 'comedy');
insert into tbl_book (isbn, title, author, details, price, category_id)
values ('33e45e7d-3e34-43df-9366-91c66a8cc9ae', 'Ion', 'Rebreanu', 'details',37.2, '1');
insert into tbl_role(name)
values ('user');
insert into tbl_user(uuid, username, password, first_name, last_name, email, address, phone, role_id)
values ('89e45e7d-3e34-43df-9366-91c66a8cc9ff', 'vlad', 'pass123', 'vlad', 'popescu', 'vlad@gmail.com', 'Craiova',
        '0725984687', 1);

insert into tbl_book_review(uuid, title, body, star, user_id, book_id)
values('89e45e7d-3e34-43df-9366-91c66a8cc9mm', 'excellent', 'magic', 5, '1', '1');
insert into tbl_book_review(uuid, title, body, star, user_id, book_id)
values('66e45e7d-3e34-43df-9366-91c66a8cc9mm', 'bad', 'i don t like it', 2, '1', '1');