drop table if exists hadoop;
create database hadoop;
use hadoop;
create table file
(
    id int primary key auto_increment,
    name varchar(30) not null
);
insert into file(name) values("file1");
insert into file(name) values("file2");
select * from file;


drop table if exists user;
create table user
(
name varchar(20) primary key,
password varchar(40) not null
);
insert into user(name, password) value("latin", "123456");
select * from user;
