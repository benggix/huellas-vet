package ar.edu.unl.huellas.servlet;

import ar.edu.unl.huellas.dao.NewsletterDAO;
import ar.edu.unl.huellas.model.ResultadoOperacion;
import ar.edu.unl.huellas.model.Suscriptor;
import ar.edu.unl.huellas.util.InputSanitizer;
import ar.edu.unl.huellas.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/* Controlador encargado de gestionar las suscripciones al boletín informativo (Newsletter). A diferencia de otros controladores, este servicio esta diseñado para ser invocado desde elementos globales presentes en cualquier pagina de la app como en footer por ejemplo o sidebar del blog.
   Este controlador va a trabajar solamente para peticiones POST y utilizara una redireccion inteligente para devolver al usuario hacia la misma vista donde se realizo el tramite.
   Obviamente tambien se realiza validaciones en el mail a la hora de guardarlo en PostgreSQL
*/

@WebServlet("/suscripcion")
public class NewsletterServlet extends HttpServlet {

  private final NewsletterDAO newsletterDAO = new NewsletterDAO();

  // Metodo para procesar la peticion de suscripcion, validando el correo para posteriormente registrarlo en el sistema. Al finalizar, sea exitosamente o erroneamente, se guarda el resultado como un mensaje flash/temporal en la sesion y redirigimos al usuario de vuelta a la pagina de origen.
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {

    String email = InputSanitizer.limpiar(req.getParameter("email"));
    ResultadoOperacion resultado;

    // Primero se realiza una validacion de formato en el correo electronico
    if (!ValidationUtil.esEmailValido(email)) {
      resultado = ResultadoOperacion.error("Ingres\u00e1 un correo electr\u00f3nico v\u00e1lido.");
    } else {
      try {
        // Despues intentamos realizar la persistencia de datos, donde si el DAO devuelve true es porque los datos se inserto, si devuelve false es porque ya existia en la BD.
        boolean nuevo = newsletterDAO.guardar(new Suscriptor(email));
        resultado = nuevo
                ? ResultadoOperacion.exito("\u00a1Gracias por suscribirte! Ya sos parte de Huellas.")
                : ResultadoOperacion.exito("Ese correo ya estaba suscripto. \u00a1Gracias igual!");
      } catch (SQLException e) {
        resultado = ResultadoOperacion.error(
                "No pudimos procesar la suscripci\u00f3n. Prob\u00e1 nuevamente en unos minutos.");
        log("Error guardando suscriptor de newsletter", e);
      }
    }
    // Almacenamos el mensaje flash/temporal en la sesion para ser leido en la vista de destino
    HttpSession session = req.getSession();
    session.setAttribute("resultadoNewsletter", resultado);


    // Por ultimo, realizamos la redirección contextual e Inteligente (el patron PRG). Es decir, intentamos volver a la pagina exacta desde donde se envió el formulario leyendo la cabecera "Referer". Tambien por seguridad, se valida estrictamente que la cabecera contenga el context path de la propia aplicacion para evitar redirecciones engañosas hacia sitios web externos que pueden ser maliciosos
    String referer = req.getHeader("Referer");
    String destino = req.getContextPath() + "/index.jsp";
    if (referer != null && referer.contains(req.getContextPath())) {
      destino = referer;
    }

    resp.sendRedirect(destino);
  }
}

