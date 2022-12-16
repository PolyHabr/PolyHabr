@echo off

cd "backend" && .\mvnw "spring-boot:build-image" -DskipTests && cd "../" && docker-compose "up" "--build" "--force-recreate"