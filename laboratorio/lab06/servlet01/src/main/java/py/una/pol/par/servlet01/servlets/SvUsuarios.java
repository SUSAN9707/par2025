package py.una.pol.par.servlet01.servlets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@WebServlet(name = "SvUsuarios", urlPatterns = {"/SvUsuarios"})
public class SvUsuarios extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse
            response)
            throws ServletException, IOException {

    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        List<List<String>> listaUsuarios = (List<List<String>>) session.getAttribute("listaUsuarios");

        try (var out = response.getWriter()) {
            out.println("<html><body>");
            out.println("<h1>Lista de usuarios</h1>");

            if (listaUsuarios == null || listaUsuarios.isEmpty()) {
                out.println("<p>No hay usuarios cargados.</p>");
            } else {
                out.println("<table border='1'>");
                out.println("<tr><th>CI</th><th>Nombre</th><th>Apellido</th><th>Teléfono</th></tr>");

                for (List<String> usuario : listaUsuarios) {
                    out.println("<tr>");
                    for (String dato : usuario) {
                        out.println("<td>" + dato + "</td>");
                    }
                    out.println("</tr>");
                }

                out.println("</table>");
            }

            out.println("<p><a href='index.jsp'>Volver al inicio</a></p>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String ci = request.getParameter("ci");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String telefono = request.getParameter("telefono");

        System.out.println("CI es: " + ci);
        System.out.println("Nombre es: " + nombre);
        System.out.println("Apellido es: " + apellido);
        System.out.println("Teléfono es: " + telefono);

        // Crear una lista de atributos del usuario
        List<String> usuario = new ArrayList<>();
        usuario.add(ci);
        usuario.add(nombre);
        usuario.add(apellido);
        usuario.add(telefono);

        // Obtener la sesión y guardar la lista
        HttpSession session = request.getSession();

        // Recuperar lista de usuarios ya guardados (si existe)
        List<List<String>> listaUsuarios = (List<List<String>>) session.getAttribute("listaUsuarios");
        if (listaUsuarios == null) {
            listaUsuarios = new ArrayList<>();
        }

        // Agregar nuevo usuario
        listaUsuarios.add(usuario);

        // Guardar de nuevo en la sesión
        session.setAttribute("listaUsuarios", listaUsuarios);

        // Redirigir de nuevo al index.jsp (opcional)
        response.sendRedirect("index.jsp?exito=true");
    }
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
