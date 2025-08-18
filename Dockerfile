FROM maven:3.9.9-eclipse-temurin-17

# Установка всех библиотек, необходимых для запуска Chrome 138
RUN apt-get update && apt-get install -y \
    wget unzip xvfb ffmpeg x11-utils \
    libnspr4 libnss3 libatk1.0-0 libatk-bridge2.0-0 \
    libcups2 libx11-xcb1 libxcomposite1 libxdamage1 libxrandr2 \
    libgtk-3-0 libasound2 libdbus-glib-1-2 libpangocairo-1.0-0 \
 && wget https://storage.googleapis.com/chrome-for-testing-public/138.0.7204.183/linux64/chrome-linux64.zip \
 && unzip chrome-linux64.zip -d /opt/ \
 && mv /opt/chrome-linux64 /opt/chrome \
 && ln -s /opt/chrome/chrome /usr/bin/google-chrome \
 && wget https://storage.googleapis.com/chrome-for-testing-public/138.0.7204.183/linux64/chromedriver-linux64.zip \
 && unzip chromedriver-linux64.zip -d /opt/ \
 && mv /opt/chromedriver-linux64/chromedriver /usr/bin/chromedriver \
 && chmod +x /usr/bin/chromedriver \
 && rm -rf *.zip \
 && apt-get clean && rm -rf /var/lib/apt/lists/*

# Проверка установок
RUN google-chrome --version && chromedriver --version

WORKDIR /workspace
