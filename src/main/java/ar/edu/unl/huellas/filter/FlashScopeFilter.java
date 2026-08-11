package ar.edu.unl.huellas.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


/* Clase filtro encargado de limpiar los mensajes temporales (Flash Scope) almacenados en la sesion luego de que fueron mostrados al usuario.
 Esta clase se ejecutara para todas las peticiones de nuestra app.
 */
@WebFilter(urlPatterns = {"/*"})
public class FlashScopeFilter implements Filter {

    // Lista de atributos de sesion que van a ser utilizados como mensajes flash. Estos mensajes van a permanecer disponibles hasta la siguiente peticion GET.
    private static final String[] FLASH_KEYS = {
            "resultadoTurno", "resultadoContacto", "resultadoNewsletter"
    };

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        // Permitimos que la solicitud continue hacia el siguiente filtro o hacia el servlet correspondiente.
        chain.doFilter(req, res);

        HttpServletRequest request = (HttpServletRequest) req;

        /* Al terminar una peticion POST, no se eliminan los mensajes sino que los controladores almacenan el mensaje flash en la sesion y luego realizan un redirect.
        Esto es porque si se borrara ahora el mensaje, el navegador no tendria tiempo de mostrarlo en la siguiente peticion GET. Es por eso que la limpieza se hace
        únicamente despues de una solicitud GET, es decir, cuando el mensaje ya fue renderizado en la vista.
         */
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            return;
        }

        // Obtenemos la sesion unicamente si ya existe.
        HttpSession session = request.getSession(false);

        // Eliminamos todos los mensajes flash una vez que ya fueron utilizados.
        if (session != null) {
            for (String key : FLASH_KEYS) {
                session.removeAttribute(key);
            }
        }
    }
}
