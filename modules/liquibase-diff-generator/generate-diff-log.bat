SET comparison_database=127.0.0.1:20100/crm_central
SET comparison_user=root
SET comparison_pass=root

SET ref_database=127.0.0.1:20100/crm_central_new
SET ref_user=root
SET ref_pass=root

liquibase/liquibase.bat --outputFile=diff-log.txt --driver=com.mysql.jdbc.Driver --classpath=mysql-connector-java-8.0.16.jar --url="jdbc:mysql://%comparison_database%" --username=%comparison_user% --password=%comparison_pass% diff --referenceUrl="jdbc:mysql://%ref_database%" --referenceUsername=%ref_user% --referencePassword=%ref_pass%