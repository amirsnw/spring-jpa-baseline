declare
    c int;
begin
    select count(*) into c from user_tables where table_name = upper('supplier');
    if c = 1 then
      execute immediate 'drop table supplier';
    end if;
end;

DROP SEQUENCE sup_seq

CREATE SEQUENCE sup_seq START WITH 1;

CREATE table supplier (
      id NUMBER(11) NOT NULL,
      name VARCHAR2(45),
      address VARCHAR2(11),
      country VARCHAR2(11),
      CONSTRAINT supplier_pk PRIMARY KEY (id)
);
