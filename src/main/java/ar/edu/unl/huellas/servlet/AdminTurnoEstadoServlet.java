package ar.edu.unl.huellas.servlet;

import ar.edu.unl.huellas.dao.TurnoDAO;
import ar.edu.unl.huellas.model.ResultadoOperacion;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

/* Controlador para que el admin pueda operar sobre los turnos ya reservados */
@WebServlet("/admin/turnos/estado")
public class AdminTurnoEstadoServlet extends HttpServlet {

  private static final Set<String> ESTADOS_VALIDOS = Set.of(
          "pendiente", "confirmado", "cancelado", "atendido");

  private final TurnoDAO turnoDAO = new TurnoDAO();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String idParam = req.getParameter("turnoId");
    String estado = req.getParameter("estado");

    Integer turnoId = parseIdSeguro(idParam);

    if (turnoId == null || estado == null || !ESTADOS_VALIDOS.contains(estado)) {
      guardarFlashYRedirigir(req, resp,
              ResultadoOperacion.error("No se pudo actualizar el turno: datos inválidos."));
      return;
    }

    try {
      boolean actualizado = turnoDAO.actualizarEstado(turnoId, estado);
      if (actualizado) {
        guardarFlashYRedirigir(req, resp,
                ResultadoOperacion.exito("El turno #" + turnoId + " se marcó como \"" + estado + "\"."));
      } else {
        guardarFlashYRedirigir(req, resp,
                ResultadoOperacion.error("No se encontró el turno #" + turnoId + "."));
      }
    } catch (SQLException e) {
      log("Error actualizando el estado del turno " + turnoId, e);
      guardarFlashYRedirigir(req, resp,
              ResultadoOperacion.error("Ocurrió un problema al actualizar el turno. Probá nuevamente."));
    }
  }

  private Integer parseIdSeguro(String valor) {
    try {
      return valor == null ? null : Integer.valueOf(valor.trim());
    } catch (NumberFormatException e) {
      return null;
    }
  }

  private void guardarFlashYRedirigir(HttpServletRequest req, HttpServletResponse resp,
                                       ResultadoOperacion resultado) throws IOException {
    HttpSession session = req.getSession();
    session.setAttribute("resultadoAdminTurno", resultado);
    resp.sendRedirect(req.getContextPath() + "/admin/panel");
  }
}