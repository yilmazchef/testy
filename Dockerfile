FROM openjdk:11
MAINTAINER Yilmaz Mustafa <yilmaz@mail.be>
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} testy-pwa.jar
ENV SPRING_DATASOURCE_URL:jdbc:mysql://db_server:3306/testydb?autoReconnect=true&useSSL=false
ENTRYPOINT ["java","-jar","/testy-pwa.jar"]
