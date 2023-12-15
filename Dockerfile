FROM amazoncorretto:21.0.1-alpine
MAINTAINER miloshpetrovic
COPY target/car-inventory-0.0.1-SNAPSHOT.jar car-inventory.jar
ENTRYPOINT ["java","-jar","/car-inventory.jar"]