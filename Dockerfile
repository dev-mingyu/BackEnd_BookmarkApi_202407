##Staging1
#FROM maven:3.8.4-openjdk-17 AS build
#WORKDIR /app
#
#COPY pom.xml .
#COPY src ./src
#
#RUN mvn clean package -DskipTests
#
##Staging2
#FROM openjdk:17
#VOLUME /tmp
#
#COPY --from=build /app/target/springboot-nextjs-backend.jar /springboot-nextjs-backend.jar
#ENTRYPOINT ["java","-jar","/springboot-nextjs-backend.jar","--spring.profiles.active=prod"]

FROM openjdk:17
VOLUME /tmp
COPY target/springboot-nextjs-backend.jar springboot-nextjs-backend.jar
ENTRYPOINT ["java","-jar","/springboot-nextjs-backend.jar","--spring.profiles.active=prod"]
