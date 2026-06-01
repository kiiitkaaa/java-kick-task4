<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Вход</title></head>
<body>
<jsp:include page="/WEB-INF/pages/header.jsp" />

<h2>Вход в систему</h2>
<p>${requestScope.errorMessage}</p>

<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="LOGIN">
    Логин: <input type="text" name="login" required><br><br>
    Пароль: <input type="password" name="password" required><br><br>
    <button type="submit">Войти</button>
</form>
</body>
</html>
