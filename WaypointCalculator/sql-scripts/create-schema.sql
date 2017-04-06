CREATE TABLE DB_INFO
	(NAME           TEXT    NOT NULL, 
	VAL				TEXT,
	TS				TIMESTAMP);
	
CREATE TABLE FIELDS
	(ID INT		PRIMARY KEY,
	NAME           TEXT    NOT NULL, 
	AGE            INT     NOT NULL);
	
CREATE TABLE MACHIENERY
	(ID INT	PRIMARY KEY,
	NAME           TEXT    NOT NULL, 
	AGE            INT     NOT NULL, 
	ADDRESS        CHAR(50), 
	SALARY         REAL);
	
insert into DB_INFO VALUES("DB schema name", "1.0.5", datetime('now'));
insert into DB_INFO VALUES("FIELDS_SEQUENCE", "1", datetime('now'));
insert into DB_INFO VALUES("MACHINERY_SEQUENCE", "1", datetime('now'));