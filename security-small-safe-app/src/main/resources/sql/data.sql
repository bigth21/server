insert ignore into user (id, username, password, algorithm)
values (1, 'john', '$2a$10$nMT6vOn.f1cTmbfODGZY2OeF9KRtD3EL2YTkPAQ9LsRM6pcDj5W6S', 'BCRYPT'); # password: 12345

insert ignore into authority (id, name, user_id)
values (1, 'READ', '1');
insert ignore into authority (id, name, user_id)
values (2, 'WRITE', '1');

insert ignore into product (id, name, price, currency)
values (1, 'Chocolate', 10, 'USD');