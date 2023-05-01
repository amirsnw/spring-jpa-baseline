declare
   c int;
begin
   select count(*) into c from user_tables where table_name = upper('student');
   if c = 1 then
      execute immediate 'drop table student';
   end if;
end;

DROP SEQUENCE stu_seq

CREATE SEQUENCE stu_seq START WITH 1;

CREATE table student (
   id NUMBER(11) NOT NULL,
   full_name VARCHAR2(45),
   age NUMBER(3),
   teacher_id NUMBER(11),
   CONSTRAINT stu_pk PRIMARY KEY (id),
   CONSTRAINT fk_teacher FOREIGN KEY (teacher_id) REFERENCES TEACHER (id)
);
