declare
   c int;
begin
   select count(*) into c from user_tables where table_name = upper('influencer');
   if c = 1 then
      execute immediate 'drop table influencer';
   end if;
end;

CREATE table influencer (
   id NUMBER(11) NOT NULL,
   first_name VARCHAR2(45),
   last_name VARCHAR2(45),
   channel VARCHAR2(45)
);

CREATE SEQUENCE inf_seq START WITH 1;

ALTER TABLE influencer ADD (
  CONSTRAINT inf_pk PRIMARY KEY (id));