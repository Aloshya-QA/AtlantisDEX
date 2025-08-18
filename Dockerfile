# Базовый образ
FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive

# Установка JDK 17, Maven и зависимостей
RUN apt-get update && apt-get upgrade -y && \
    apt-get install -y openjdk-17-jdk maven wget unzip xvfb ffmpeg x11-utils ca-certificates \
        libnspr4 libnss3 libatk1.0-0 libatk-bridge2.0-0 libcups2 libx11-xcb1 libxcomposite1 \
        libxdamage1 libxrandr2 libgtk-3-0 libasound2 libdbus-glib-1-2 fonts-liberation libxss1 \
        libgconf-2-4 && \
    rm -rf /var/lib/apt/lists/*

# Установка Chrome 138
RUN wget https://storage.googleapis.com/chrome-for-testing-public/138.0.7204.183/linux64/chrome-linux64.zip && \
    unzip chrome-linux64.zip -d /opt/ && \
    mv /opt/chrome-linux64 /opt/chrome && \
    ln -s /opt/chrome/chrome /usr/bin/google-chrome && \
    rm chrome-linux64.zip

# Установка Chromedriver 138
RUN wget https://storage.googleapis.com/chrome-for-testing-public/138.0.7204.183/linux64/chromedriver-linux64.zip && \
    unzip chromedriver-linux64.zip -d /tmp/ && \
    mv /tmp/chromedriver-linux64/chromedriver /usr/bin/chromedriver && \
    chmod +x /usr/bin/chromedriver && \
    rm -rf /tmp/chromedriver-linux64 chromedriver-linux64.zip

# Проверка версий
RUN google-chrome --version && chromedriver --version && java -version && mvn -version

# Рабочая директория
WORKDIR /workspace
