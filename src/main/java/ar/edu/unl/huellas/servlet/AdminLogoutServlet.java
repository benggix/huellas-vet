package ar.edu.unl.huellas.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// Controlador para manejar peticion GET cuando se cierra la sesion del admin
@WebServlet("/admin/logout")
public class AdminLogoutServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    if (session != null) {
      session.removeAttribute("isAdmin");
    }
    resp.sendRedirect(req.getContextPath() + "/admin/login");
  }
}
