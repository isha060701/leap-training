--DROP TABLE b_hours;
--DROP TABLE b_phoneContracts;
--DROP TABLE b_rates;


CREATE TABLE b_phoneContracts (
    pcid   NUMBER(6, 0),
    pcname VARCHAR2(200) NOT NULL,
    CONSTRAINT b_phoneContracts_pk PRIMARY KEY(pcid)
);

CREATE TABLE b_rates (
    ratesid    NUMBER(6, 0),
    ratesname  VARCHAR2(200) NOT NULL,
    ratesprice NUMBER(8, 2)  NOT NULL,
    CONSTRAINT b_rates_pk PRIMARY KEY(ratesid)
);

CREATE TABLE b_hours (
    hoursid       NUMBER(6, 0),
    pcid       NUMBER(6, 0),
    ratesid            NUMBER(6, 0),
    hours_quantity NUMBER(4, 0) NOT NULL,
    CONSTRAINT b_hours_pk PRIMARY KEY(hoursid)
);

ALTER TABLE b_hours
    ADD CONSTRAINT pc_hours_fk
    FOREIGN KEY (pcid)
    REFERENCES b_phoneContracts(pcid);

ALTER TABLE b_hours
    ADD CONSTRAINT rates_hours_fk
    FOREIGN KEY (ratesid)
    REFERENCES b_rates(ratesid);

INSERT INTO b_phoneContracts (pcid, pcname) VALUES (1, 'Cheap USA');
INSERT INTO b_phoneContracts (pcid, pcname) VALUES (2, 'North America');
INSERT INTO b_phoneContracts (pcid, pcname) VALUES (3, 'Global');
INSERT INTO b_phoneContracts (pcid, pcname) VALUES (4, 'Village');

INSERT INTO b_rates (ratesid, ratesname, ratesprice) VALUES (100, 'weekend', .42);
INSERT INTO b_rates (ratesid, ratesname, ratesprice) VALUES (101, 'workhours', 4.2);
INSERT INTO b_rates (ratesid, ratesname, ratesprice) VALUES (102, 'night', 0.042);
INSERT INTO b_rates (ratesid, ratesname, ratesprice) VALUES (103, 'flat', 4.42);


INSERT INTO b_hours (hoursid, pcid, ratesid, hours_quantity) VALUES (42, 1, 100, 2);
INSERT INTO b_hours (hoursid, pcid, ratesid, hours_quantity) VALUES (43, 2, 100, 1);
INSERT INTO b_hours (hoursid, pcid, ratesid, hours_quantity) VALUES (44, 2, 103, 42);
INSERT INTO b_hours (hoursid, pcid, ratesid, hours_quantity) VALUES (45, 3, 101, 1);
INSERT INTO b_hours (hoursid, pcid, ratesid, hours_quantity) VALUES (46, 3, 103, 1);
INSERT INTO b_hours (hoursid, pcid, ratesid, hours_quantity) VALUES (49, 1, 102, 1);

COMMIT;
