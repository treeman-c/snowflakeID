create database if not exists demo;
use demo;
drop table if exists snowflake;
create table snowflake(
    id bigint not null comment '雪花id',
    time_bit bigint not null comment '41位时间戳',
    work_bit bigint not null comment '5位机器码',
    data_bit bigint not null comment '5位数据码',
    sequence bigint not null comment '12位自增序列码'
    primary key (id)
)character set utf8 comment '雪花表';