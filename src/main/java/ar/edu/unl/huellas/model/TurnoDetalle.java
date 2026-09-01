package ar.edu.unl.huellas.model;

import java.time.LocalDate;
import java.time.LocalTime;

// Modelo de solo lectura que se usara unicamente para obtener y mostrar el listado completo con informacion detallada de los turnos en el panel de administracion del admin.
public class TurnoDetalle {
  private Integer id;
  private String mascotaNombre;
  private String especie;
  private String duenoNombre;
  private String duenoContacto;
  private String servicio;
  private LocalDate fecha;
  private LocalTime hora;
  private String notas;
  private String estado;

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getMascotaNombre() {
    return mascotaNombre;
  }

  public void setMascotaNombre(String mascotaNombre) {
    this.mascotaNombre = mascotaNombre;
  }

  public String getEspecie() {
    return especie;
  }

  public void setEspecie(String especie) {
    this.especie = especie;
  }

  public String getDuenoNombre() {
    return duenoNombre;
  }

  public void setDuenoNombre(String duenoNombre) {
    this.duenoNombre = duenoNombre;
  }

  public String getDuenoContacto() {
    return duenoContacto;
  }

  public void setDuenoContacto(String duenoContacto) {
    this.duenoContacto = duenoContacto;
  }

  public String getServicio() {
    return servicio;
  }

  public void setServicio(String servicio) {
    this.servicio = servicio;
  }

  public LocalDate getFecha() {
    return fecha;
  }

  public void setFecha(LocalDate fecha) {
    this.fecha = fecha;
  }

  public LocalTime getHora() {
    return hora;
  }

  public void setHora(LocalTime hora) {
    this.hora = hora;
  }

  public String getNotas() {
    return notas;
  }

  public void setNotas(String notas) {
    this.notas = notas;
  }
}