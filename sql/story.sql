declare
   c int;
begin
   select count(*) into c from user_tables where table_name = upper('story');
   if c = 1 then
      execute immediate 'drop table story';
   end if;
end;

CREATE table story (
   id NUMBER(11) NOT NULL,
   first_name VARCHAR2(45),
   last_name VARCHAR2(45),
   channel VARCHAR2(45)
);

CREATE SEQUENCE sto_seq START WITH 1;

ALTER TABLE story ADD (
  CONSTRAINT sto_pk PRIMARY KEY (id));

CREATE OR REPLACE TRIGGER sto_trg
BEFORE INSERT ON story
FOR EACH ROW

BEGIN
  SELECT sto_pk.NEXTVAL
  INTO   :new.id
  FROM   dual;
END;
