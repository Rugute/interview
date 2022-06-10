CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_mergeclients`()
BEGIN
declare scName varchar(250);
    declare q varchar(2000);
    
    DROP TABLE IF EXISTS rt_mergedclients;
    create table rt_mergedclients (
		 `clientsname` varchar(45) DEFAULT NULL,
		  `branchcode` varchar(45) DEFAULT NULL,
		  `policynumber` varchar(45) DEFAULT NULL,
		  `policytype` varchar(45) DEFAULT NULL,
		  `address` varchar(45) DEFAULT NULL,
		  `idno` varchar(45) DEFAULT NULL
    );
     DROP TABLE IF EXISTS MySchemaNamess;
    create table MySchemaNamess (
        schemaName varchar(250)
    );
    insert into MySchemaNamess
    SELECT CONCAT(SCHEMA_NAME) AS DBNames
	FROM `information_schema`.`SCHEMATA`
	WHERE SCHEMA_NAME LIKE 'insurance%';
	label1:
	LOOP
     set scName = (select schemaName from MySchemaNamess limit 1);
	 set @q = concat('insert into rt_mergedclients(clientsname,branchcode,policynumber,policytype,address,idno) 
		SELECT 
		  clientsname,
		  branchcode,
		  policynumber,
		  policytype,
		  address,
		  idno
		From ', scname,'.clients pp
		');
        PREPARE stmt1 FROM @q;
        EXECUTE stmt1;
        DEALLOCATE PREPARE stmt1;

        delete from MySchemaNamess where schemaName = scName;
        IF ((select count(*) from MySchemaNamess) > 0) THEN
            ITERATE label1;
        END IF;
        LEAVE label1;

    END LOOP label1;

    SELECT * FROM rt_mergedclients;
END