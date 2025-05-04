FROM swr.cn-north-4.myhuaweicloud.com/ddn-k8s/docker.io/maven:3.8.5-openjdk-17 AS build
WORKDIR /app
RUN mkdir -p /root/.m2
COPY settings.xml /root/.m2/settings.xml
COPY pom.xml .
RUN mvn dependency:go-offline -B -s /root/.m2/settings.xml
COPY src ./src
RUN mvn package spring-boot:repackage -DskipTests -s /root/.m2/settings.xml \
    && ls -lh /app/target/*.jar

FROM swr.cn-north-4.myhuaweicloud.com/ddn-k8s/docker.io/openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]