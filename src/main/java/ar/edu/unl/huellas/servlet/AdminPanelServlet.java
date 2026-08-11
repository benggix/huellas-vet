package ar.edu.unl.huellas.servlet;

import ar.edu.unl.huellas.dao.ContactoDAO;
import ar.edu.unl.huellas.dao.NewsletterDAO;
import ar.edu.unl.huellas.dao.TurnoDAO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/* Controlador para manejar el panel de administracion, donde se van a mostrar los turnos, consultas, contactos y suscriptores de newsletter cargados hasta el momento.
   Toda esta info solo podra ser accedido por el admin, ya que esta protegido por AdminAuthFilter, que le pide haber iniciado sesion en /admin/login.
 */

@WebServlet("/admin/panel")
public class AdminPanelServlet extends HttpServlet {

  private final TurnoDAO turnoDAO = new TurnoDAO();
  private final ContactoDAO contactoDAO = new ContactoDAO();
  private final NewsletterDAO newsletterDAO = new NewsletterDAO();

  // Metodo para obtener los datos administrativos de las distintas tablas y poder asi renderizarla en el panel general
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
    try {
      // Intentamos recolectar y carga los datos para que luego pueda ser consumidos/utilizados por la vista JSP
      req.setAttribute("turnos", turnoDAO.listarTodosConMascota());
      req.setAttribute("consultas", contactoDAO.listarTodos());
      req.setAttribute("suscriptores", newsletterDAO.listarTodos());
    } catch (SQLException e) {
      // Si ocurre un error, se registrara el error tecnico en los logs del servidor y mandamos mensaje en la interfaz sin interrumpir la navegacion.
      log("Error cargando datos del panel de administración", e);
      req.setAttribute("errorCarga", "No se pudieron cargar algunos datos. Probá recargar la página.");
    }
    RequestDispatcher dispatcher = req.getRequestDispatcher("/admin/panel.jsp");
    dispatcher.forward(req, resp);
  }
}