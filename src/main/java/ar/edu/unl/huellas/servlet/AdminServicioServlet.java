package ar.edu.unl.huellas.servlet;

import ar.edu.unl.huellas.dao.ServicioDAO;
import ar.edu.unl.huellas.model.ResultadoOperacion;
import ar.edu.unl.huellas.model.Servicio;
import ar.edu.unl.huellas.util.InputSanitizer;
import ar.edu.unl.huellas.util.SlugUtil;
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
import java.util.List;
import java.util.Optional;

/* Controlador para el ABM (Alta, Baja, Modificacion) de servicios desde el panel de administracion.*/
@WebServlet("/admin/servicios")
public class AdminServicioServlet extends HttpServlet {

  private final ServicioDAO servicioDAO = new ServicioDAO();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
    try {
      List<Servicio> servicios = servicioDAO.listarTodos();
      req.setAttribute("servicios", servicios);

      String idParam = req.getParameter("id");
      if (idParam != null && !idParam.isBlank()) {
        try {
          Optional<Servicio> servicio = servicioDAO.obtenerPorId(Integer.parseInt(idParam.trim()));
          servicio.ifPresent(s -> req.setAttribute("servicioEditar", s));
        } catch (NumberFormatException ignored) {
          // Si el id no es numerico, simplemente mostramos el formulario vacio (alta de un servicio nuevo)
        }
      }
    } catch (SQLException e) {
      log("Error cargando servicios para el panel admin", e);
      req.setAttribute("servicios", List.of());
      req.setAttribute("errorCarga", "No se pudieron cargar los servicios. Probá recargar la página.");
    }
    RequestDispatcher dispatcher = req.getRequestDispatcher("/admin/servicios.jsp");
    dispatcher.forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String accion = req.getParameter("accion");

    if ("eliminar".equals(accion)) {
      eliminar(req, resp);
      return;
    }
    guardar(req, resp);
  }

  private void guardar(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String idParam = req.getParameter("id");
    String codigoParam = InputSanitizer.limpiar(req.getParameter("codigo"));
    String titulo = InputSanitizer.limpiar(req.getParameter("titulo"));
    String descripcion = InputSanitizer.limpiar(req.getParameter("descripcion"));
    String detalle = InputSanitizer.limpiar(req.getParameter("detalle"));
    String imagenUrl = InputSanitizer.limpiar(req.getParameter("imagenUrl"));
    String ordenParam = req.getParameter("orden");

    if (!ValidationUtil.noVacio(titulo) || !ValidationUtil.noVacio(descripcion)) {
      guardarFlashYRedirigir(req, resp,
              ResultadoOperacion.error("Completá al menos el título y la descripción antes de guardar."));
      return;
    }

    Integer id = null;
    if (idParam != null && !idParam.isBlank()) {
      try {
        id = Integer.parseInt(idParam.trim());
      } catch (NumberFormatException e) {
        guardarFlashYRedirigir(req, resp, ResultadoOperacion.error("Id de servicio inválido."));
        return;
      }
    }

    int orden = 0;
    if (ordenParam != null && !ordenParam.isBlank()) {
      try {
        orden = Integer.parseInt(ordenParam.trim());
      } catch (NumberFormatException ignored) {
        // Si el orden no es numerico, lo dejamos en 0 (queda al principio del listado) en vez de fallar la carga completa
      }
    }

    try {
      String codigo = ValidationUtil.noVacio(codigoParam) ? SlugUtil.generar(codigoParam) : SlugUtil.generar(titulo);
      if (!ValidationUtil.noVacio(codigo)) {
        guardarFlashYRedirigir(req, resp,
                ResultadoOperacion.error("No se pudo generar un código válido a partir del título."));
        return;
      }
      codigo = asegurarCodigoUnico(codigo, id);

      Servicio servicio = new Servicio();
      servicio.setId(id);
      servicio.setCodigo(codigo);
      servicio.setTitulo(titulo);
      servicio.setDescripcion(descripcion);
      servicio.setDetalle(detalle);
      servicio.setImagenUrl(ValidationUtil.noVacio(imagenUrl) ? imagenUrl : "/resources/img/placeholder.svg");
      servicio.setOrden(orden);

      if (id == null) {
        servicioDAO.crear(servicio);
        guardarFlashYRedirigir(req, resp, ResultadoOperacion.exito("Servicio \"" + titulo + "\" creado correctamente."));
      } else {
        boolean actualizado = servicioDAO.actualizar(servicio);
        if (actualizado) {
          guardarFlashYRedirigir(req, resp, ResultadoOperacion.exito("Servicio \"" + titulo + "\" actualizado correctamente."));
        } else {
          guardarFlashYRedirigir(req, resp, ResultadoOperacion.error("No se encontró el servicio a editar."));
        }
      }
    } catch (SQLException e) {
      log("Error guardando servicio", e);
      guardarFlashYRedirigir(req, resp,
              ResultadoOperacion.error("Ocurrió un problema al guardar el servicio. Probá nuevamente."));
    }
  }

  private void eliminar(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String idParam = req.getParameter("id");
    try {
      int id = Integer.parseInt(idParam.trim());
      boolean eliminado = servicioDAO.eliminar(id);
      guardarFlashYRedirigir(req, resp, eliminado
              ? ResultadoOperacion.exito("Servicio eliminado correctamente.")
              : ResultadoOperacion.error("No se encontró el servicio a eliminar."));
    } catch (NumberFormatException e) {
      guardarFlashYRedirigir(req, resp, ResultadoOperacion.error("Id de servicio inválido."));
    } catch (SQLException e) {
      log("Error eliminando servicio", e);
      guardarFlashYRedirigir(req, resp, ResultadoOperacion.error("Ocurrió un problema al eliminar el servicio."));
    }
  }

  private String asegurarCodigoUnico(String codigoBase, Integer idAExcluir) throws SQLException {
    String codigo = codigoBase;
    int sufijo = 2;
    while (servicioDAO.existeCodigo(codigo, idAExcluir)) {
      codigo = codigoBase + "-" + sufijo;
      sufijo++;
    }
    return codigo;
  }

  private void guardarFlashYRedirigir(HttpServletRequest req, HttpServletResponse resp,
                                      ResultadoOperacion resultado) throws IOException {
    HttpSession session = req.getSession();
    session.setAttribute("resultadoAdminServicio", resultado);
    resp.sendRedirect(req.getContextPath() + "/admin/servicios");
  }
}