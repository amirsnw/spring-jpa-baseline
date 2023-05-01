declare
    c int;
begin
    select count(*) into c from user_tables where table_name = upper('supplier_product');
    if c = 1 then
      execute immediate 'drop table supplier_product';
    end if;
end;

CREATE TABLE supplier_product (
      supplier_id NUMBER(11) NOT NULL,
      product_id  NUMBER(11) NOT NULL,
      CONSTRAINT prod_sup_pk PRIMARY KEY (supplier_id, product_id),
      CONSTRAINT fk_sup FOREIGN KEY (supplier_id) REFERENCES supplier (id),
      CONSTRAINT fk_prod FOREIGN KEY (product_id) REFERENCES product (id)
);