package ar.edu.unl.huellas.servlet;

import ar.edu.unl.huellas.dao.BlogDAO;
import ar.edu.unl.huellas.model.ArticuloBlog;
import ar.edu.unl.huellas.model.ResultadoOperacion;
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

/* Controlador para el ABM (Alta, Baja, Modificacion) de articulos del blog desde el panel de administracion. Antes, el contenido del blog salia unicamente de los seeders de schema.sql; con este servlet el admin puede cargar, editar y eliminar articulos sin tocar la base de datos a mano. Esta protegido por AdminAuthFilter (todo lo que empieza con /admin/*) y por CsrfFilter para las peticiones POST. */
@WebServlet("/admin/blog")
public class AdminBlogServlet extends HttpServlet {

  private final BlogDAO blogDAO = new BlogDAO();

  // GET: muestra el listado de articulos y, si viene ?id=, precarga el formulario en modo edicion
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
    try {
      List<ArticuloBlog> articulos = blogDAO.listarTodos();
      req.setAttribute("articulos", articulos);

      String idParam = req.getParameter("id");
      if (idParam != null && !idParam.isBlank()) {
        try {
          Optional<ArticuloBlog> articulo = blogDAO.obtenerPorId(Integer.parseInt(idParam.trim()));
          articulo.ifPresent(a -> req.setAttribute("articuloEditar", a));
        } catch (NumberFormatException ignored) {
          // Si el id no es numerico, simplemente mostramos el formulario vacio (alta de un articulo nuevo)
        }
      }
    } catch (SQLException e) {
      log("Error cargando articulos del blog para el panel admin", e);
      req.setAttribute("articulos", List.of());
      req.setAttribute("errorCarga", "No se pudieron cargar los artículos. Probá recargar la página.");
    }
    RequestDispatcher dispatcher = req.getRequestDispatcher("/admin/blog.jsp");
    dispatcher.forward(req, resp);
  }

  // POST: procesa tanto el alta/edicion (accion=guardar) como la baja (accion=eliminar) de un articulo
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
    String etiqueta = InputSanitizer.limpiar(req.getParameter("etiqueta"));
    String titulo = InputSanitizer.limpiar(req.getParameter("titulo"));
    String resumen = InputSanitizer.limpiar(req.getParameter("resumen"));
    String contenido = InputSanitizer.limpiar(req.getParameter("contenido"));
    String imagenUrl = InputSanitizer.limpiar(req.getParameter("imagenUrl"));
    String slugParam = InputSanitizer.limpiar(req.getParameter("slug"));

    if (!ValidationUtil.noVacio(etiqueta) || !ValidationUtil.noVacio(titulo)
            || !ValidationUtil.noVacio(resumen) || !ValidationUtil.noVacio(contenido)) {
      guardarFlashYRedirigir(req, resp,
              ResultadoOperacion.error("Completá etiqueta, título, resumen y contenido antes de guardar."));
      return;
    }

    Integer id = null;
    if (idParam != null && !idParam.isBlank()) {
      try {
        id = Integer.parseInt(idParam.trim());
      } catch (NumberFormatException e) {
        guardarFlashYRedirigir(req, resp, ResultadoOperacion.error("Id de artículo inválido."));
        return;
      }
    }

    try {
      String slug = ValidationUtil.noVacio(slugParam) ? SlugUtil.generar(slugParam) : SlugUtil.generar(titulo);
      if (!ValidationUtil.noVacio(slug)) {
        guardarFlashYRedirigir(req, resp,
                ResultadoOperacion.error("No se pudo generar una URL válida a partir del título. Probá con otro título."));
        return;
      }
      slug = asegurarSlugUnico(slug, id);

      ArticuloBlog articulo = new ArticuloBlog();
      articulo.setId(id);
      articulo.setSlug(slug);
      articulo.setEtiqueta(etiqueta);
      articulo.setTitulo(titulo);
      articulo.setResumen(resumen);
      articulo.setContenido(contenido);
      articulo.setImagenUrl(ValidationUtil.noVacio(imagenUrl) ? imagenUrl : "/resources/img/placeholder.svg");

      if (id == null) {
        blogDAO.crear(articulo);
        guardarFlashYRedirigir(req, resp, ResultadoOperacion.exito("Artículo \"" + titulo + "\" creado correctamente."));
      } else {
        boolean actualizado = blogDAO.actualizar(articulo);
        if (actualizado) {
          guardarFlashYRedirigir(req, resp, ResultadoOperacion.exito("Artículo \"" + titulo + "\" actualizado correctamente."));
        } else {
          guardarFlashYRedirigir(req, resp, ResultadoOperacion.error("No se encontró el artículo a editar."));
        }
      }
    } catch (SQLException e) {
      log("Error guardando articulo de blog", e);
      guardarFlashYRedirigir(req, resp,
              ResultadoOperacion.error("Ocurrió un problema al guardar el artículo. Probá nuevamente."));
    }
  }

  private void eliminar(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String idParam = req.getParameter("id");
    try {
      int id = Integer.parseInt(idParam.trim());
      boolean eliminado = blogDAO.eliminar(id);
      guardarFlashYRedirigir(req, resp, eliminado
              ? ResultadoOperacion.exito("Artículo eliminado correctamente.")
              : ResultadoOperacion.error("No se encontró el artículo a eliminar."));
    } catch (NumberFormatException e) {
      guardarFlashYRedirigir(req, resp, ResultadoOperacion.error("Id de artículo inválido."));
    } catch (SQLException e) {
      log("Error eliminando articulo de blog", e);
      guardarFlashYRedirigir(req, resp, ResultadoOperacion.error("Ocurrió un problema al eliminar el artículo."));
    }
  }

  // Si el slug generado ya existe (para otro articulo distinto al que estamos editando), le agregamos un sufijo numerico incremental hasta encontrar uno libre
  private String asegurarSlugUnico(String slugBase, Integer idAExcluir) throws SQLException {
    String slug = slugBase;
    int sufijo = 2;
    while (blogDAO.existeSlug(slug, idAExcluir)) {
      slug = slugBase + "-" + sufijo;
      sufijo++;
    }
    return slug;
  }

  private void guardarFlashYRedirigir(HttpServletRequest req, HttpServletResponse resp,
                                      ResultadoOperacion resultado) throws IOException {
    HttpSession session = req.getSession();
    session.setAttribute("resultadoAdminBlog", resultado);
    resp.sendRedirect(req.getContextPath() + "/admin/blog");
  }
}
