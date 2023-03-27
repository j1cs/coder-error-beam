#!/usr/bin/env sh
set -e

trap "make down" SIGINT

./mvnw mn:run -DskipTests=true -Dmn.jvmArgs="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"