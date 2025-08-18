FROM maven:3.9.9-eclipse-temurin-17

# Chrome 138 + Chromedriver + утилиты
RUN apt-get update && apt-get install -y wget gnupg unzip xvfb ffmpeg x11-utils \
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

RUN google-chrome --version && chromedriver --version

WORKDIR /workspace
