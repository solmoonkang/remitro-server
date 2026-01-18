# Java 17 (사용 중인 버전 확인) 기반 이미지
FROM amazoncorretto:17-alpine-jdk

# 빌드된 jar 파일을 컨테이너 안으로 복사
COPY remitro-gateway/build/libs/*-SNAPSHOT.jar app.jar

# 실행 명령어
ENTRYPOINT ["java", "-jar", "/app.jar"]
