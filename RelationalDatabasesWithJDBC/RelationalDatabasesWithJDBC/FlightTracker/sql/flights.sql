--------------------------------------------------------
--  File created - Monday-August-23-2021   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table FLIGHT
--------------------------------------------------------
DROP TABLE "FLIGHT";

  CREATE TABLE "FLIGHT" 
   (	"ID" NUMBER, 
	"ORIGIN" VARCHAR2(20), 
	"DESTINATION" VARCHAR2(20), 
	"DEPARTURE" DATE
   );

--------------------------------------------------------
--  Constraints for Table FLIGHT
--------------------------------------------------------

  ALTER TABLE "FLIGHT" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "FLIGHT" ADD CONSTRAINT "FLIGHT_PK" PRIMARY KEY ("ID");

Insert into FLIGHT (ID,ORIGIN,DESTINATION,DEPARTURE) values (1,'Chicago','New York',to_date('28-AUG-21, 09:15','DD-MON-YY, HH:MI'));
Insert into FLIGHT (ID,ORIGIN,DESTINATION,DEPARTURE) values (2,'Chicago','New York',to_date('29-AUG-21, 10:45','DD-MON-YY, HH:MI'));

commit;
