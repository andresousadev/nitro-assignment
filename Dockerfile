FROM maven:3.9-amazoncorretto-21-al2023 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -DskipTests

COPY src ./src

RUN mvn clean install

CMD ["mvn", "compile", "exec:java"]


