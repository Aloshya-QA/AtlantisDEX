FROM ubuntu:24.04

# Устанавливаем базовые зависимости
RUN apt-get update && apt-get install -y --no-install-recommends \
    openjdk-17-jdk maven wget unzip xvfb ffmpeg x11-utils ca-certificates \
    libnspr4 libnss3 libatk1.0-0 libatk-bridge2.0-0 libcups2 libx11-xcb1 \
    libxcomposite1 libxdamage1 libxrandr2 libgtk-3-0 libasound2 \
    libdbus-glib-1-2 libpangocairo-1.0-0 fonts-liberation libxss1 libgconf-2-4 \
    && rm -rf /var/lib/apt/lists/*

# Установка Google Chrome
RUN wget https://storage.googleapis.com/chrome-for-testing-public/138.0.7204.183/linux64/chrome-linux64.zip && \
    unzip chrome-linux64.zip -d /opt/ && \
    mv /opt/chrome-linux64 /opt/chrome && \
    ln -s /opt/chrome/chrome /usr/bin/google-chrome && \
    rm chrome-linux64.zip

# Установка Chromedriver
RUN wget https://storage.googleapis.com/chrome-for-testing-public/138.0.7204.183/linux64/chromedriver-linux64.zip && \
    unzip chromedriver-linux64.zip -d /usr/bin/ && \
    chmod +x /usr/bin/chromedriver && \
    rm chromedriver-linux64.zip

# Переменная окружения для Selenide/Selenium
ENV WEBDRIVER_CHROME_DRIVER=/usr/bin/chromedriver
ENV PATH=$PATH:/usr/bin

WORKDIR /workspace
