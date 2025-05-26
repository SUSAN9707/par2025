# Etapa de build
FROM node:18-alpine AS build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build

# Etapa de producción con nginx
FROM nginx:alpine

# Copiamos el build generado a la carpeta de nginx
COPY --from=build /app/dist /usr/share/nginx/html

# Exponemos el puerto 80 para servir la app
EXPOSE 80

# Arrancamos nginx en primer plano
CMD ["nginx", "-g", "daemon off;"]
