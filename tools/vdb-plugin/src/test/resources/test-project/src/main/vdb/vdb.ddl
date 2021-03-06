CREATE DATABASE customer OPTIONS (ANNOTATION 'Customer VDB');
USE DATABASE customer;

CREATE SERVER mydb FOREIGN DATA WRAPPER h2;
CREATE VIRTUAL SCHEMA virt;
CREATE SCHEMA accounts SERVER mydb;

SET SCHEMA accounts;

IMPORT FOREIGN SCHEMA "MY""SCHEMA" FROM SERVER mydb INTO accounts OPTIONS("importer.useFullSchemaName" 'false');
IMPORT FOREIGN SCHEMA SAMPLE FROM SERVER mydb INTO accounts;

IMPORT DATABASE FOO VERSION '1';
IMPORT DATABASE BAR VERSION '1';
