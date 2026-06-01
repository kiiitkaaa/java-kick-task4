<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Page Not Found - 404</title>
</head>
<body>
<div>404</div>
<h2>Страница не найдена</h2>
<p>Запрошенный ресурс <strong>${pageContext.errorData.requestURI}</strong> не существует.</p>
<a href="${pageContext.request.contextPath}/">Вернуться на главную</a>
</body>
</html>
