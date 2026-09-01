package ar.edu.unl.huellas.model;

import java.time.LocalDate;

// Modelo para un artuculo de nuestro blog
public class ArticuloBlog {

  private Integer id;
  private String slug;
  private String etiqueta;
  private String titulo;
  private String resumen;
  private String contenido;
  private String imagenUrl;
  private LocalDate publicadoEn;

  public ArticuloBlog() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public String getContenido() {
    return contenido;
  }

  public void setContenido(String contenido) {
    this.contenido = contenido;
  }

  public String getImagenUrl() {
    return imagenUrl;
  }

  public void setImagenUrl(String imagenUrl) {
    this.imagenUrl = imagenUrl;
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