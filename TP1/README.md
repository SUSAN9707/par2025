
# Gestión Comercial – Backend

Este proyecto es el backend de la aplicación de Gestión Comercial. Utiliza Spring Boot y PostgreSQL como base de datos. A continuación se detallan los pasos para compilar y levantar el proyecto con Docker.


## Pasos para levantar el backend

1. **Empaquetar la aplicación(PASO MUY IMPORTANTE!!):**

   mvn clean package


Construir la imagen Docker:

docker build -t gestion_comercial_app .


Ejecutar el contenedor:


docker run --name gestion_comercial_app -e SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/productos_db -e SPRING_DATASOURCE_USERNAME=postgres -e SPRING_DATASOURCE_PASSWORD=admin -p 8081:8081 -p 5432:5432 gestion_comercial_app

#El contenedor expondrá el backend en http://localhost:8081.