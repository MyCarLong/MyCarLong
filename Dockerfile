# 빌드 스테이지
FROM gradle:8.7-jdk17 AS build
WORKDIR /home/gradle/src
RUN git clone -b release https://github.com/MyCarLong/MyCarLong.git .
WORKDIR /home/gradle/src/MyCarLong
RUN git pull origin release
WORKDIR /home/gradle/src
RUN gradle clean build --no-daemon

# 패키지 스테이지
FROM openjdk:17-jdk-buster
# Chrome 및 ChromeDriver 설치
RUN apt-get update && apt-get install -y curl unzip xvfb libxi6 libgconf-2-4 \
    && curl -sS -o - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" | tee /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable \
    && wget https://chromedriver.storage.googleapis.com/2.41/chromedriver_linux64.zip \
    && unzip chromedriver_linux64.zip \
    && mkdir -p /app/chrome \
    && mv chromedriver /app/chrome/chromedriver \
    && chown root:root /app/chrome/chromedriver \
    && chmod +x /app/chrome/chromedriver

# 사용자 및 그룹 추가
RUN groupadd --system --gid 1000 worker
RUN useradd --system --uid 1000 --gid worker worker

# WebDriverManager 캐시 디렉토리 생성 및 권한 설정
RUN mkdir -p /home/worker/.cache/selenium
RUN chown -R worker:worker /home/worker/.cache/selenium

USER worker:worker

# 환경 변수 설정
ENV MySQL_URL ${MySQL_URL}
ENV MySQL_PW ${MySQL_PW}
ENV SpringJWT ${SpringJWT}
ENV Naver_ClientID ${Naver_ClientID}
ENV Naver_Secret ${Naver_Secret}
ENV Google_ClientID ${Google_ClientID}
ENV Google_Secret ${Google_Secret}
ENV Kakao_ClientID ${Kakao_ClientID}
ENV Kakao_Secret ${Kakao_Secret}
ENV S3_AccessKey ${S3_AccessKey}
ENV S3_SecretKey ${S3_SecretKey}
ENV S3_BucketName ${S3_BucketName}
ENV S3_Region ${S3_Region}
ENV CloudFrontURL ${CloudFrontURL}
ENV Kakao_redirectURL ${Kakao_redirectURL}
ENV Google_redirectURL ${Google_redirectURL}
ENV Naver_redirectURL ${Naver_redirectURL}
ENV FrontServer ${FrontServer}
ENV FrontServerIp ${FrontServerIp}

EXPOSE 8097
COPY --from=build /home/gradle/src/build/libs/*.jar /app.jar
ENTRYPOINT ["java","-Xms128m","-Xmx512m","-Dspring.profiles.active=${PROFILE}","-jar","/app.jar"]

