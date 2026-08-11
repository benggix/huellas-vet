package ar.edu.unl.huellas.model;

import java.time.LocalDate;

// Modelo para un artuculo de nuestro blog
public class ArticuloBlog {

  private Integer id;
  private String etiqueta;
  private String titulo;
  private String resumen;
  private LocalDate publicadoEn;

  public ArticuloBlog() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getEtiqueta() {
    return etiqueta;
  }

  public void setEtiqueta(String etiqueta) {
    this.etiqueta = etiqueta;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getResumen() {
    return resumen;
  }

  public void setResumen(String resumen) {
    this.resumen = resumen;
  }

  public LocalDate getPublicadoEn() {
    return publicadoEn;
  }

  public void setPublicadoEn(LocalDate publicadoEn) {
    this.publicadoEn = publicadoEn;
  }
}