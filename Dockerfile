FROM gradle:6.3.0-jdk8 as builder
# Create and choose a workdir
RUN mkdir /home/gradle/project
WORKDIR /home/gradle/project
# Copy pom.xml to get dependencies
COPY build.gradle settings.gradle ./
# This will resolve dependencies and cache them
RUN gradle clean build
# Copy sources
COPY src src
# Build app (jar will be in /usr/src/app/target/)
RUN gradle clean build

FROM openjdk:8-jre-alpine
WORKDIR /opt/app
# Copy bin from builder to this new image
COPY --from=builder /home/gradle/project/build/libs/flink-simple-demo-app-1.0-SNAPSHOT.jar flink-simple-demo-app-1.0-SNAPSHOT.jar
# Default command, run app
CMD java -jar flink-simple-demo-app-1.0-SNAPSHOT.jar
