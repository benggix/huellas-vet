<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Gestionar blog · Panel Huellas</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
  <main class="container" style="padding:24px 24px 60px;">

    <div class="admin-header">
      <div>
        <span class="eyebrow">Panel interno</span>
        <h1 style="font-size:1.6rem; margin:0;">Huellas · Blog</h1>
      </div>
      <a href="${pageContext.request.contextPath}/admin/logout" class="btn btn--ghost btn--small">Cerrar sesión</a>
    </div>

    <nav style="display:flex; gap:10px; margin-bottom:1.6em; flex-wrap:wrap;">
      <a href="${pageContext.request.contextPath}/admin/panel" class="btn btn--small btn--ghost">Turnos y consultas</a>
      <a href="${pageContext.request.contextPath}/admin/blog" class="btn btn--small btn--honey">Gestionar blog</a>
      <a href="${pageContext.request.contextPath}/admin/servicios" class="btn btn--small btn--ghost">Gestionar servicios</a>
    </nav>

    <c:if test="${not empty errorCarga}">
      <p class="form-msg is-error">${errorCarga}</p>
    </c:if>
    <c:if test="${not empty resultadoAdminBlog}">
      <p class="form-msg ${resultadoAdminBlog.ok ? 'is-ok' : 'is-error'}" role="status">${resultadoAdminBlog.mensaje}</p>
    </c:if>

    <%-- Formulario de alta/edicion. Si llega ?id= valido, "articuloEditar" viene precargado y el formulario pasa a modo edicion --%>
    <section class="admin-form-card">
      <h2>
        <c:choose>
          <c:when test="${empty articuloEditar}">Nuevo artículo</c:when>
          <c:otherwise>Editar artículo #${articuloEditar.id}</c:otherwise>
        </c:choose>
      </h2>
      <form action="${pageContext.request.contextPath}/admin/blog" method="post" novalidate>
        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />
        <input type="hidden" name="accion" value="guardar" />
        <c:if test="${not empty articuloEditar}">
          <input type="hidden" name="id" value="${articuloEditar.id}" />
        </c:if>

        <div class="field-row">
          <div class="field">
            <label for="etiqueta">Etiqueta</label>
            <input type="text" id="etiqueta" name="etiqueta" required
                   value="${articuloEditar.etiqueta}" placeholder="Perros, Gatos, Conejos…" />
          </div>
          <div class="field">
            <label for="titulo">Título</label>
            <input type="text" id="titulo" name="titulo" required value="${articuloEditar.titulo}" />
          </div>
        </div>

        <div class="field">
          <label for="slug">URL / slug <span class="hint">(opcional — si se deja vacío se genera a partir del título)</span></label>
          <input type="text" id="slug" name="slug" value="${articuloEditar.slug}" placeholder="mi-nueva-nota" />
        </div>

        <div class="field">
          <label for="resumen">Resumen</label>
          <textarea id="resumen" name="resumen" rows="2" required>${articuloEditar.resumen}</textarea>
        </div>

        <div class="field">
          <label for="contenido">Contenido completo <span class="hint">(separá los párrafos dejando una línea en blanco entre ellos)</span></label>
          <textarea id="contenido" name="contenido" rows="10" required>${articuloEditar.contenido}</textarea>
        </div>

        <div class="field">
          <label for="imagenUrl">Imagen <span class="hint">(ruta dentro de /resources/img o URL completa; opcional)</span></label>
          <input type="text" id="imagenUrl" name="imagenUrl" value="${articuloEditar.imagenUrl}" placeholder="/resources/img/blog/mi-imagen.svg" />
        </div>

        <div style="display:flex; gap:10px; margin-top:1em;">
          <button type="submit" class="btn btn--honey">${empty articuloEditar ? 'Publicar artículo' : 'Guardar cambios'}</button>
          <c:if test="${not empty articuloEditar}">
            <a href="${pageContext.request.contextPath}/admin/blog" class="btn btn--ghost">Cancelar edición</a>
          </c:if>
        </div>
      </form>
    </section>

    <!-- listado -->
    <h2 style="font-size:1.2rem;">Artículos publicados (${fn:length(articulos)})</h2>
    <div class="admin-table-wrap">
      <table class="admin-table">
        <thead>
          <tr><th></th><th>Título</th><th>Etiqueta</th><th>Publicado</th><th>Acciones</th></tr>
        </thead>
        <tbody>
          <c:forEach items="${articulos}" var="a">
            <tr>
              <td>
                <div class="admin-list-thumb">
                  <img src="${pageContext.request.contextPath}${not empty a.imagenUrl ? a.imagenUrl : '/resources/img/placeholder.svg'}" alt="" />
                </div>
              </td>
              <td>${a.titulo}</td>
              <td>${a.etiqueta}</td>
              <td>${a.publicadoEn}</td>
              <td>
                <div class="admin-actions">
                  <a href="${pageContext.request.contextPath}/admin/blog?id=${a.id}" class="btn btn--small btn--ghost">Editar</a>
                  <a href="${pageContext.request.contextPath}/blog/nota?slug=${a.slug}" class="btn btn--small btn--ghost" target="_blank" rel="noopener">Ver</a>
                  <form action="${pageContext.request.contextPath}/admin/blog" method="post"
                        onsubmit="return confirm('¿Eliminar el artículo &quot;${a.titulo}&quot;? Esta acción no se puede deshacer.');">
                    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />
                    <input type="hidden" name="accion" value="eliminar" />
                    <input type="hidden" name="id" value="${a.id}" />
                    <button type="submit" class="btn btn--small btn--ghost">Eliminar</button>
                  </form>
                </div>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
      <c:if test="${empty articulos}"><p class="admin-empty">Todavía no hay artículos cargados.</p></c:if>
    </div>

  </main>
</body>
</html>
