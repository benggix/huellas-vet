package ar.edu.unl.huellas.model;


// Modelo Mascota que se guardara en el sistema. Cuando se realice un turno, cada turno tendra como referencia a una mascota, sera una relación de 1 a muchos, porque una mascota puede tener varios turnos a lo largo del tiempo.
public class Mascota {
  private Integer id;
  private String nombre;
  private String especie;
  private String duenoNombre;
  private String duenoContacto;

  public Mascota() {
  }

  public Mascota(String nombre, String especie, String duenoNombre, String duenoContacto) {
    this.nombre = nombre;
    this.especie = especie;
    this.duenoNombre = duenoNombre;
    this.duenoContacto = duenoContacto;
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
}