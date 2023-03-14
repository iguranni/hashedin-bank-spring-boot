FROM amazoncorretto:17-alpine-jdk
MAINTAINER hashedin-bank.com
COPY target/hashedin-bank-0.0.1-SNAPSHOT.jar hashedin-bank-0.0.1-SNAPSHOT.jar
EXPOSE 1010
ENTRYPOINT ["java","-jar","/hashedin-bank-0.0.1-SNAPSHOT.jar"]