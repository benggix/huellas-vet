<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% String currentPage = "inicio"; %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Huellas · Clínica Veterinaria en La Plata</title>
  <meta name="description" content="Clínica veterinaria en La Plata: reservá turnos online, conocé nuestros servicios y leé consejos de cuidado animal.">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/views/fragments/header.jspf" %>

<main>

  <section class="hero">
    <div class="container hero__inner">
      <div class="hero__copy">
        <span class="eyebrow">La Plata · Tolosa · Los Hornos · City Bell · Gonnet</span>
        <h1>Cuidamos a quienes no pueden pedirlo con palabras.</h1>
        <p class="hero__lede">Turnos online, consultas, cirugías y vacunación para perros, gatos, conejos, hurones y cobayos. Reservá tu turno desde el celular en menos de dos minutos.</p>
        <div class="hero__actions">
          <a href="${pageContext.request.contextPath}/turnos" class="btn btn--honey">Reservar turno</a>
          <a href="${pageContext.request.contextPath}/contacto" class="btn btn--ghost">Hacer una consulta</a>
        </div>
      </div>

      <div class="hero__aside">
        <div class="ledger-card">
          <h3>Ficha de atención</h3>
          <dl>
            <dt>Horario</dt><dd>Lun a Vie · 9 a 19 h</dd>
            <dt>Sábados</dt><dd>9 a 13 h</dd>
            <dt>Urgencias</dt><dd>(0221) 555-0148</dd>
            <dt>Zona</dt><dd>La Plata y alrededores</dd>
          </dl>
          <span class="stamp stamp--hero" aria-hidden="true">
            <svg viewBox="0 0 60 60" width="64" height="64"><circle cx="30" cy="30" r="27" fill="none" stroke="currentColor" stroke-width="2"/><ellipse cx="30" cy="35" rx="11" ry="8.5" fill="currentColor"/><ellipse cx="18" cy="21" rx="4.3" ry="5.5" fill="currentColor"/><ellipse cx="42" cy="21" rx="4.3" ry="5.5" fill="currentColor"/><ellipse cx="20.5" cy="29" rx="3.8" ry="4.8" fill="currentColor"/><ellipse cx="39.5" cy="29" rx="3.8" ry="4.8" fill="currentColor"/></svg>
          </span>
        </div>
      </div>
    </div>
  </section>

  <section class="section">
    <div class="container">
      <div class="section__head">
        <span class="eyebrow">Accesos rápidos</span>
        <h2>Todo lo que necesitás, a un toque.</h2>
      </div>
      <div class="quick-grid">
        <a href="${pageContext.request.contextPath}/turnos" class="quick-card">
          <span class="quick-card__icon" aria-hidden="true">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none"><rect x="3" y="4" width="18" height="17" rx="2" stroke="currentColor" stroke-width="1.6"/><path d="M3 9h18M8 2v4M16 2v4" stroke="currentColor" stroke-width="1.6" stroke-linecap="round"/></svg>
          </span>
          <h3>Reservar turno</h3>
          <p>Elegí día, horario y servicio según disponibilidad real del veterinario.</p>
        </a>
        <a href="${pageContext.request.contextPath}/contacto" class="quick-card">
          <span class="quick-card__icon" aria-hidden="true">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none"><path d="M4 5h16v11H7l-3 3V5z" stroke="currentColor" stroke-width="1.6" stroke-linejoin="round"/></svg>
          </span>
          <h3>Hacer una consulta</h3>
          <p>Escribinos por el formulario de contacto o por WhatsApp.</p>
        </a>
        <a href="${pageContext.request.contextPath}/blog" class="quick-card">
          <span class="quick-card__icon" aria-hidden="true">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none"><path d="M5 4h11l3 3v13H5V4z" stroke="currentColor" stroke-width="1.6" stroke-linejoin="round"/><path d="M9 10h6M9 14h6" stroke="currentColor" stroke-width="1.6" stroke-linecap="round"/></svg>
          </span>
          <h3>Consejos de cuidado</h3>
          <p>Notas breves sobre alimentación, vacunas y señales de alerta.</p>
        </a>
      </div>
    </div>
  </section>

  <section class="section section--alt">
    <div class="container">
      <div class="section__head">
        <span class="eyebrow">Servicios destacados</span>
        <h2>Atención veterinaria completa, cerca tuyo.</h2>
      </div>
      <div class="ficha-grid">
        <article class="ficha">
          <span class="ficha__code">FICHA · 01</span>
          <h3>Consultas clínicas</h3>
          <p>Control general, diagnóstico y seguimiento para todo tipo de mascotas.</p>
          <div class="ficha__reveal">
            <a href="${pageContext.request.contextPath}/turnos" class="ficha__link">Reservar consulta →</a>
          </div>
        </article>
        <article class="ficha">
          <span class="ficha__code">FICHA · 02</span>
          <h3>Vacunación</h3>
          <p>Calendario completo de vacunas según especie y edad del animal.</p>
          <div class="ficha__reveal">
            <a href="${pageContext.request.contextPath}/turnos" class="ficha__link">Reservar vacuna →</a>
          </div>
        </article>
        <article class="ficha">
          <span class="ficha__code">FICHA · 03</span>
          <h3>Cirugías</h3>
          <p>Castraciones y cirugías programadas con seguimiento post-operatorio.</p>
          <div class="ficha__reveal">
            <a href="${pageContext.request.contextPath}/turnos" class="ficha__link">Consultar disponibilidad →</a>
          </div>
        </article>
      </div>
      <p style="margin-top:2em">
        <a href="${pageContext.request.contextPath}/servicios" class="btn btn--ghost">Ver todos los servicios</a>
      </p>
    </div>
  </section>

</main>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>

</body>
</html>
