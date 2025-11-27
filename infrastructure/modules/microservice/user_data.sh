#!/bin/bash
yum update -y
yum install -y docker git
systemctl enable docker
systemctl start docker

curl -L "https://github.com/docker/compose/releases/download/v2.27.1/docker-compose-linux-x86_64" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

git clone ${repo_url} /app

cd /app

docker-compose -f ${compose_file} up -d
