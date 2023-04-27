declare
   c int;
begin
   select count(*) into c from user_tables where table_name = upper('teacher');
   if c = 1 then
      execute immediate 'drop table teacher';
   end if;
end;

DROP SEQUENCE tch_seq

CREATE SEQUENCE tch_seq START WITH 1;

CREATE table teacher (
   id NUMBER(11) NOT NULL,
   full_name VARCHAR2(45),
   CONSTRAINT teacher_pk PRIMARY KEY (id)
);

ALTER TABLE teacher ADD (
  CONSTRAINT tch_pk PRIMARY KEY (id));

DROP TRIGGER tch_trg

CREATE OR REPLACE TRIGGER tch_trg
BEFORE INSERT ON teacher
FOR EACH ROW

BEGIN
  SELECT tch_seq.NEXTVAL
  INTO   :new.id
  FROM   dual;
END;
