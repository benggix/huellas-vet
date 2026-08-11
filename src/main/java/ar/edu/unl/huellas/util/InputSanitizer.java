package ar.edu.unl.huellas.util;

// Clase utilitaria de seguridad que servira para la sanitizacion de entradas de texto, con el objetivo de escapar caracteres HTML que puedan ser peligrosos en las entradas de los formularios, es decir, evitar que un usuario malicioso inyecte scripts (XSS) que despues se rendericen en el blog, panel de turnos, etc. Se podra aplicar sobre cualquier texto libre que provenga del usuario antes de guardarlo o de mostrarlo (como nombre, mensaje, notas del turno, etc.).
public final class InputSanitizer {

  private InputSanitizer() {
  }

  public static String limpiar(String valor) {
    if (valor == null) {
      return null;
    }
    return valor
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#x27;")
            .trim();
  }
}
