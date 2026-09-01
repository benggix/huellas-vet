<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<% String currentPage = "blog"; %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${articulo.titulo} · Blog Huellas Clínica Veterinaria</title>
  <meta name="description" content="${articulo.resumen}">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/views/fragments/header.jspf" %>

<main>
  <section class="hero" style="padding-bottom:0;">
    <div class="container" style="max-width:760px;">
      <span class="eyebrow">Blog · ${articulo.etiqueta}</span>
      <h1>${articulo.titulo}</h1>
      <p class="hero__lede">Publicado el ${articulo.publicadoEn}</p>
    </div>
  </section>

  <section class="section">
    <div class="container" style="max-width:760px;">

      <img class="post-hero__img"
           src="${pageContext.request.contextPath}${not empty articulo.imagenUrl ? articulo.imagenUrl : '/resources/img/placeholder.svg'}"
           alt="Ilustración de la nota: ${articulo.titulo}" />

      <article class="post-body">
        <%-- El contenido se guarda como texto plano con parrafos separados por saltos de linea dobles, asi que los partimos aca para renderizar cada <p> --%>
        <c:forEach items="${fn:split(articulo.contenido, '\n\n')}" var="parrafo">
          <c:if test="${not empty fn:trim(parrafo)}">
            <p>${parrafo}</p>
          </c:if>
        </c:forEach>
      </article>

      <p style="margin-top:2em;">
        <a href="${pageContext.request.contextPath}/blog" class="ficha__link">← Volver al blog</a>
      </p>
    </div>
  </section>
</main>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>

</body>
</html>
