# MyCareer Web API

This project hold the my career web api. This web service is used by the [MyCareer Web Application](http://slnxlrgceglab01.edin.uk.sopra/mycareer/mycareer-rest-app).

## Prerequisites 

1. [JDK 1.8+](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
2. [Gradle 3.1+](https://gradle.org/gradle-download/)

## Getting Started Locally

1. Clone the repository using git clone 
2. `cd mycareer-web-api`
3. `gradle bootRun`
4. The web api will now be available at [localhost:8080](http://localhost:8080/)

### Notes:
You can build this project using `gradle build`
This will build an executable file in the `/build/libs` directory.

Use `gradle eclipse` to sync your project if you are seeing error with imports.

Only UAT, Dev and Live servers have the certificate to use ews services. 