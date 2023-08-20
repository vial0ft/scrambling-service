FROM clojure:temurin-20-lein-jammy AS build

ENV CLOJURE_VERSION=1.11.1.1182

RUN mkdir -p /build
WORKDIR /build

COPY . /build

RUN lein uberjar

FROM eclipse-temurin:20-jre-alpine

RUN addgroup -S user && adduser -S user -G user
RUN mkdir -p /service && chown -R user /service
USER user

WORKDIR /service

COPY --from=build /build/target/scrambling-service-0.1.0-SNAPSHOT-standalone.jar /service/app.jar

EXPOSE $PORT

CMD ["java", "-jar", "/service/app.jar"]
