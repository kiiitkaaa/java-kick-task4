<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Редактирование профиля</title></head>
<body>
<jsp:include page="/WEB-INF/pages/header.jsp" />

<h2>Настройки профиля (${sessionScope.user.login})</h2>
<p>${requestScope.successMessage}</p>

<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="EDIT_PROFILE">
    Новый пароль: <input type="password" name="newPassword" required><br><br>
    <button type="submit">Сохранить изменения</button>
</form>
</body>
</html>
