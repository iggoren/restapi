FROM openjdk:19
ADD /target/RestApi-0.0.1-SNAPSHOT.jar backend.jar
ENTRYPOINT ["java", "-jar","backend.jar"]
#FROM postgres:13
#
#ENV POSTGRES_USER root
#ENV POSTGRES_PASSWORD 123
#ENV POSTGRES_DB mdb
#EXPOSE 5432

#COPY init.sql /docker-entrypoint-initdb.d/
