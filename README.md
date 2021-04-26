# UniTalk-backend
[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-orange.svg)](https://sonarcloud.io/dashboard?id=UniTalk-app_UniTalk-backend)
<br><br>
[![CI](https://github.com/UniTalk-app/UniTalk-backend/actions/workflows/main.yml/badge.svg)](https://github.com/UniTalk-app/UniTalk-backend/actions/workflows/main.yml)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=UniTalk-app_UniTalk-backend&metric=bugs)](https://sonarcloud.io/dashboard?id=UniTalk-app_UniTalk-backend)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=UniTalk-app_UniTalk-backend&metric=code_smells)](https://sonarcloud.io/dashboard?id=UniTalk-app_UniTalk-backend)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=UniTalk-app_UniTalk-backend&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=UniTalk-app_UniTalk-backend)
<br>
Created in Java/Spring with start.spring.io <br>
Database server: PostgreSQL

## Details:
- Maven project
- Spring boot v2.4.4
- Java v11
- included dependencies: <br>
  Spring Web, JDBC API, PostgreSQL Driver
  
Check out [our wiki](https://github.com/UniTalk-app/UniTalk-backend/wiki) for documentation!
  
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
