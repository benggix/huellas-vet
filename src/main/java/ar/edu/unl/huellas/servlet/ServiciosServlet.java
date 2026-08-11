package ar.edu.unl.huellas.servlet;

import ar.edu.unl.huellas.dao.ServicioDAO;
import ar.edu.unl.huellas.model.Servicio;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


// Controlar para la seccion de servicios, aca simplemente vamos a mostrar el listado de servicios que ofrece la clinica cuando el usuario haga una peticion GET, trayendo el listado desde la base de datos de la tabla "servicios" y lo pasamos como atributo de request a nuestra vista servicios.jsp, donde la misma lo va a recorrer con un foreach.
@WebServlet("/servicios")
public class ServiciosServlet extends HttpServlet {

    private final ServicioDAO servicioDAO = new ServicioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<Servicio> servicios = servicioDAO.listarTodos();
            req.setAttribute("servicios", servicios);
        } catch (SQLException e) {
            log("Error obteniendo servicios", e);
            req.setAttribute("servicios", List.of());
        }
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/servicios.jsp");
        dispatcher.forward(req, resp);
    }
}
