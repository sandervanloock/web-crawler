#!/usr/bin/env bash

echo "starting demo crawl"

curl -X POST \
  http://localhost:8081/crawler \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/xml' \
  --data '@crawler-config.xml'