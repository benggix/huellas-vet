package ar.edu.unl.huellas.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

// Clase utilitaria para generar "slugs", vendria ser unos identificadores legibles para usar en la URL, por ejemplo "/blog/nota?slug=cuidados-basicos-hurones" a partir de un texto libre como el titulo de un articulo.
public final class SlugUtil {

    private static final Pattern NO_ALFANUMERICO = Pattern.compile("[^a-z0-9]+");
    private static final Pattern GUIONES_REPETIDOS = Pattern.compile("-{2,}");

    private SlugUtil() {
    }

    // metodo para normaliza el texto, o sea se sacan los acentos, se pasa a minusculas, se reemplaza espacios y simbolos por guiones para obtener un slug limpio y apto para URL.
    public static String generar(String texto) {
        if (texto == null) {
            return "";
        }
        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", ""); // saca las tildes/diacriticos
        String slug = normalizado.toLowerCase()
                .trim();
        slug = NO_ALFANUMERICO.matcher(slug).replaceAll("-");
        slug = GUIONES_REPETIDOS.matcher(slug).replaceAll("-");
        slug = slug.replaceAll("^-|-$", "");
        return slug;
    }
}