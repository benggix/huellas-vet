<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% String currentPage = "servicios"; %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Servicios · Huellas Clínica Veterinaria</title>
  <meta name="description" content="Consultas, cirugías, vacunación, estudios y más. Conocé todos los servicios de Huellas Clínica Veterinaria en La Plata.">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>

<%@ include file="/WEB-INF/views/fragments/header.jspf" %>

<main>
  <section class="hero" style="padding-bottom:0;">
    <div class="container">
      <span class="eyebrow">Servicios</span>
      <h1>Una ficha por cada cuidado que tu mascota necesita.</h1>
      <p class="hero__lede">Pasá el mouse (o tocá en el celular) sobre cada ficha para ver el detalle y reservar directamente.</p>
    </div>
  </section>

  <section class="section">
    <div class="container">
      <%-- Cargo el listado que lo obtenimos del controlador/servlet ServiciosServlet, correspondiente a la tabla "servicios" en PostgreSQL --%>
      <div class="ficha-grid">
        <c:forEach items="${servicios}" var="s" varStatus="st">
          <article class="ficha">
            <span class="ficha__code">FICHA · ${st.index + 1}</span>
            <h3>${s.titulo}</h3>
            <p>${s.descripcion}</p>
            <div class="ficha__reveal">
              <c:if test="${not empty s.detalle}">
                <p style="font-size:0.85rem;margin-bottom:0.6em;">${s.detalle}</p>
              </c:if>
              <a href="${pageContext.request.contextPath}/turnos?servicio=${s.codigo}" class="ficha__link">Reservar turno →</a>
            </div>
          </article>
        </c:forEach>

        <c:if test="${empty servicios}">
          <p>Todavía no hay servicios cargados en la base.</p>
        </c:if>
      </div>
    </div>
  </section>

  <section class="section section--alt">
    <div class="container" style="text-align:center;">
      <h2>¿No sabés qué servicio necesitás?</h2>
      <p style="max-width:50ch;margin:0 auto 1.4em;">Contanos qué le pasa a tu mascota y te ayudamos a elegir el turno adecuado.</p>
      <a href="${pageContext.request.contextPath}/contacto" class="btn btn--honey">Escribinos</a>
    </div>
  </section>
</main>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>

</body>
</html>
