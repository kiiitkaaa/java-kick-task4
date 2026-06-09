<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
    <title>Добавление товара</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .nav { margin-bottom: 20px; }
    </style>
</head>
<body>

<div class="nav">
    <a href="${pageContext.request.contextPath}/controller?command=VIEW_PRODUCTS">Вернуться в каталог</a> |
    <a href="${pageContext.request.contextPath}/controller?command=LOGOUT">Выйти</a>
</div>

<h2>Добавление нового товара</h2>

<c:if test="${not empty errorMessage}">
    <div style="color: red; margin-bottom: 10px;">${errorMessage}</div>
</c:if>

<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="ADD_PRODUCT">

    <label>Название:</label><br>
    <input type="text" name="name" required style="width: 300px;"><br><br>

    <label>Описание:</label><br>
    <textarea name="description" rows="4" style="width: 300px;"></textarea><br><br>

    <label>Цена:</label><br>
    <input type="number" step="0.01" name="price" required><br><br>

    <button type="submit" style="padding: 5px 15px;">Сохранить товар</button>
</form>

</body>
</html>