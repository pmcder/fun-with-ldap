
#builds an Alpine Linux image with amazoncorretto installed so we can run a Java app
FROM amazoncorretto:11-alpine3.14-jdk

#tells docker to use a volume mount 
#volume is better for performance
VOLUME /tmp

#copies the jar file to image as app.jar 
COPY target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]