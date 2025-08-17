#!/bin/bash

cd /home/ubuntu/ourhood-server
docker-compose down
docker pull 711387105994.dkr.ecr.ap-northeast-2.amazonaws.com/ourhood-server:latest
docker-compose up -d
