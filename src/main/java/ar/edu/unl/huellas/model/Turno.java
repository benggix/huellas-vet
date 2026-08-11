package ar.edu.unl.huellas.model;

import java.time.LocalDate;
import java.time.LocalTime;


// Modelo para la reserva de un turno. Estara relacionada con la mascota a traves de mascotaId que es la clave foránea hacia la tabla mascotas, y evitar asi que se repita en cada turno los datos de la mascota y su dueño.
public class Turno {

  private Integer id;
  private Integer mascotaId;
  private String servicio;
  private LocalDate fecha;
  private LocalTime hora;
  private String notas;

  public Turno() {
  }
  
  public Turno(Integer mascotaId, String servicio, LocalDate fecha, LocalTime hora, String notas) {
    this.mascotaId = mascotaId;
    this.servicio = servicio;
    this.fecha = fecha;
    this.hora = hora;
    this.notas = notas;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getMascotaId() {
    return mascotaId;
  }

  public void setMascotaId(Integer mascotaId) {
    this.mascotaId = mascotaId;
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