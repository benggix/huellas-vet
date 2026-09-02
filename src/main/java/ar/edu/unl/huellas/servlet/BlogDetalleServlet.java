package ar.edu.unl.huellas.servlet;

import ar.edu.unl.huellas.dao.BlogDAO;
import ar.edu.unl.huellas.model.ArticuloBlog;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

// Controlador para la pagina de detalle de un articulo del blog.
@WebServlet("/blog/nota")
public class BlogDetalleServlet extends HttpServlet {

  private final BlogDAO blogDAO = new BlogDAO();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {

    String slug = req.getParameter("slug");
    if (slug == null || slug.isBlank()) {
      resp.sendRedirect(req.getContextPath() + "/blog");
      return;
    }

    try {
      Optional<ArticuloBlog> articulo = blogDAO.obtenerPorSlug(slug);
      if (articulo.isEmpty()) {
        resp.sendRedirect(req.getContextPath() + "/blog");
        return;
      }
      ArticuloBlog a = articulo.get();
      req.setAttribute("articulo", a);
      List<String> parrafos = Arrays.stream(a.getContenido().split("\\n\\s*\\n"))
              .map(String::trim)
              .filter(p -> !p.isEmpty())
              .toList();
      req.setAttribute("parrafos", parrafos);
    } catch (SQLException e) {
      log("Error obteniendo el detalle del articulo de blog", e);
      resp.sendRedirect(req.getContextPath() + "/blog");
      return;
    }

    RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/blog-detalle.jsp");
    dispatcher.forward(req, resp);
  }
}