# Dockerfile.ci
FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive \
    DISPLAY=:99 \
    TZ=Etc/UTC

# 1) Устанавливаем базовые пакеты
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    maven \
    wget \
    gnupg \
    apt-transport-https \
    xvfb \
    ffmpeg \
    x11-utils \
    unzip \
    curl \
  && rm -rf /var/lib/apt/lists/*

# 2) Устанавливаем Chrome 138
RUN wget -qO- https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
 && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" \
      > /etc/apt/sources.list.d/google-chrome.list \
 && apt-get update \
 && apt-get install -y google-chrome-stable=138.0.7204.183-1 \
 && rm -rf /var/lib/apt/lists/*

# 3) Устанавливаем Chromedriver той же версии
RUN CHROME_VER=$(google-chrome --version | grep -oP '\d+\.\d+\.\d+\.\d+') \
 && LATEST=$(curl -sSL "https://chromedriver.storage.googleapis.com/LATEST_RELEASE_${CHROME_VER}") \
 && wget -O /tmp/chromedriver.zip "https://chromedriver.storage.googleapis.com/${LATEST}/chromedriver_linux64.zip" \
 && unzip /tmp/chromedriver.zip -d /usr/local/bin \
 && chmod +x /usr/local/bin/chromedriver \
 && rm /tmp/chromedriver.zip

# 4) Копируем проект и предварительно кешируем зависимости
WORKDIR /app
COPY pom.xml .
# кешируем зависимости, чтобы не тянуть на каждом билде
RUN mvn dependency:go-offline -B

COPY . .

# 5) Копируем entrypoint-скрипт
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]