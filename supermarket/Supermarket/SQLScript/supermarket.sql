drop database supermarket;
CREATE DATABASE supermarket;
USE supermarket;

CREATE TABLE aquatic(
in_no varchar(20) primary key,
id varchar(3),
name varchar(10),
inventory int,
p_date varchar(8),
exp_day int
);

CREATE TABLE meat(
in_no varchar(20) primary key,
id varchar(3),
name varchar(10),
inventory int,
p_date varchar(8),
exp_day int
);
CREATE TABLE fruit(
in_no varchar(20) primary key,
id varchar(3),
name varchar(10),
inventory int,
p_date varchar(8),
exp_day int
);
CREATE TABLE dry(
in_no varchar(20) primary key,
id varchar(3),
name varchar(10),
inventory int,
p_date varchar(8),
exp_day int
);
CREATE TABLE cloth(
in_no varchar(20) primary key,
id varchar(3),
name varchar(10),
inventory int,
p_date varchar(8)
);
CREATE TABLE department(
in_no varchar(20) primary key,
id varchar(3),
name varchar(10),
inventory int,
p_date varchar(8)
);
CREATE TABLE electric(
in_no varchar(20) primary key,
id varchar(3),
name varchar(10),
inventory int,
p_date varchar(8)
);

CREATE TABLE safty(
id varchar(3) primary key,
safty_inventory int 
);

CREATE TABLE outstore(
out_no varchar(20) primary key,
id varchar(3),
name varchar(10),
amount int,
out_date varchar(8)
);

INSERT INTO aquatic values('120150611A01001','A01','fish',5,'20150601',10);
INSERT INTO aquatic values('120150611A02002','A02','shell',5,'20150601',15);
INSERT INTO meat values('120150611M01003','M01','beff',10,'20150601',10);
INSERT INTO meat values('120150611M02004','M02','pork',10,'20150601',6);
INSERT INTO meat values('120150611M03005','M03','mutton',15,'20150602',8);
INSERT INTO fruit values('120150611F01006','F01','apple',30,'20150603',15);
INSERT INTO fruit values('120150611F02007','F02','banana',30,'20150603',5);
INSERT INTO fruit values('120150611F03008','F03','peach',30,'20150603',8);
INSERT INTO dry values('120150611D01009','D01','peanut',50,'20150601',25);
INSERT INTO dry values('120150611D02010','D02','bean',30,'20150603',30);
INSERT INTO cloth values('120150611C01011','C01','nike',10,'20150510');
INSERT INTO cloth values('120150611C02012','C02','addidas',18,'20150510');
INSERT INTO cloth values('120150611C03013','C03','anta',20,'20150310');
INSERT INTO cloth values('120150611C04014','C04','lining',35,'20150410');
INSERT INTO department values('120150611T01015','T01','soap',12,'20150510');
INSERT INTO department values('120150611T02016','T02','shampoo',25,'20150510');
INSERT INTO electric values('120150611E01017','E01','light',5,'20150510');
INSERT INTO electric values('120150611E02018','E02','computer',6,'20150513');

INSERT INTO safty values('A01',5);
INSERT INTO safty values('A02',5);
INSERT INTO safty values('M01',5);
INSERT INTO safty values('M02',5);
INSERT INTO safty values('M03',5);
INSERT INTO safty values('F01',5);
INSERT INTO safty values('F02',5);
INSERT INTO safty values('F03',5);
INSERT INTO safty values('D01',5);
INSERT INTO safty values('D02',5);
INSERT INTO safty values('C01',5);
INSERT INTO safty values('C02',5);
INSERT INTO safty values('C03',5);
INSERT INTO safty values('C04',5);
INSERT INTO safty values('T01',5);
INSERT INTO safty values('T02',5);
INSERT INTO safty values('E01',5);
INSERT INTO safty values('E02',5);


