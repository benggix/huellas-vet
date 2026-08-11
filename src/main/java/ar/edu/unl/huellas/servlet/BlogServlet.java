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
import java.util.List;


// Controlador para la seccion blog. Aca vamos a manejar peticiones GET en rutas relacionadas a /blog. Vamos a traer los artículos desde la base de datos la tabla articulos_blog y se los pasamos como atributo de request a nuestra vista blog.jsp.
@WebServlet("/blog")
public class BlogServlet extends HttpServlet {
  private final BlogDAO blogDAO = new BlogDAO();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
    try {
      List<ArticuloBlog> articulos = blogDAO.listarTodos();
      req.setAttribute("articulos", articulos);
    } catch (SQLException e) {
      log("Error obteniendo artículos del blog", e);
      req.setAttribute("articulos", List.of());
    }
    RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/blog.jsp");
    dispatcher.forward(req, resp);
  }
}