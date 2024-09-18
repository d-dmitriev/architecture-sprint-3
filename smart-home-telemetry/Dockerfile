FROM maven:3.9.8-eclipse-temurin-21-alpine AS builder
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21.0.4_7-jre-alpine
WORKDIR /opt/app
RUN addgroup --system javauser && adduser -S -s /usr/sbin/nologin -G javauser javauser
COPY --from=builder /build/target/smart-home-telemetry-1.0-SNAPSHOT.jar app.jar
RUN chown -R javauser:javauser .
USER javauser
ENV PG_HOST=localhost
ENTRYPOINT ["java", "-jar", "app.jar"]