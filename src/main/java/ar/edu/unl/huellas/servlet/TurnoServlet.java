package ar.edu.unl.huellas.servlet;

import ar.edu.unl.huellas.dao.MascotaDAO;
import ar.edu.unl.huellas.dao.ServicioDAO;
import ar.edu.unl.huellas.dao.TurnoDAO;
import ar.edu.unl.huellas.model.Mascota;
import ar.edu.unl.huellas.model.ResultadoOperacion;
import ar.edu.unl.huellas.model.Servicio;
import ar.edu.unl.huellas.model.Turno;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;


// Controlador para la seccion de turnos. Manejaremos la peticion GET para mostrar el formulario al usuario y tambien, peticion POST para validar, buscar/crear la mascota, verificando la superposicion para luego guardar el turno en PostgreSQL
@WebServlet("/turnos")
public class TurnoServlet extends HttpServlet {

  // Aca estan los horario de atencion operativo para las reservas de los turnos.
  private static final LocalTime APERTURA = LocalTime.of(9, 0);
  private static final LocalTime CIERRE = LocalTime.of(19, 0);

  private final TurnoDAO turnoDAO = new TurnoDAO();
  private final MascotaDAO mascotaDAO = new MascotaDAO();
  private final ServicioDAO servicioDAO = new ServicioDAO();

  // Metodo para atender la petición GET preparando los datos necesarios y renderizando la vista del formulario
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
    forwardToView(req, resp);
  }

    /* Metodo que procesa y valida integralmente una nueva solicitud de turno. El proceso es el siguiente:
        - Sanitizacion, limpiamos las entradas de texto para prevenir ataques XSS
        - Completitud, verificamos que la presencia de todos los campos sean obligatorios
        - Parseo, para conviertir y validar los formatos de fecha y hora
        - Reglas de negocio, para asegurar que la fecha sea futura y el horario este dentro del rango de apertura/cierre de la clinica
        - Disponibilidad, para verificar en tiempo real de que no exista un turno superpuesto en el mismo horario
        - Persistencia, para registrar o recuperar la mascota y reservar el turno
     */

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {

    // Sanitizacion de las entradas de texto contra XSS
    String nombreDueno = InputSanitizer.limpiar(req.getParameter("nombreDueno"));
    String contacto = InputSanitizer.limpiar(req.getParameter("contacto"));
    String mascota = InputSanitizer.limpiar(req.getParameter("mascota"));
    String especie = InputSanitizer.limpiar(req.getParameter("especie"));
    String servicio = InputSanitizer.limpiar(req.getParameter("servicio"));
    String notas = InputSanitizer.limpiar(req.getParameter("notas"));
    String fechaParam = req.getParameter("fecha");
    String horaParam = req.getParameter("hora");

    // validamos para que los campos sean obligatorios
    if (!ValidationUtil.noVacio(nombreDueno) || !ValidationUtil.noVacio(contacto)
            || !ValidationUtil.noVacio(mascota) || !ValidationUtil.noVacio(especie)
            || !ValidationUtil.noVacio(servicio) || !ValidationUtil.noVacio(fechaParam)
            || !ValidationUtil.noVacio(horaParam)) {
      volverConError(req, resp, "Faltan datos obligatorios. Revis\u00e1 el formulario.");
      return;
    }

    // Parseamos y validamos los tipos de datos temporales
    LocalDate fecha;
    LocalTime hora;
    try {
      fecha = LocalDate.parse(fechaParam);
      hora = LocalTime.parse(horaParam);
    } catch (DateTimeParseException e) {
      volverConError(req, resp, "La fecha u horario ingresados no son v\u00e1lidos.");
      return;
    }

    // Validamos acorde a las reglas de negocio temporales
    if (fecha.isBefore(LocalDate.now())) {
      volverConError(req, resp, "La fecha del turno no puede ser anterior a hoy.");
      return;
    }
    if (hora.isBefore(APERTURA) || hora.isAfter(CIERRE)) {
      volverConError(req, resp, "El horario debe estar entre las 9:00 y las 19:00.");
      return;
    }

    // Validamos la concurrencia y persistencia de datos
    try {
      if (turnoDAO.existeSuperposicion(fecha, hora)) {
        volverConError(req, resp,
                "Ese horario ya fue reservado por otro cliente. Eleg\u00ed otro, por favor.");
        return;
      }
      // Buscamos si la mascota ya esta registrada, si no lo esta, se crea una nueva
      int mascotaId = mascotaDAO.buscarOCrear(new Mascota(mascota, especie, nombreDueno, contacto));
      Turno turno = new Turno(mascotaId, servicio, fecha, hora, notas);
      boolean guardado = turnoDAO.guardar(turno);

      if (guardado) {
        volverConExito(req, resp, "\u00a1Listo! Reservamos el turno de " + mascota
                + " para el " + fecha + " a las " + hora + ".");
      } else {
        volverConError(req, resp,
                "Ese horario se ocup\u00f3 justo antes que el tuyo. Prob\u00e1 con otro horario.");
      }
    } catch (SQLException e) {
      log("Error guardando turno", e);
      volverConError(req, resp,
              "Ocurri\u00f3 un problema al guardar el turno. Intent\u00e1 nuevamente en unos minutos.");
    }
  }

  private void volverConError(HttpServletRequest req, HttpServletResponse resp, String mensaje)
          throws IOException {
    guardarFlashYRedirigir(req, resp, ResultadoOperacion.error(mensaje));
  }

  private void volverConExito(HttpServletRequest req, HttpServletResponse resp, String mensaje)
          throws IOException {
    guardarFlashYRedirigir(req, resp, ResultadoOperacion.exito(mensaje));
  }

  // Metodo para centralizar la logica del patron PRG (Post/Redirect/Get), guardando el mensaje flash/temporal en la sesion y redirigiendo al endpoint de turnos
  private void guardarFlashYRedirigir(HttpServletRequest req, HttpServletResponse resp,
                                      ResultadoOperacion resultado) throws IOException {
    HttpSession session = req.getSession();
    session.setAttribute("resultadoTurno", resultado);
    resp.sendRedirect(req.getContextPath() + "/turnos");
  }

  // Metodo para cargar las dependencias necesarias desde la base de datos, es decir, la lista de servicios y lo enviamos hacia la vista JSP interna que se encarga de manejarlo.
  private void forwardToView(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
    try {
      List<Servicio> servicios = servicioDAO.listarTodos();
      req.setAttribute("servicios", servicios);
    } catch (SQLException e) {
      // Si falla la BD, se loguea el error y se envía una lista vacía para evitar que la interfaz de usuario se rompa o arroje un error 500
      log("Error obteniendo servicios para el formulario de turnos", e);
      req.setAttribute("servicios", List.of());
    }
    RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/turnos.jsp");
    dispatcher.forward(req, resp);
  }
}
