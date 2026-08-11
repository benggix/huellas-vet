package ar.edu.unl.huellas.model;

import java.time.LocalDateTime;


// Modelo del mensaje que va a enviarse en el formulario de contacto
public class ConsultaContacto {

  private Integer id;
  private String nombre;
  private String email;
  private String asunto;
  private String mensaje;
  private LocalDateTime creadoEn;

  public ConsultaContacto() {
  }

  public ConsultaContacto(String nombre, String email, String asunto, String mensaje) {
    this.nombre = nombre;
    this.email = email;
    this.asunto = asunto;
    this.mensaje = mensaje;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAsunto() {
    return asunto;
  }

  public void setAsunto(String asunto) {
    this.asunto = asunto;
  }

  public String getMensaje() {
    return mensaje;
  }

  public void setMensaje(String mensaje) {
    this.mensaje = mensaje;
  }

  public LocalDateTime getCreadoEn() {
    return creadoEn;
  }

  public void setCreadoEn(LocalDateTime creadoEn) {
    this.creadoEn = creadoEn;
  }
}