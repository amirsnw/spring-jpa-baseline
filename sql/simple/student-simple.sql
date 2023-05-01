declare
   c int;
begin
   select count(*) into c from user_tables where table_name = upper('student_simple');
   if c = 1 then
      execute immediate 'drop table student_simple';
   end if;
end;

DROP SEQUENCE stu_sim_seq

CREATE SEQUENCE stu_sim_seq START WITH 1;

CREATE table student_simple (
   id NUMBER(11) NOT NULL,
   first_name VARCHAR2(45),
   last_name VARCHAR2(45),
   email VARCHAR2(45),
   CONSTRAINT stu_sim_pk PRIMARY KEY (id)
);

DROP TRIGGER stu_sim_trg

CREATE OR REPLACE TRIGGER stu_sim_trg
BEFORE INSERT ON student_simple
FOR EACH ROW

BEGIN
  SELECT stu_sim_seq.NEXTVAL
  INTO   :new.id
  FROM   dual;
END;
