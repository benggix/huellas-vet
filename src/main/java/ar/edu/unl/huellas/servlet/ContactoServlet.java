package ar.edu.unl.huellas.servlet;

import ar.edu.unl.huellas.dao.ContactoDAO;
import ar.edu.unl.huellas.model.ConsultaContacto;
import ar.edu.unl.huellas.model.ResultadoOperacion;
import ar.edu.unl.huellas.util.InputSanitizer;
import ar.edu.unl.huellas.util.ValidationUtil;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;


// Controlador para manejar la seccion de contacto, peticiones get para mostrar el formulario de contacto jsp y tambien para manejar peticiones POST para validar y persistir el mensaje en PostgreSQL. Vamos a tener como resultado un mensaje temporal en sesion, donde el usuario sera redirigido a hacia el jsp de contacto para evitar el reenvio del formulario al recargar la pagina.
@WebServlet("/contacto")
public class ContactoServlet extends HttpServlet {

  private final ContactoDAO contactoDAO = new ContactoDAO();

  // Metodo para atender/manejar las peticiones GET redirigiendo a la vista JSP del formulario
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
    forwardToView(req, resp);
  }


  /* Metodo donde se procesa toda la info enviada desde el formulario (POST). Aca se van a realizar las siguientes tareas:
      - sanitizacion, limpiamos todas las entradas del usuario
      - Validacion, verificamos que los datos requeridos no esten vacíos y el email sea valido
      - Persistencia, es decir, si todo es correcto, intentamos recien guardar los datos en la base vía DAO
      - Respuesta, creamos un objeto y lo redirigimos
  */
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    // Primero realizamos la sanitización de los inputs para prevenir inyecciones de codigo (XSS)
    String nombre = InputSanitizer.limpiar(req.getParameter("nombre"));
    String email = InputSanitizer.limpiar(req.getParameter("email"));
    String asunto = InputSanitizer.limpiar(req.getParameter("asunto"));
    String mensaje = InputSanitizer.limpiar(req.getParameter("mensaje"));

    // Despues realizamos la validacion
    if (!ValidationUtil.noVacio(nombre) || !ValidationUtil.noVacio(mensaje)) {
      guardarFlashYRedirigir(req, resp,
              ResultadoOperacion.error("Contanos tu nombre y tu consulta antes de enviar."));
      return;
    }
    if (!ValidationUtil.esEmailValido(email)) {
      guardarFlashYRedirigir(req, resp,
              ResultadoOperacion.error("Ingres\u00e1 un correo electr\u00f3nico v\u00e1lido."));
      return;
    }

    // Por ultimo realizamos la persistencia en la base de datos
    try {
      ConsultaContacto consulta = new ConsultaContacto(nombre, email, asunto, mensaje);
      contactoDAO.guardar(consulta);
      guardarFlashYRedirigir(req, resp, ResultadoOperacion.exito(
              "\u00a1Gracias, " + nombre + "! Recibimos tu consulta y te vamos a responder a la brevedad."));
    } catch (SQLException e) {
      log("Error guardando consulta de contacto", e);
      guardarFlashYRedirigir(req, resp, ResultadoOperacion.error(
              "Ocurri\u00f3 un problema al enviar tu consulta. Prob\u00e1 nuevamente en unos minutos."));
    }
  }

  // Metodo para guardar el resultado de la operacion como un atributo temporal en la sesion y se realiza una redireccion HTTP. Esto mas que nada para evitar que el formulario se vuelva a enviar en caso de que el usuario vaya a actualizar el sitio (prevenimos duplicacion)
  private void guardarFlashYRedirigir(HttpServletRequest req, HttpServletResponse resp,
                                      ResultadoOperacion resultado) throws IOException {
    HttpSession session = req.getSession();
    session.setAttribute("resultadoContacto", resultado);
    resp.sendRedirect(req.getContextPath() + "/contacto");
  }

  // Metodo para delegar la renderizacion visual a neustra pagina JSP interna
  private void forwardToView(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
    RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/contacto.jsp");
    dispatcher.forward(req, resp);
  }
}