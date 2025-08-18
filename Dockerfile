# Берём официальный образ с Chrome 138 и всеми зависимостями
FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive

# Установка JDK 17 и Maven
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk maven wget unzip ffmpeg xvfb x11-utils ca-certificates && \
    rm -rf /var/lib/apt/lists/*

# Установка Chrome 138
RUN wget https://storage.googleapis.com/chrome-for-testing-public/138.0.7204.183/linux64/chrome-linux64.zip && \
    unzip chrome-linux64.zip -d /opt/ && \
    mv /opt/chrome-linux64 /opt/chrome && \
    ln -s /opt/chrome/chrome /usr/bin/google-chrome && \
    rm chrome-linux64.zip

# Установка Chromedriver 138
RUN wget https://storage.googleapis.com/chrome-for-testing-public/138.0.7204.183/linux64/chromedriver-linux64.zip && \
    unzip chromedriver-linux64.zip -d /usr/bin/ && \
    chmod +x /usr/bin/chromedriver && \
    rm chromedriver-linux64.zip

WORKDIR /workspace

# Проверка
RUN google-chrome --version && chromedriver --version
