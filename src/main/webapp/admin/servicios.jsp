<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Gestionar servicios · Panel Huellas</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
  <main class="container" style="padding:24px 24px 60px;">

    <div class="admin-header">
      <div>
        <span class="eyebrow">Panel interno</span>
        <h1 style="font-size:1.6rem; margin:0;">Huellas · Servicios</h1>
      </div>
      <a href="${pageContext.request.contextPath}/admin/logout" class="btn btn--ghost btn--small">Cerrar sesión</a>
    </div>

    <nav style="display:flex; gap:10px; margin-bottom:1.6em; flex-wrap:wrap;">
      <a href="${pageContext.request.contextPath}/admin/panel" class="btn btn--small btn--ghost">Turnos y consultas</a>
      <a href="${pageContext.request.contextPath}/admin/blog" class="btn btn--small btn--ghost">Gestionar blog</a>
      <a href="${pageContext.request.contextPath}/admin/servicios" class="btn btn--small btn--honey">Gestionar servicios</a>
    </nav>

    <c:if test="${not empty errorCarga}">
      <p class="form-msg is-error">${errorCarga}</p>
    </c:if>
    <c:if test="${not empty resultadoAdminServicio}">
      <p class="form-msg ${resultadoAdminServicio.ok ? 'is-ok' : 'is-error'}" role="status">${resultadoAdminServicio.mensaje}</p>
    </c:if>

    <%-- Formulario de alta/edicion. Si llega ?id= valido, "servicioEditar" viene precargado y el formulario pasa a modo edicion --%>
    <section class="admin-form-card">
      <h2>
        <c:choose>
          <c:when test="${empty servicioEditar}">Nuevo servicio</c:when>
          <c:otherwise>Editar servicio #${servicioEditar.id}</c:otherwise>
        </c:choose>
      </h2>
      <form action="${pageContext.request.contextPath}/admin/servicios" method="post" novalidate>
        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />
        <input type="hidden" name="accion" value="guardar" />
        <c:if test="${not empty servicioEditar}">
          <input type="hidden" name="id" value="${servicioEditar.id}" />
        </c:if>

        <div class="field-row">
          <div class="field">
            <label for="titulo">Título</label>
            <input type="text" id="titulo" name="titulo" required value="${servicioEditar.titulo}" />
          </div>
          <div class="field">
            <label for="codigo">Código <span class="hint">(opcional — se usa en la URL de turnos; si se deja vacío se genera del título)</span></label>
            <input type="text" id="codigo" name="codigo" value="${servicioEditar.codigo}" placeholder="consulta" />
          </div>
        </div>

        <div class="field">
          <label for="descripcion">Descripción</label>
          <textarea id="descripcion" name="descripcion" rows="3" required>${servicioEditar.descripcion}</textarea>
        </div>

        <div class="field">
          <label for="detalle">Detalle breve <span class="hint">(se muestra al pasar el mouse por la ficha; opcional)</span></label>
          <input type="text" id="detalle" name="detalle" value="${servicioEditar.detalle}" />
        </div>

        <div class="field-row">
          <div class="field">
            <label for="imagenUrl">URL</label>
            <input type="text" id="imagenUrl" name="imagenUrl" value="${servicioEditar.imagenUrl}" placeholder="https://ejemplo.com/imagen.jpg" />
          </div>
          <div class="field">
            <label for="orden">Orden</label>
            <input type="number" id="orden" name="orden" value="${empty servicioEditar ? 0 : servicioEditar.orden}" min="0" />
          </div>
        </div>

        <div style="display:flex; gap:10px; margin-top:1em;">
          <button type="submit" class="btn btn--honey">
            <c:choose>
              <c:when test="${empty servicioEditar}">Crear servicio</c:when>
              <c:otherwise>Guardar cambios</c:otherwise>
            </c:choose>
          </button>
          <c:if test="${not empty servicioEditar}">
            <a href="${pageContext.request.contextPath}/admin/servicios" class="btn btn--ghost">Cancelar edición</a>
          </c:if>
        </div>
      </form>
    </section>

    <!-- listado -->
    <h2 style="font-size:1.2rem;">Servicios activos (${fn:length(servicios)})</h2>
    <div class="admin-table-wrap">
      <table class="admin-table">
        <thead>
          <tr><th></th><th>Título</th><th>Código</th><th>Orden</th><th>Acciones</th></tr>
        </thead>
        <tbody>
          <c:forEach items="${servicios}" var="s">
            <tr>
              <td>
                <div class="admin-list-thumb">
                  <img src="${pageContext.request.contextPath}${not empty s.imagenUrl ? s.imagenUrl : '/resources/img/placeholder.svg'}" alt="" />
                </div>
              </td>
              <td>${s.titulo}</td>
              <td>${s.codigo}</td>
              <td>${s.orden}</td>
              <td>
                <div class="admin-actions">
                  <a href="${pageContext.request.contextPath}/admin/servicios?id=${s.id}" class="btn btn--small btn--ghost">Editar</a>
                  <form action="${pageContext.request.contextPath}/admin/servicios" method="post"
                        onsubmit="return confirm('¿Eliminar el servicio &quot;${s.titulo}&quot;? Esta acción no se puede deshacer.');">
                    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />
                    <input type="hidden" name="accion" value="eliminar" />
                    <input type="hidden" name="id" value="${s.id}" />
                    <button type="submit" class="btn btn--small btn--ghost">Eliminar</button>
                  </form>
                </div>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
      <c:if test="${empty servicios}"><p class="admin-empty">Todavía no hay servicios cargados.</p></c:if>
    </div>

  </main>
</body>
</html>