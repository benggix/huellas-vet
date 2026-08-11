<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% String currentPage = "turnos"; %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Reservar turno · Huellas Clínica Veterinaria</title>
  <meta name="description" content="Reservá el turno para tu mascota online: elegí servicio, fecha y horario disponible.">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/views/fragments/header.jspf" %>

<main>
  <section class="hero" style="padding-bottom:0;">
    <div class="container">
      <span class="eyebrow">Turnos</span>
      <h1>Reservá el turno de tu mascota.</h1>
      <p class="hero__lede">Completá la ficha. El sistema valida la disponibilidad horaria antes de confirmar tu turno.</p>
    </div>
  </section>

  <section class="section">
    <div class="container" style="max-width:760px;">

      <%-- action que apuntara al Servlet/Controller de turnos (por ejemplo un POST hacia /turnos). Luego, el Controller validara superposicion de horarios contra PostgreSQL (DAO TurnoDAO) y devolvera un mensaje que fue un exito o que fue error en pantalla --%>
      <c:if test="${not empty resultadoTurno}">
        <p class="form-msg ${resultadoTurno.ok ? 'is-ok' : 'is-error'}" role="status">${resultadoTurno.mensaje}</p>
      </c:if>

      <form id="turnosForm" class="form-card form-card--ledger" action="${pageContext.request.contextPath}/turnos" method="post" novalidate>
        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />

        <div class="field-row">
          <div class="field">
            <label for="nombreDueno">Nombre y apellido</label>
            <input type="text" id="nombreDueno" name="nombreDueno" required autocomplete="name" />
            <p class="field__error">Ingresá tu nombre.</p>
          </div>
          <div class="field">
            <label for="contacto">Teléfono o email</label>
            <input type="text" id="contacto" name="contacto" required autocomplete="tel" />
            <p class="field__error">Necesitamos un dato de contacto.</p>
          </div>
        </div>

        <div class="field-row">
          <div class="field">
            <label for="mascota">Nombre de la mascota</label>
            <input type="text" id="mascota" name="mascota" required />
            <p class="field__error">Contanos cómo se llama.</p>
          </div>
          <div class="field">
            <label for="especie">Especie</label>
            <select id="especie" name="especie" required>
              <option value="">Elegir…</option>
              <option value="perro">Perro</option>
              <option value="gato">Gato</option>
              <option value="conejo">Conejo</option>
              <option value="huron">Hurón</option>
              <option value="cobayo">Cobayo</option>
              <option value="otro">Otro</option>
            </select>
            <p class="field__error">Elegí una especie.</p>
          </div>
        </div>

        <div class="field">
          <label for="servicio">Servicio</label>
          <select id="servicio" name="servicio" required>
            <option value="">Elegir…</option>
            <c:forEach items="${servicios}" var="s">
              <option value="${s.codigo}" ${param.servicio == s.codigo ? 'selected' : ''}>${s.titulo}</option>
            </c:forEach>
          </select>
          <p class="field__error">Elegí un servicio.</p>
        </div>

        <div class="field-row">
          <div class="field">
            <label for="fecha">Fecha</label>
            <input type="date" id="fecha" name="fecha" required />
            <p class="field__error">Elegí una fecha válida (no puede ser anterior a hoy).</p>
          </div>
          <div class="field">
            <label for="hora">Horario</label>
            <input type="time" id="hora" name="hora" min="09:00" max="19:00" required />
            <p class="hint">Atendemos de 9 a 19 h (sáb. 9 a 13 h).</p>
            <p class="field__error">Elegí un horario dentro del rango de atención.</p>
          </div>
        </div>

        <div class="field">
          <label for="notas">Notas para el veterinario (opcional)</label>
          <textarea id="notas" name="notas" rows="3" placeholder="Ej: primera vez, alergias, síntomas puntuales…"></textarea>
        </div>

        <button type="submit" class="btn btn--honey" style="width:100%;">Confirmar solicitud de turno</button>
        <p class="form-msg" id="turnosMsg" role="status" aria-live="polite"></p>
      </form>

    </div>
  </section>
</main>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>

</body>
</html>
