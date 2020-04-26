create table tbl_todo (
    no int not null auto_increment primary key,
    content varchar(255),
    ref varchar(255),
    is_finish char(1),
    is_active char(1),
    reg_date timestamp,
    last_upd_date timestamp
);