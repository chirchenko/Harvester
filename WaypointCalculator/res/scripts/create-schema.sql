--drop table DB_INFO;
--drop table FIELDS;
--drop table MACHINERY;
--drop table POINTS;

CREATE TABLE DB_INFO
	(NAME		TEXT    NOT NULL, 
	VAL			TEXT	NOT NULL,
	TS			TIMESTAMP);
	
CREATE TABLE FIELDS
	(ID 		INT		PRIMARY KEY,
	NAME		TEXT    NOT NULL);
	
CREATE TABLE MACHINERY
	(ID 		INT		PRIMARY KEY,
	NAME        TEXT    NOT NULL, 
	WORK_WIDTH	INT     NOT NULL, 
	FUEL        REAL	NOT NULL);

CREATE TABLE POINTS
	(ID 		INT		PRIMARY KEY,
	FIELD_ID	INT		NOT NULL,
	SEQ			INT 	NOT NULL,
	LAT			REAL    NOT NULL, 
	LON 		REAL    NOT NULL);
	
	
insert into DB_INFO VALUES("DB schema name", "1.0.7", datetime('now'));
insert into DB_INFO VALUES("FIELDS_SEQUENCE", "3", datetime('now'));
insert into DB_INFO VALUES("MACHINERY_SEQUENCE", "1", datetime('now'));
insert into DB_INFO VALUES("POINTS_SEQUENCE", "17", datetime('now'));

--insert into MACHINERY VALUES(1, "John Deere T660", 5, 0.4);
--insert into MACHINERY VALUES(2, "Claas Lexion 480", 4.5, 0.45);
--insert into MACHINERY VALUES(3, "Єнісей 1200", 3.5, 0.5);
--insert into MACHINERY VALUES(4, "Славутич КЗС 9", 5.2, 0.7);
--insert into MACHINERY VALUES(5, "Єнісей 950-1", 5.1, 0.6);

insert into POINTS VALUES(1, 1, 1, 49.856666, 30.122131);
insert into POINTS VALUES(2, 1, 2, 49.855485, 30.121552);
insert into POINTS VALUES(3, 1, 3, 49.856100, 30.117736);
insert into POINTS VALUES(4, 1, 4, 49.856143, 30.116169);
insert into POINTS VALUES(5, 1, 5, 49.856625, 30.116229);
insert into POINTS VALUES(6, 1, 6, 49.856646, 30.115896);
insert into POINTS VALUES(7, 1, 7, 49.857538, 30.115832);
insert into POINTS VALUES(8, 2, 1, 49.851887, 30.120772);
insert into POINTS VALUES(9, 2, 2, 49.848890, 30.137644);
insert into POINTS VALUES(10, 2, 3, 49.854177, 30.139869);
insert into POINTS VALUES(11, 2, 4, 49.856543, 30.122645);
insert into POINTS VALUES(12, 3, 1, 50.082687, 30.039135);
insert into POINTS VALUES(13, 3, 2, 50.080071, 30.040690);
insert into POINTS VALUES(14, 3, 3, 50.076592, 30.027692);
insert into POINTS VALUES(15, 3, 4, 50.078366, 30.024730);
insert into POINTS VALUES(16, 3, 5, 50.080584, 30.032966);
insert into POINTS VALUES(17, 3, 6, 50.081290, 30.033961);

INSERT INTO FIELDS VALUES(1, "Терезино №603");
INSERT INTO FIELDS VALUES(2, "Терезино №598");
INSERT INTO FIELDS VALUES(3, "Велика Снітинка №153");

