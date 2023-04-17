declare
   c int;
begin
   select count(*) into c from user_tables where table_name = upper('book');
   if c = 1 then
      execute immediate 'drop table book';
   end if;
end;

CREATE table book (
   id NUMBER(11) NOT NULL,
   title VARCHAR2(45)
);

CREATE SEQUENCE book_seq START WITH 1;

ALTER TABLE book ADD (
  CONSTRAINT book_pk PRIMARY KEY (id));
