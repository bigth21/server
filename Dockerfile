FROM amazoncorretto:21

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 8080

ENV TZ=Asia/Seoul
ENV SPRING_PROFILES_ACTIVE=local

ENTRYPOINT ["java", \
            "-Duser.timezone=${TZ}", \
            "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", \
            "-jar", "app.jar"]
# docker build -t server:latest .
# docker run -p 8080:8080 -e TZ=America/New_York -e SPRING_PROFILES_ACTIVE=prod server:latest