Sakai is an application template for Vue based on the [create-vue](https://github.com/vuejs/create-vue), the recommended way to start a Vite-powered Vue projects.

Visit the [documentation](https://sakai.primevue.org/documentation) to get started.

#Frontend - Instrucciones para levantar con Docker
Este proyecto Vue puede ejecutarse fácilmente mediante Docker, sin necesidad de instalar Node.js ni dependencias manuales.

#Pasos para levantar el frontend:
Construir la imagen Docker:

docker build -t mi_vue_frontend .


Ejecutar el contenedor:

docker run -d -p 5173:80 --name sakai_vue_frontend mi_vue_frontend


###El frontend quedará disponible en: http://localhost:5173
