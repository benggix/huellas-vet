package ar.edu.unl.huellas.model;


// Clase modelo para devolver a la vista JSP un mensaje despues de que el resultado de una op sea existosa, como un turno reservado, consulta enviada o suscripción realizada.
public record ResultadoOperacion(boolean ok, String mensaje) {

  public boolean isOk() {
    return ok();
  }

  public String getMensaje() {
    return mensaje();
  }

  public static ResultadoOperacion exito(String mensaje) {
    return new ResultadoOperacion(true, mensaje);
  }

  public static ResultadoOperacion error(String mensaje) {
    return new ResultadoOperacion(false, mensaje);
  }
}