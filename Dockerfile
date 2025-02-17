# base image
FROM --platform=linux/amd64 openjdk:21-jdk-slim
# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일을 컨테이너로 복사
COPY build/libs/hair-mvp-0.0.1-SNAPSHOT.jar app.jar

# 포트 열기
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]