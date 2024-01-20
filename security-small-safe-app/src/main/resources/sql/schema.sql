create table if not exists user
(
    id        bigint      not null auto_increment,
    username  varchar(45) not null,
    password  text        not null,
    algorithm varchar(45) not null,
    primary key (id)
);

create table if not exists authority
(
    id      bigint      not null auto_increment,
    name    varchar(45) not null,
    user_id bigint      not null,
    primary key (id)
);

create table if not exists product
(
    id       bigint      not null auto_increment,
    name     varchar(45) not null,
    price    double not null,
    currency varchar(45) not null,
    primary key (id)
);