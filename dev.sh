#!/usr/bin/env bash

docker-compose stop

echo "starting docker containers"
docker-compose up -d

echo "starting JEF monitor on port 8082"
cd norconex-jef-monitor
./jef-monitor.bat
cd ../

echo "starting spring boot app"
cd norconex-crawler
#./mvnw spring-boot:run
cd ../