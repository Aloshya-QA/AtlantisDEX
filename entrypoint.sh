#!/bin/bash
set -e

# 1) Запускаем Xvfb
Xvfb :99 -screen 0 1366x768x24 &
XVFB_PID=$!
sleep 2

# 2) Запускаем запись экрана
ffmpeg -y \
  -video_size 1366x768 \
  -framerate 15 \
  -f x11grab \
  -i :99 \
  -codec:v libx264 \
  -pix_fmt yuv420p \
  screen_recording.mp4 > /dev/null 2>&1 &
FFMPEG_PID=$!

# 3) Запускаем тесты
mvn clean test \
  -DsuiteXmlFile='src/test/resources/StartSwap.xml' \
  -DSEED_PHRASE="${SEED}" \
  -DPASSWORD="${PASSWORD}" \
  -DPIN="${PIN}"

# 4) Останавливаем запись
kill -INT ${FFMPEG_PID} || true
sleep 2

# 5) Перемещаем в папку для Allure
mkdir -p target/allure-results
mv screen_recording.mp4 target/allure-results/

# 6) Выход с кодом Maven
wait ${XVFB_PID} 2>/dev/null || true
exit $(mvn help:evaluate -q -Dexpression=lastBuildExitCode)
