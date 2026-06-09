<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Настройки профиля</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .form-container { max-width: 400px; padding: 20px; border: 1px solid #ccc; border-radius: 5px; }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 5px; }
        .form-group input { width: 100%; padding: 8px; box-sizing: border-box; }
        .error { color: red; margin-bottom: 15px; }
        .success { color: green; margin-bottom: 15px; }
    </style>
</head>
<body>

<div style="margin-bottom: 20px;">
    <a href="${pageContext.request.contextPath}/controller?command=VIEW_PRODUCTS">Каталог</a> |
    <a href="${pageContext.request.contextPath}/controller?command=VIEW_MY_ORDERS">Мои заказы</a>
</div>

<h2>Настройки профиля</h2>

<div class="form-container">
    <h3>Смена пароля</h3>

    <c:if test="${not empty errorMessage}">
        <div class="error">${errorMessage}</div>
    </c:if>
    <c:if test="${not empty successMessage}">
        <div class="success">${successMessage}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/controller" method="post">
        <input type="hidden" name="command" value="UPDATE_PASSWORD">

        <div class="form-group">
            <label for="newPassword">Новый пароль:</label>
            <input type="password" id="newPassword" name="newPassword" required>
        </div>

        <div class="form-group">
            <label for="confirmPassword">Подтвердите пароль:</label>
            <input type="password" id="confirmPassword" name="confirmPassword" required>
        </div>

        <button type="submit">Сохранить изменения</button>
    </form>
</div>

</body>
</html>
