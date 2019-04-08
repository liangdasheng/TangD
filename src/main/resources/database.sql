create database if not exists tangshi;
use tangshi;
create table if not exists poetry_info(
  title varchar(1024) not null,
  dynasty varchar(64) not null,
  author varchar(32) not null,
  content varchar(1024) not null
);



