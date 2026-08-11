<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% String currentPage = "contacto"; %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Contacto · Huellas Clínica Veterinaria</title>
  <meta name="description" content="Escribinos tu consulta, encontrá nuestra ubicación en La Plata y seguinos en redes sociales.">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/views/fragments/header.jspf" %>

<main>
  <section class="hero" style="padding-bottom:0;">
    <div class="container">
      <span class="eyebrow">Contacto</span>
      <h1>¿Tenés una duda? Escribinos.</h1>
      <p class="hero__lede">Respondemos consultas generales por este formulario, o por WhatsApp si preferís algo más rápido.</p>
    </div>
  </section>

  <section class="section">
    <div class="container contact-grid">

      <div>
        <%-- action que apuntara al Servlet/Controller de contacto (por ejemplo, un POST hacia /contacto) El token CSRF se validara en el servidor antes de persistir el mensaje--%>
        <c:if test="${not empty resultadoContacto}">
          <p class="form-msg ${resultadoContacto.ok ? 'is-ok' : 'is-error'}" role="status">${resultadoContacto.mensaje}</p>
        </c:if>

        <form id="contactoForm" class="form-card" action="${pageContext.request.contextPath}/contacto" method="post" novalidate>
          <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />

          <div class="field">
            <label for="nombre">Nombre</label>
            <input type="text" id="nombre" name="nombre" required autocomplete="name" />
            <p class="field__error">Ingresá tu nombre.</p>
          </div>

          <div class="field">
            <label for="email">Correo electrónico</label>
            <input type="email" id="email" name="email" required autocomplete="email" />
            <p class="field__error">Ingresá un correo electrónico válido.</p>
          </div>

          <div class="field">
            <label for="asunto">Asunto (opcional)</label>
            <input type="text" id="asunto" name="asunto" />
          </div>

          <div class="field">
            <label for="mensaje">Mensaje</label>
            <textarea id="mensaje" name="mensaje" rows="5" required></textarea>
            <p class="field__error">Contanos brevemente tu consulta.</p>
          </div>

          <button type="submit" class="btn btn--honey" style="width:100%;">Enviar consulta</button>
          <p class="form-msg" id="contactoMsg" role="status" aria-live="polite"></p>
        </form>
      </div>

      <div>
        <h2 style="font-size:1.5rem;">Clínica Veterinaria Huellas</h2>
        <ul class="contact-info-list">
          <li>
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" style="flex-shrink:0;color:var(--honey-dark);"><path d="M12 21s7-6.2 7-11.5A7 7 0 1 0 5 9.5C5 14.8 12 21 12 21z" stroke="currentColor" stroke-width="1.6"/><circle cx="12" cy="9.5" r="2.4" stroke="currentColor" stroke-width="1.6"/></svg>
            <span>Calle 47 n.º 620, La Plata, Buenos Aires</span>
          </li>
          <li>
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" style="flex-shrink:0;color:var(--honey-dark);"><circle cx="12" cy="12" r="9" stroke="currentColor" stroke-width="1.6"/><path d="M12 7v5l3 2" stroke="currentColor" stroke-width="1.6" stroke-linecap="round"/></svg>
            <span>Lun a Vie 9–19 h · Sáb 9–13 h</span>
          </li>
          <li>
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" style="flex-shrink:0;color:var(--honey-dark);"><path d="M4 5h16v14H4V5z" stroke="currentColor" stroke-width="1.6"/><path d="M4 6l8 7 8-7" stroke="currentColor" stroke-width="1.6"/></svg>
            <span>contacto@huellasvet.com.ar</span>
          </li>
          <li>
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" style="flex-shrink:0;color:var(--honey-dark);"><path d="M6.6 10.8a15 15 0 0 0 6.6 6.6l2.2-2.2c.3-.3.7-.4 1-.2 1.1.4 2.3.6 3.6.6.6 0 1 .4 1 1V20c0 .6-.4 1-1 1C10.6 21 3 13.4 3 4c0-.6.4-1 1-1h3.5c.6 0 1 .4 1 1 0 1.3.2 2.5.6 3.6.1.4 0 .8-.2 1L6.6 10.8z" stroke="currentColor" stroke-width="1.4"/></svg>
            <span>(0221) 555-0148</span>
          </li>
        </ul>

        <div class="map-frame">
          <iframe
            title="Ubicación de Huellas Clínica Veterinaria en La Plata"
            src="https://www.google.com/maps?q=La+Plata,+Buenos+Aires,+Argentina&output=embed"
            loading="lazy"
            referrerpolicy="no-referrer-when-downgrade">
          </iframe>
        </div>
      </div>

    </div>
  </section>
</main>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>

</body>
</html>
