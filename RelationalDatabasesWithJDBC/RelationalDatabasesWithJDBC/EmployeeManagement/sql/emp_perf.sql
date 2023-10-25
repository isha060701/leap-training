DROP TABLE emp_perf;

--------------------------------------------------------
--  DDL for Table EMP_PERF
--------------------------------------------------------

  CREATE TABLE "EMP_PERF" 
   (	"EMPNO" NUMBER(4,0), 
	"PERF_REV_CODE" NUMBER(1,0), 
	"PERF_REV_NAME" VARCHAR2(7)
   );
REM INSERTING into EMP_PERF
SET DEFINE OFF;
Insert into EMP_PERF (EMPNO,PERF_REV_CODE,PERF_REV_NAME) values (7369,5,'ABOVE');
Insert into EMP_PERF (EMPNO,PERF_REV_CODE,PERF_REV_NAME) values (7499,1,'BELOW');
Insert into EMP_PERF (EMPNO,PERF_REV_CODE,PERF_REV_NAME) values (7521,3,'AVERAGE');
Insert into EMP_PERF (EMPNO,PERF_REV_CODE,PERF_REV_NAME) values (7566,3,'AVERAGE');
Insert into EMP_PERF (EMPNO,PERF_REV_CODE,PERF_REV_NAME) values (7654,3,'AVERAGE');
Insert into EMP_PERF (EMPNO,PERF_REV_CODE,PERF_REV_NAME) values (7698,3,'AVERAGE');

COMMIT;
--------------------------------------------------------
--  DDL for Index SYS_C007951
--------------------------------------------------------

  CREATE UNIQUE INDEX "SYS_C007951" ON "EMP_PERF" ("EMPNO");
--------------------------------------------------------
--  Constraints for Table EMP_PERF
--------------------------------------------------------

  ALTER TABLE "EMP_PERF" MODIFY ("EMPNO" NOT NULL ENABLE);
  ALTER TABLE "EMP_PERF" MODIFY ("PERF_REV_CODE" NOT NULL ENABLE);
  ALTER TABLE "EMP_PERF" MODIFY ("PERF_REV_NAME" NOT NULL ENABLE);
  ALTER TABLE "EMP_PERF" ADD PRIMARY KEY ("EMPNO") ENABLE;
--  USING INDEX  ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table EMP_PERF
--------------------------------------------------------

  ALTER TABLE "EMP_PERF" ADD FOREIGN KEY ("EMPNO")
	  REFERENCES "EMP" ("EMPNO") ON DELETE CASCADE ENABLE;
