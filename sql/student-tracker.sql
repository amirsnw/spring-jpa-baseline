declare
   c int;
begin
   select count(*) into c from user_tables where table_name = upper('student');
   if c = 1 then
      execute immediate 'drop table student';
   end if;
end;


CREATE SEQUENCE stu_seq START WITH 1;

CREATE table student (
   id NUMBER(11) NOT NULL,
   first_name VARCHAR2(45),
   last_name VARCHAR2(45),
   email VARCHAR2(45)
);

ALTER TABLE student ADD (
  CONSTRAINT stu_pk PRIMARY KEY (id));
  
ALTER TABLE departments ADD (
  CONSTRAINT dept_pk PRIMARY KEY (ID));

CREATE OR REPLACE TRIGGER stu_trg 
BEFORE INSERT ON student 
FOR EACH ROW

BEGIN
  SELECT stu_pk.NEXTVAL
  INTO   :new.id
  FROM   dual;
END;
