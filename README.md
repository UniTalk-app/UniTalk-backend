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
- Install PostgreSQL (See [developer info](#developer-info))
- Open the projects directory and type `./mvnw spring-boot:run ` in order to install dependencies and run application

To use tests you will need to download [Maven](https://maven.apache.org/download.cgi) and run `mvn test`

## Using hot swap
App will be automagically hot swapped if you build project (eg Hammer icon in IntelliJ). However if you use IntelliJ you can skip the boring human action to click the button, just follow [this 3 easy steps!](https://dzone.com/articles/spring-boot-application-live-reload-hot-swap-with)

# Developer info
To start further development of the project one must:

- download, install the latest version of the JDK <br>
  and add needed system path variables<br>
  https://www.oracle.com/pl/java/technologies/javase-downloads.html
- download and install IDE (suggested IntelliJ IDEA)
- download and install PostgreSQL
- DB connection info: src/main/resources/<b>application.properties</b>
<br>(Or simply use docker for PostgreSQL):<br>
`docker run -p 5432:5432 --name postgres -e POSTGRES_PASSWORD=passwd -d --rm postgres`

# Comments
- DB is in 'create-drop' mode, so that means every time the server
starts, tables are dropped and recreated - empty
