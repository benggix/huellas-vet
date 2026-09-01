package ar.edu.unl.huellas.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

/* Clase que implementa protección contra ataques CSRF (Cross-Site Request Forgery). Tiene 2 funciones importantes:
    - Como generacion de un token, es decir, intercepta cualquier peticion web y asegura que la sesion del usuario tenga asignado en el atributo "csrfToken".
      Si no tiene un token lo genera y los JSP van a estar leyendo dicho token con "${sessionScope.csrfToken}" colocandolo en un input oculto,
    - Como validador, es decir, para todas las peticiones HTTP de tipo Post dirigidas a rutas como /turnos, /contacto y /suscripcion, se exige que el parámetro
      recibido coincida exactamente con el token guardado en la sesión. Si no es valido, se rechaza la solicitud.
*/

@WebFilter(urlPatterns = {"/*"})
public class CsrfFilter implements Filter {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String SESSION_ATTR = "csrfToken";

    // Rutas que modifican o persisten datos y que, por lo tanto requieren una validacion con el token
    private static final String[] RUTAS_PROTEGIDAS = {
            "/turnos", "/contacto", "/suscripcion",
            "/admin/turnos/estado", "/admin/blog", "/admin/servicios"
    };

    /* Metodo para interceptar las peticiones HTTP para poder garantizar la extistencia del token y validar su autencidiad en las operaciones graves.
        -> @throws ServletException: Es para saber si ocurre un error interno en la cadena de filtros.
        -> @throws IOException: Es para saber si ocurre un error de entrada/salida al procesar la peticion o respuesta.
    */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(true);

        // Verificar que exista siempre un token en la sesion del usuario
        if (session.getAttribute(SESSION_ATTR) == null) {
            session.setAttribute(SESSION_ATTR, generarToken());
        }
        // Validamos el token, solamente si se intenta enviar informacion (POST) a una ruta protegida
        if ("POST".equalsIgnoreCase(request.getMethod()) && esRutaProtegida(request)) {
            String tokenSesion = (String) session.getAttribute(SESSION_ATTR);
            String tokenForm = request.getParameter("csrfToken");

            if (tokenSesion == null || !tokenSesion.equals(tokenForm)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Token de seguridad inválido o expirado. Volvé a cargar el formulario.");
                return;
            }
        }

        chain.doFilter(req, res);
    }

    // Metodo para comprobar si el path de la peticion corresponde a una de las rutas sensibles del sistema
    private boolean esRutaProtegida(HttpServletRequest request) {
        String path = request.getServletPath();
        for (String ruta : RUTAS_PROTEGIDAS) {
            if (ruta.equals(path)) {
                return true;
            }
        }
        return false;
    }

    // Generamos un token random seguro de 32 bytes codificado en Base64 URL-safe.
    private String generarToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}