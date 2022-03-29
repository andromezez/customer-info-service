FROM openjdk:17.0.2-slim
COPY application-start/target/customer-info-service-application-start-2.1.0.jar customer-info-service-application-start-2.1.0.jar
EXPOSE 8090 8091
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-Dsun.jnu.encoding=UTF-8", "-server", "-XX:+UseG1GC", "-jar", "/customer-info-service-application-start-2.1.0.jar"]