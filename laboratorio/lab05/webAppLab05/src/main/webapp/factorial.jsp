<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Cálculo del factorial</title>
</head>
<body>
<h1>Cálculo del factorial</h1>
<!-- Formulario que solicita al usuario el número a usar en el cálculo -->
<form action="factorial.jsp" method="get">
    <p>Número: <input type="text" name="numero">
        <input type="submit" value="Calcular"></p>
</form>
<%
    // A través del objeto request se obtiene el valor pasado por el formulario
    String numeroGet = request.getParameter("numero");
    if(numeroGet!=null) {
        int numero=0;
        double factorial=1;
        boolean error = false;
        try {
            numero = Integer.valueOf(numeroGet);
            if(numero<1) {
                error = true;
            } else {
                for(int i=numero; i>1; i--) {
                    factorial *= i;
                }
            }
        } catch(NumberFormatException e) {
            error = true;
        }
        if(error) {
            out.println("<p>Debe indicar un número entero mayor que 0</p>");
        } else {
            // Mostrar el resultado en la página usando el objeto out
            out.println("<p>Resultado: "+numero + "! = " + factorial+"</p>");
        }
    }

    // Uso del objeto session para contar las veces que se ejecuta la aplicación
    Integer contador = (Integer)session.getAttribute("contadorVisitas");
    if(contador!=null) {
        contador = Integer.valueOf(contador);
    } else {
        contador = 0;
    }
    if(contador!=0) {
        out.println("<p>Ejecuciones de la aplicación en esta sesión: "+contador+
                "</p>");
    }
    contador++;
    session.setAttribute("contadorVisitas", contador);
%>
</body>
</html>