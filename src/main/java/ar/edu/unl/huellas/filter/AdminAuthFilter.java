package ar.edu.unl.huellas.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// Clase para aplicar un filtro de autenticacion para proteger el path relacionado con el admin, salvo la pantalla de login (ya que es de uso publico)


// Usaamos la anotacion @WebFilter para registrar este filtro automaticamente en el servlet container (sin necesidad de declararlo en web.xml), aplicandolo a toda URL que empiece con "/admin/"
@WebFilter(urlPatterns = {"/admin/*"})
public class AdminAuthFilter implements Filter {

    // Metodo principal del filtro que se va a ejecutar antes de llegar al servlet/controlador en cada petición que coincida con el path "/admin/*"
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        // Realizamos un casteo (o sea conversion) a las peticiones y respuestas genericos a  a sus versiones HTTP (ServletRequest/Response), para que de esta forma se pueda usar metodos específicos como getSession() o sendRedirect()
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Obtengo la ruta de la peticion actual, "/admin/*"
        String path = request.getServletPath();

        // verifico si la ruta obtenida corresponde a la pantalla de login
        boolean esLogin = path.equals("/admin/login");

        // Si es la pantalla de login, no se pide autenticacion, porque el login es de acceso publico (sino nadie podria loguearse)
        if (esLogin) {
            chain.doFilter(req, res); // Permitimos que la petición continue hacia el destino solicitado
            return; // Cortamos la ejecucion para no seguir evaluando el resto
        }

        // Obtenemos la sesión actual del usuario, sin crear una nueva si no existe (el "false" es para evitar crear una sesion vacia cuando el usuario nunca inicio sesion)
        HttpSession session = request.getSession(false);

        // verificamos si la sesion existe y si tiene el rol de admin (mediante el atributo "isAdmin" seteado como true). El uso de "Boolean.TRUE.equals(...)" es para evitar el error NullPointerException en caso de que el atributo no exista)
        boolean autenticado = session != null && Boolean.TRUE.equals(session.getAttribute("isAdmin"));

        // Si el usuario no esta autenticado como admin, se lo redirige a la pantalla de login
        if (!autenticado) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return; // No permitimos que la petición continue hacia el destino solicitado (cortamos ejecucion)
        }

        // Si llegamos hasta aca, es porque el usuario se ah autenticado correstamente como admin, por lo tanto, le permite continuar hacia el recurso solicitado.
        chain.doFilter(req, res);
    }
}