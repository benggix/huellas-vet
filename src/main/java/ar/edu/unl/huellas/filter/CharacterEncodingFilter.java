package ar.edu.unl.huellas.filter;

import jakarta.servlet.*;

import java.io.IOException;

/* Clase filtro para fijar la codificacion de caracteres en UTF-8 tanto en la request como en la response antes de que se lea cualquier parametro del formulario.

Es necesario porque cuando un formulario HTML se envia por POST con "application/x-www-form-urlencoded" (o incluso al leer parametros de la query string en un GET),
el contenedor de servlets necesita saber con que charset debe decodificar esos bytes. Si nadie llama a request.setCharacterEncoding(...) antes del primer
req.getParameter(...), el contenedor usa por defecto ISO-8859-1. Como el navegador en realidad envia los bytes en UTF-8, los caracteres multibyte
como las tildes o la "ñ" quedan mal interpretados, o sea que cada caracter UTF-8 de 2 bytes se lee como 2 caracteres Latin-1 distintos.

Este filtro se va a registrar explicitamente en web.xml (en vez de usar la anotacion @WebFilter) para que de esta forma haya garantia de que se ejecute antes
que CsrfFilter y FlashScopeFilter. Como aprendimos en la UNL, segun la especificacion de Servlets, los filtros declarados en web.xml se invocan primero en el orden
en que aparecen, y recien despues se invocan los filtros que solo usan anotaciones. Como CsrfFilter lee request.getParameter("csrfToken") en los POST, si el charset
no esta seteado antes de esa lectura, ya es demasiado tarde para corregirlo justamente los parametros quedan cacheados/parseados con el charset incorrecto.
*/
public class CharacterEncodingFilter implements Filter {

    private static final String ENCODING = "UTF-8";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Solo tiene sentido si todavia no se leyo ningun parametro, por eso este filtro va primero en la cadena
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(ENCODING);
        }

        // Tambien fijamos el charset de salida por las dudas
        response.setCharacterEncoding(ENCODING);

        chain.doFilter(request, response);
    }
}