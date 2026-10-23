# ---- Build Stage ----
FROM amazoncorretto:22 AS build
WORKDIR /app
COPY . .
RUN yum install -y maven && mvn clean package -DskipTests

# ---- Run Stage ----
FROM amazoncorretto:22
LABEL version="2.0" maintainer="thinkconstructive"
WORKDIR /app
EXPOSE 8080
COPY --from=build /app/target/docker-demo-0.0.1-SNAPSHOT.jar /app/docker-demo.jar
ENTRYPOINT ["java", "-jar", "docker-demo.jar"]