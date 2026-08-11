<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Ingresar · Panel de administración · Huellas</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
  <main class="section" style="min-height:100vh; display:flex; align-items:center;">
    <div class="container" style="max-width:420px;">
      <span class="eyebrow">Panel interno</span>
      <h1 style="font-size:1.8rem;">Acceso de administración</h1>
      <p style="margin-bottom:1.5em;">Ingresá tus credenciales para ver los turnos, consultas y suscriptores cargados.</p>

      <c:if test="${not empty error}">
        <p class="form-msg is-error" role="alert">${error}</p>
      </c:if>

      <form class="form-card" action="${pageContext.request.contextPath}/admin/login" method="post" novalidate>
        <div class="field">
          <label for="usuario">Usuario</label>
          <input type="text" id="usuario" name="usuario" required autocomplete="username" autofocus />
        </div>
        <div class="field">
          <label for="password">Contraseña</label>
          <input type="password" id="password" name="password" required autocomplete="current-password" />
        </div>
        <button type="submit" class="btn btn--honey" style="width:100%;">Ingresar</button>
      </form>

      <p style="margin-top:1.5em;"><a href="${pageContext.request.contextPath}/index.jsp" class="ficha__link">← Volver al sitio</a></p>
    </div>
  </main>
</body>
</html>
