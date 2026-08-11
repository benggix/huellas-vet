package ar.edu.unl.huellas.model;

import java.time.LocalDateTime;

// Modelo para la subscripcion de la newsletter mensual
public class Suscriptor {
  private Integer id;
  private String email;
  private LocalDateTime creadoEn;

  public Suscriptor() {
  }

  public Suscriptor(String email) {
    this.email = email;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public LocalDateTime getCreadoEn() {
    return creadoEn;
  }

  public void setCreadoEn(LocalDateTime creadoEn) {
    this.creadoEn = creadoEn;
  }
}