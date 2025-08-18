#!/usr/bin/env bash
set -e

echo "Starting tests in Docker..."

# Запуск виртуального дисплея
Xvfb :99 -screen 0 1366x768x24 & sleep 3
export DISPLAY=:99

# Старт записи экрана
ffmpeg -y -video_size 1366x768 -framerate 15 -f x11grab -i :99 \
    -codec:v libx264 -pix_fmt yuv420p screen_recording.mp4 > /dev/null 2>&1 & echo $! > ffmpeg_pid.txt

# Запуск Maven тестов
mvn -B clean test -DsuiteXmlFile='src/test/resources/StartSwap.xml' \
    -DSEED_PHRASE="$SEED" -DPASSWORD="$PASS" -DPIN="$PIN"

# Остановка записи
kill -INT $(cat ffmpeg_pid.txt) && sleep 2

# Копирование видео для Allure
mkdir -p target/allure-results
cp screen_recording.mp4 target/allure-results/

echo "Tests finished!"
