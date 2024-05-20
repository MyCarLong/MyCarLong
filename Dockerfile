# 빌드 스테이지
FROM gradle:8.7-jdk17 AS build
WORKDIR /home/gradle/src
RUN git clone -b release https://github.com/MyCarLong/MyCarLong.git .
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
RUN useradd --system --uid 1000 --gid worker --disabled-password worker
USER worker:worker

EXPOSE 8097
COPY --from=build /home/gradle/src/build/libs/*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]