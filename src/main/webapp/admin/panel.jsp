<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Panel de administración · Huellas</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
  <main class="container" style="padding:24px 24px 60px;">

    <div class="admin-header">
      <div>
        <span class="eyebrow">Panel interno</span>
        <h1 style="font-size:1.6rem; margin:0;">Huellas · Administración</h1>
      </div>
      <a href="${pageContext.request.contextPath}/admin/logout" class="btn btn--ghost btn--small">Cerrar sesión</a>
    </div>

    <c:if test="${not empty errorCarga}">
      <p class="form-msg is-error">${errorCarga}</p>
    </c:if>

    <!-- turnos -->
    <h2 style="font-size:1.2rem;">Turnos reservados (${fn:length(turnos)})</h2>
    <div class="admin-table-wrap">
      <table class="admin-table">
        <thead>
          <tr>
            <th>Fecha</th><th>Hora</th><th>Servicio</th><th>Mascota</th>
            <th>Especie</th><th>Dueño</th><th>Contacto</th><th>Notas</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${turnos}" var="t">
            <tr>
              <td>${t.fecha}</td>
              <td>${t.hora}</td>
              <td>${t.servicio}</td>
              <td>${t.mascotaNombre}</td>
              <td>${t.especie}</td>
              <td>${t.duenoNombre}</td>
              <td>${t.duenoContacto}</td>
              <td>${t.notas}</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
      <c:if test="${empty turnos}"><p class="admin-empty">Todavía no hay turnos reservados.</p></c:if>
    </div>

    <!-- consultas de contacto ================= -->
    <h2 style="font-size:1.2rem;">Consultas de contacto (${fn:length(consultas)})</h2>
    <div class="admin-table-wrap">
      <table class="admin-table">
        <thead>
          <tr><th>Fecha</th><th>Nombre</th><th>Email</th><th>Asunto</th><th>Mensaje</th></tr>
        </thead>
        <tbody>
          <c:forEach items="${consultas}" var="c">
            <tr>
              <td>${c.creadoEn}</td>
              <td>${c.nombre}</td>
              <td>${c.email}</td>
              <td>${c.asunto}</td>
              <td>${c.mensaje}</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
      <c:if test="${empty consultas}"><p class="admin-empty">Todavía no hay consultas enviadas.</p></c:if>
    </div>

    <!-- suscriptores newsletter -->
    <h2 style="font-size:1.2rem;">Suscriptores de newsletter (${fn:length(suscriptores)})</h2>
    <div class="admin-table-wrap">
      <table class="admin-table">
        <thead><tr><th>Fecha</th><th>Email</th></tr></thead>
        <tbody>
          <c:forEach items="${suscriptores}" var="s">
            <tr><td>${s.creadoEn}</td><td>${s.email}</td></tr>
          </c:forEach>
        </tbody>
      </table>
      <c:if test="${empty suscriptores}"><p class="admin-empty">Todavía no hay suscriptores.</p></c:if>
    </div>

  </main>
</body>
</html>
