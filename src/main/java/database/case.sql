drop table if exists file;
create table file
(
    id int primary key auto_increment,
    name varchar(30) not null,
    user_id int not null,
    foreign key(user_id) references user(id)
);
insert into file(name, user_id) values("file1", 1);
insert into file(name, user_id) values("file2", 1);
select * from file;

drop table if exists user;
create table user
(
id int primary key auto_increment,
name varchar(20) not null,
password varchar(40) not null
);
insert into user(name, password) value("test", "123");
select * from user;
