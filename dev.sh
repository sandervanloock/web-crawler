#!/usr/bin/env bash

echo "starting docker containers"
docker-compose up -d

echo "starting JEF monitor"
cd norconex-jef-monitor
./jef-monitor.bat
cd ../

echo "starting spring boot app"
cd norconex-crawler
#./mvnw spring-boot:run
cd ../