use salesmanagementdata;
CREATE TABLE IF Not Exists company(cmpid int NOT NULL AUTO_INCREMENT
, cmpname varchar(255) NOT NULL, cmpaddress varchar(255), cmpstate varchar(255), cmpcountry varchar(255),
cmpphoneno varchar(255), cmpgstin varchar(255), cmpstatecode varchar(255) ,cmpsetting varchar(255), 
cmptc text ,cmplogo longblob, PRIMARY KEY (cmpid));
CREATE TABLE IF NOT EXISTS productcategory(catid int NOT NULL AUTO_INCREMENT, catname varchar(255),
PRIMARY KEY(catid));
CREATE TABLE IF NOT EXISTS product(prid int NOT NULL AUTO_INCREMENT, productname varchar(255), catid int
, gstrate decimal(10,2), cess decimal(10,2), rate decimal(10,2), unit varchar(255), hsn varchar(255), barcode varchar(255),
opbl decimal(10,2), clbl decimal(10,2) , PRIMARY KEY(prid), FOREIGN KEY(catid) REFERENCES productcategory(catid));
CREATE TABLE IF NOT EXISTS customer(custid int not null auto_increment, custname varchar(255), custphoneno varchar(255)
, custaddress varchar(255), custstate varchar(255), custgstin varchar(255), opbl decimal(10,2), clbl decimal(10,2),
PRIMARY KEY(custid));
CREATE TABLE IF NOT EXISTS transactions(traid int NOT NULL AUTO_INCREMENT, classid int, billno int , billdate date,
 billtime time, custid int, custname varchar(255), billingaddress varchar(255), custgstin varchar(255), 
 phoneno varchar(255), total decimal(10,2), offamount decimal(10,2), gtotal decimal(10,2), igst decimal(10,2), cgst decimal(10,2), sgst decimal(10,2),
 cess decimal(10,2), gtwd text, quantity decimal(10,2), rc decimal(10,2), apptax decimal(10,2), pos decimal(10,2), invtype varchar(255), 
 docnature varchar(255), ectype varchar(255), ecgstin varchar(255), logname varchar(255), pmt varchar(255),
 PRIMARY KEY(traid), foreign key(custid) references customer(custid));
 CREATE TABLE IF NOT EXISTS taxdetail(taxdtlid int, traid int, gstname varchar(255), onamt decimal(10,2), gstrate decimal(10,2),
 taxamt decimal(10,2), PRIMARY KEY(taxdtlid), foreign key(traid) references transactions(traid));
 CREATE TABLE IF NOT EXISTS transactiondetail(dtlid int not null auto_increment, traid int, barcode varchar(255), 
 prname varchar(255), prid int, hsn varchar(255),quantity decimal, unit varchar(255), rate decimal(10,2), gstrate decimal(10,2),
 cess decimal(10,2), discount decimal(10,2), total decimal(10,2), igst decimal(10,2), cgst decimal(10,2), sgst decimal(10,2), cessamt decimal(10,2), gtotal decimal(10,2)
 ,PRIMARY KEY (dtlid), foreign key(traid) references transactions(traid));
 CREATE TABLE IF NOT EXISTS users(usid int not null auto_increment, username varchar(255), password_hash VARCHAR(255) NOT NULL, status varchar(255),
 role varchar(255), logname varchar(255), primary key(usid));