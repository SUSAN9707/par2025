<%--
  Created by IntelliJ IDEA.
  User: Susana
  Date: 10/5/2025
  Time: 21:00
--%>
<%@ page import="java.io.*, java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Galería de Imágenes</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        .galeria {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
        }
        .galeria img {
            width: 200px;
            height: auto;
            border: 1px solid #ccc;
        }
    </style>
</head>
<body>
<h1>Galería de Imágenes</h1>
<div class="galeria">
    <%
        String imagenesPath = application.getRealPath("/imagenes");
        File carpeta = new File(imagenesPath);
        String[] archivos = carpeta.list();

        if (archivos != null) {
            for (String archivo : archivos) {
                if (archivo.endsWith(".jpg") || archivo.endsWith(".png") || archivo.endsWith(".jpeg") || archivo.endsWith(".gif")) {
    %>
    <img src="imagenes/<%= archivo %>" alt="<%= archivo %>">
    <%
                }
            }
        } else {
            out.println("<p>No se encontraron imágenes.</p>");
        }
    %>
</div>
</body>
</html>

