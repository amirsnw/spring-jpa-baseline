declare
   c int;
begin
   select count(*) into c from user_tables where table_name = upper('manuscript');
   if c = 1 then
      execute immediate 'drop table manuscript';
   end if;
end;

DROP SEQUENCE manu_seq

CREATE table manuscript (
   man_id NUMBER(11) NOT NULL,
   water_mark VARCHAR2(45)
);

CREATE SEQUENCE manu_seq START WITH 1;

ALTER TABLE manuscript ADD (
  CONSTRAINT manu_pk PRIMARY KEY (man_id));
