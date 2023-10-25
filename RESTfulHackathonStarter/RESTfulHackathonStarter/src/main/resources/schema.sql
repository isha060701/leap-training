-- Schema definition for the departments and employees

-- drop table
DROP TABLE presidents;

-- create new table
CREATE TABLE presidents 
   ("PRESIDENTID" integer, 
	"FIRSTNAME" VARCHAR(45), 
	"LASTNAME" VARCHAR(45), 
	"STARTYEAR" integer, 
	"ENDYEAR" integer, 
	"IMAGEPATH" VARCHAR(80), 
	"BIO" VARCHAR(700)
   );