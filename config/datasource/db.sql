create schema `default`;
use `default`;
create table detail
(
    id   int unsigned auto_increment comment '主键id'
        primary key,
    name varchar(20) default '' not null comment '详情'
)
    comment '详情表';
create table product
(
    id   int unsigned auto_increment comment '主键id'
        primary key,
    name varchar(20) default '' not null comment '详情'
)
    comment '产品表';
create table user
(
    id   int unsigned auto_increment comment '主键id'
        primary key,
    name varchar(20) default '' not null comment '详情'
)
    comment '用户表';

INSERT INTO `default`.detail (name) VALUES ('default-detail');
INSERT INTO `default`.product (name) VALUES ('default-product');
INSERT INTO `default`.user (name) VALUES ('default-user');

create schema `gray`;
use `gray`;
create table detail
(
    id   int unsigned auto_increment comment '主键id'
        primary key,
    name varchar(20) default '' not null comment '详情'
)
    comment '详情表';
create table product
(
    id   int unsigned auto_increment comment '主键id'
        primary key,
    name varchar(20) default '' not null comment '详情'
)
    comment '产品表';
create table user
(
    id   int unsigned auto_increment comment '主键id'
        primary key,
    name varchar(20) default '' not null comment '详情'
)
    comment '用户表';

INSERT INTO `gray`.detail (name) VALUES ('gray-detail');
INSERT INTO `gray`.product (name) VALUES ('gray-product');
INSERT INTO `gray`.user (name) VALUES ('gray-user');




