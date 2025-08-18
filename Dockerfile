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

# Установка Chrome 138.0.7204.157 (не Chrome for Testing)
RUN wget -q https://dl.google.com/linux/direct/google-chrome-stable_138.0.7204.157-1_amd64.deb && \
    apt-get update && apt-get install -y ./google-chrome-stable_138.0.7204.157-1_amd64.deb && \
    rm google-chrome-stable_138.0.7204.157-1_amd64.deb && \
    ln -s /usr/bin/google-chrome-stable /usr/bin/google-chrome

# Установка Chromedriver 138.0.7204.157
RUN wget -q https://chromedriver.storage.googleapis.com/138.0.7204.157/chromedriver_linux64.zip && \
    unzip chromedriver_linux64.zip -d /tmp/ && \
    mv /tmp/chromedriver /usr/bin/chromedriver && \
    chmod +x /usr/bin/chromedriver && \
    rm chromedriver_linux64.zip

# Проверка версий
RUN google-chrome --version && chromedriver --version && java -version && mvn -version

# Рабочая директория
WORKDIR /workspace
