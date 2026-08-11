package ar.edu.unl.huellas.util;

import java.util.regex.Pattern;

// Clase utilitaria que nos servira para las validaciones sencillas, sera reutilizables para los formularios del sitio web.
public final class ValidationUtil {

  private static final Pattern EMAIL_PATTERN =
          Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

  private ValidationUtil() {
  }

  public static boolean esEmailValido(String email) {
    return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
  }

  public static boolean noVacio(String valor) {
    return valor != null && !valor.trim().isEmpty();
  }
}
