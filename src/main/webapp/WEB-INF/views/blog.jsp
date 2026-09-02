<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% String currentPage = "blog"; %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Blog de cuidado animal · Huellas Clínica Veterinaria</title>
  <meta name="description" content="Consejos breves sobre alimentación, vacunación y señales de alerta para perros, gatos, conejos, hurones y cobayos.">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/views/fragments/header.jspf" %>

<main>
  <section class="hero" style="padding-bottom:0;">
    <div class="container">
      <span class="eyebrow">Blog</span>
      <h1>Consejos breves para el día a día con tu mascota.</h1>
      <p class="hero__lede">Notas cortas, pensadas para leerse en un par de minutos desde el celular.</p>
    </div>
  </section>

  <section class="section">
    <div class="container blog-grid">

      <%-- El listado viene del controlador BlogServlet, correspondiente a la tabla "articulos_blog" en PostgreSQL --%>
      <div class="post-list">
        <c:forEach items="${articulos}" var="a">
          <article class="post-card">
            <div class="post-card__thumb">
              <img src="${pageContext.request.contextPath}${not empty a.imagenUrl ? a.imagenUrl : '/resources/img/placeholder.svg'}"
                   alt="Ilustración de la nota: ${a.titulo}" loading="lazy" width="96" height="96" />
            </div>
            <div>
              <span class="tag">${a.etiqueta}</span>
              <h3>${a.titulo}</h3>
              <p>${a.resumen}</p>
              <a href="${pageContext.request.contextPath}/blog/nota?slug=${a.slug}" class="ficha__link">Leer nota →</a>
            </div>
          </article>
        </c:forEach>

        <c:if test="${empty articulos}">
          <p>Todavía no hay artículos cargados en la base.</p>
        </c:if>
      </div>

      <aside>
        <div class="sidebar-card">
          <h3>Temas</h3>
          <div class="tag-cloud">
            <span class="tag">Perros</span>
            <span class="tag">Gatos</span>
            <span class="tag">Conejos</span>
            <span class="tag">Hurones</span>
            <span class="tag">Cobayos</span>
            <span class="tag">Vacunas</span>
            <span class="tag">Alimentación</span>
          </div>
        </div>

        <div class="sidebar-card" style="margin-top:20px;">
          <h3>Suscribite</h3>
          <p style="font-size:0.9rem;">Recibí un resumen mensual con las notas nuevas del blog.</p>
          <form class="newsletter-inline-form" action="${pageContext.request.contextPath}/suscripcion" method="post" accept-charset="UTF-8" novalidate>
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />
            <label for="blogEmail" class="sr-only">Correo electrónico</label>
            <input type="email" id="blogEmail" name="email" placeholder="tu@correo.com" required
                   style="width:100%;margin-bottom:10px;padding:0.7em 0.9em;border-radius:8px;border:1.5px solid var(--line);" />
            <button type="submit" class="btn btn--honey btn--small" style="width:100%;">Suscribirme</button>
            <c:if test="${not empty resultadoNewsletter}">
              <p class="form-msg ${resultadoNewsletter.ok ? 'is-ok' : 'is-error'}" role="status">${resultadoNewsletter.mensaje}</p>
            </c:if>
            <p class="form-msg" role="status" aria-live="polite"></p>
          </form>
        </div>
      </aside>

    </div>
  </section>
</main>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>

</body>
</html>