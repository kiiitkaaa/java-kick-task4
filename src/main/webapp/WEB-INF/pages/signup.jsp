<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Регистрация</title>
</head>
<body>
<jsp:include page="/WEB-INF/pages/header.jsp" />

<h2>Регистрация нового пользователя</h2>

<p>${requestScope.errorMessage}</p>

<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="SIGN_UP">

    <table>
        <tr>
            <td>Логин:</td>
            <td><input type="text" name="login" required minlength="3" maxlength="50"></td>
        </tr>
        <tr>
            <td>Пароль:</td>
            <td><input type="password" name="password" required minlength="4"></td>
        </tr>
        <tr>
            <td>Повторите пароль:</td>
            <td><input type="password" name="confirmPassword" required minlength="4"></td>
        </tr>
        <tr>
            <td colspan="2">
                <button type="submit">Зарегистрироваться</button>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
