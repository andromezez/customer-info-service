FROM openjdk:11
COPY application-start/target/customer-info-service-application-start-1.0.0.jar customer-info-service-application-start-1.0.0.jar
EXPOSE 8091
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-Dsun.jnu.encoding=UTF-8", "-jar", "/customer-info-service-application-start-1.0.0.jar"]