FROM maven:3.9.16-eclipse-temurin-25 AS builder

WORKDIR /workspace
COPY transport/pom.xml transport/mvnw ./
COPY transport/.mvn/ .mvn/
COPY transport/src/ src/

RUN --mount=type=cache,target=/root/.m2 \
    chmod +x mvnw \
    && ./mvnw --batch-mode --no-transfer-progress package \
    && mkdir -p /out \
    && cp target/transport-*.jar /out/app.jar

FROM eclipse-temurin:25-jre-alpine AS runner

LABEL org.opencontainers.image.title="NL Transport" \
      org.opencontainers.image.description="NL Transport shipment tracking application"

WORKDIR /app
COPY --from=builder --chown=10001:10001 /out/app.jar /app/app.jar

ENV PORT=8080 \
    JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0 -XX:+UseContainerSupport"

EXPOSE 8080
USER 10001:10001

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
