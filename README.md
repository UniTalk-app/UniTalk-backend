# UniTalk-backend

Created in Java/Spring with start.spring.io <br>
Database server: PostgreSQL

## Details:
- Maven project
- Spring boot v2.4.4
- Java v11
- included dependencies: <br>
  Spring Web, JDBC API, PostgreSQL Driver
  
# Setting up the project
- Git clone repository
- Open the projects directory and type `./mvnw spring-boot:run ` in order to install dependencies and run application

To use tests you will need to download [Maven](https://maven.apache.org/download.cgi) and run `mvn test`

# Developer info
To start further development of the project one must:

- download, install the latest version of the JDK <br>
  and add needed system path variables<br>
  https://www.oracle.com/pl/java/technologies/javase-downloads.html
- download and install IDE (suggested IntelliJ IDEA)
- download and install PostgreSQL
- DB connection info: src/main/resources/<b>application.properties</b>

# Comments
- DB is in 'create-drop' mode, so that means every time the server
starts, tables are dropped and recreated - empty