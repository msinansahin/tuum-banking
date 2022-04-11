FROM eclipse-temurin:17-jre-alpine

EXPOSE 8083

ADD ./build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
