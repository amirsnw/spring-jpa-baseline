declare
   c int;
begin
   select count(*) into c from user_tables where table_name = upper('follower');
   if c = 1 then
      execute immediate 'drop table follower';
   end if;
end;

CREATE table follower (
   id NUMBER(11) NOT NULL,
   username VARCHAR2(45),
   influencer_id VARCHAR2(11)
);

CREATE SEQUENCE flw_seq START WITH 1;

ALTER TABLE follower ADD (
  CONSTRAINT flw_pk PRIMARY KEY (id));

CREATE OR REPLACE TRIGGER flw_trg
BEFORE INSERT ON follower
FOR EACH ROW

BEGIN
  SELECT flw_seq.NEXTVAL
  INTO   :new.id
  FROM   dual;
END;
