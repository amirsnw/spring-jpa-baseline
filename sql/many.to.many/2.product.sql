declare
    c int;
begin
    select count(*) into c from user_tables where table_name = upper('product');
    if c = 1 then
      execute immediate 'drop table product';
    end if;
end;

DROP SEQUENCE prod_seq

CREATE SEQUENCE prod_seq START WITH 1;

CREATE table product (
    id          NUMBER(11) NOT NULL,
    name        VARCHAR2(45),
    description        VARCHAR2(45),
    price_per_unit     VARCHAR2(11),
    supplier_id NUMBER(11),
    CONSTRAINT production_pk PRIMARY KEY (id),
    CONSTRAINT fk_supplier FOREIGN KEY (supplier_id) REFERENCES supplier (id)
);
