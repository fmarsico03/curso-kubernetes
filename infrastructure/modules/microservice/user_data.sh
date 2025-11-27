#!/bin/bash
set -xe

apt-get update -y
apt-get install -y docker.io git docker-compose

systemctl enable docker
systemctl start docker

git clone ${repo_url} /app
cd /app


if [ "${name}" = "msvc-gateway" ]; then
    echo "Exporting environment variables for the gateway..."

    export USUARIOS_URL="${usuarios_url}"
    export CURSOS_URL="${cursos_url}"
fi

docker-compose -f ${compose_file} up -d