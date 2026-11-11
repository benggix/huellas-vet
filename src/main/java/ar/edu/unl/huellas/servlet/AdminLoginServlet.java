package ar.edu.unl.huellas.servlet;

import ar.edu.unl.huellas.util.AdminAuth;
import ar.edu.unl.huellas.util.InputSanitizer;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/* Controlador para manejar peticiones GET y POST en el login del admin, manejando el GET para mostrar el formulario a dicho path y
ademas de validar dichos datos para que coincida con los del archivo admin.properties
*/

@WebServlet("/admin/login")
public class AdminLoginServlet extends HttpServlet {

  // Mostramos el formulario de inicio de sesion al admin
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
    RequestDispatcher dispatcher = req.getRequestDispatcher("/admin/login.jsp");
    dispatcher.forward(req, resp);
  }

  // Procesamos el envio del formulario, validando la informacion recibida y la gestion de la sesion. Si todo sale bien (auth), generamos la sesion con el rol admin y lo redirimos hacia el panel de administracion. Caso contrario volvemos a cargar la pantalla de login con el mensaje de error.
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {

    String usuario = InputSanitizer.limpiar(req.getParameter("usuario"));
    String password = req.getParameter("password"); // no se sanitiza.

    if (AdminAuth.credencialesValidas(usuario, password)) {
      HttpSession session = req.getSession(true);
      req.changeSessionId();
      session.setAttribute("isAdmin", Boolean.TRUE);
      resp.sendRedirect(req.getContextPath() + "/admin/panel");
    } else {
      req.setAttribute("error", "Usuario o contrase\u00f1a incorrectos.");
      RequestDispatcher dispatcher = req.getRequestDispatcher("/admin/login.jsp");
      dispatcher.forward(req, resp);
    }
  }
}
