# README
Make adjustment to the application.properties to read your current MySQL configuration

spring.datasource.url=jdbc:mysql://localhost/palladium?createDatabaseIfNotExist=true
spring.datasource.username=######################
spring.datasource.password=######################
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.servlet.multipart.max-file-size=-1
spring.servlet.multipart.max-request-size=-1
app.dbpath = C:\\Palladium\\Databases\\
app.filepath= C:\\Palladium\\Databases\\ExcelData\\
app.mysqlpath=C:\\Program Files\\MySQL\\MySQL Server 5.6\\bin\\mysql\\
