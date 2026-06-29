<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty sessionScope.user}">
    <c:redirect url="/controller?command=VIEW_PRODUCTS"/>
</c:if>

<html>
<head>
    <title>Регистрация</title>
    <style>
        body { font-family: Arial, sans-serif; display: flex; justify-content: center; margin-top: 50px; }
        .form-container { width: 300px; padding: 20px; border: 1px solid #ccc; border-radius: 5px; background-color: #f9f9f9; }
        .error { color: red; margin-bottom: 15px; font-weight: bold; font-size: 14px; text-align: center; } /* Стиль для ошибок */
        input { width: 100%; margin-bottom: 15px; padding: 8px; box-sizing: border-box; }
        button { width: 100%; padding: 10px; background-color: #007bff; color: white; border: none; border-radius: 3px; cursor: pointer; }
        button:hover { background-color: #0056b3; }
    </style>

    <script>
        function validateForm() {
            var pass = document.getElementById("password").value;
            var passConfirm = document.getElementById("confirmPassword").value;
            var jsErrorDiv = document.getElementById("js-error");

            if (pass !== passConfirm) {
                jsErrorDiv.innerText = "Пароли не совпадают!";
                return false; // Отмена отправки формы
            }

            if (pass.length < 6) {
                jsErrorDiv.innerText = "Пароль должен быть не менее 6 символов!";
                return false;
            }

            jsErrorDiv.innerText = "";
            return true;
        }
    </script>
</head>
<body>

<div class="form-container">
    <h2 style="text-align: center;">Регистрация</h2>
    <div id="js-error" class="error"></div>

    <c:if test="${not empty errorMessage}">
        <div class="error">${errorMessage}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/controller" method="post" onsubmit="return validateForm()">
        <input type="hidden" name="command" value="SIGN_UP">

        <label>Логин:</label>
        <input type="text" name="login" required>

        <label for="password">Пароль:</label>
        <input type="password" id="password" name="password" minlength="6" required>

        <label for="confirmPassword">Повторите пароль:</label>
        <input type="password" id="confirmPassword" name="confirmPassword" required>

        <button type="submit">Зарегистрироваться</button>
    </form>

    <div style="margin-top: 15px; text-align: center;">
        Уже есть аккаунт? <a href="${pageContext.request.contextPath}/index.jsp">Войти</a>
    </div>
</div>

</body>
</html>