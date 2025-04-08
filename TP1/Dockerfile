# Usar una imagen base de OpenJDK 21
FROM openjdk:21-jdk-slim

# Instalar PostgreSQL en el contenedor
RUN apt-get update && \
    apt-get install -y wget gnupg2 lsb-release curl ca-certificates && \
    echo "deb http://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list && \
    wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | apt-key add - && \
    apt-get update && \
    apt-get install -y postgresql-17 postgresql-client-17 postgresql-contrib-17 && \
    rm -rf /var/lib/apt/lists/*

# Copiar el archivo .jar de tu aplicación al contenedor
COPY target/gestion-comercial-0.0.1-SNAPSHOT.jar /app.jar

# Exponer el puerto que usará PostgreSQL y la aplicación
EXPOSE 5434 8081

# Inicializar la base de datos de PostgreSQL
RUN mkdir -p /var/lib/postgresql/data && \
    chmod 700 /var/lib/postgresql/data && \
    chown -R postgres:postgres /var/lib/postgresql/data && \
    su postgres -c "/usr/lib/postgresql/17/bin/initdb -D /var/lib/postgresql/data"

# Cambiar al usuario postgres para ejecutar el servidor y los comandos de psql
USER postgres

# Script para iniciar PostgreSQL y luego la aplicación
CMD /usr/lib/postgresql/17/bin/pg_ctl -D /var/lib/postgresql/data -l /var/lib/postgresql/data/logfile start && \
    psql -U postgres -c "CREATE DATABASE productos_db;" && \
    psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE productos_db TO postgres;" && \
    java -jar /app.jar
 ##IMPORTANTE PARA GENERAR EL .JAR ANTES DE CORRER
    #EJECUTAR : mvn package
##PRIMERO EJECUTAR  PARA CREAR LA IMAGEN
    #docker build -t gestion_comercial_app .
##PARA CORRER  LA IMAGEN EN DOCKER:
#   docker load -i gestion_comercial_app.tar
##PARA EJECUTAR EL CONTENEDOR
#   docker run --name gestion_comercial_app -e SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/productos_db -e SPRING_DATASOURCE_USERNAME=postgres
    #-e SPRING_DATASOURCE_PASSWORD=admin -p 8081:8081 -p 5432:5432 gestion_comercial_app