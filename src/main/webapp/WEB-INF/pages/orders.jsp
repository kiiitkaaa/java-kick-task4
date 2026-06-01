<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Мои заказы</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #aaa; padding: 10px; text-align: left; }
        th { background-color: #f4f4f4; }
        .nav { margin-bottom: 20px; }
    </style>
</head>
<body>

<div class="nav">
    <a href="${pageContext.request.contextPath}/controller?command=VIEW_PRODUCTS">Вернуться в каталог</a> |
    <a href="${pageContext.request.contextPath}/controller?command=LOGOUT">Выйти</a>
</div>

<h2>Мои заказы</h2>

<c:choose>
    <c:when test="${empty orders}">
        <p>У вас пока нет заказов.</p>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
            <tr>
                <th>№ Заказа</th>
                <th>ID Товара</th>
                <th>Статус</th>
                <th>Действие</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="order" items="${orders}">
                <tr>
                    <td>${order.id}</td>
                    <td>${order.product.id}</td>
                    <td>${order.status}</td>
                    <td>
                        <form action="${pageContext.request.contextPath}/controller" method="post" style="margin: 0;">
                            <input type="hidden" name="command" value="CANCEL_ORDER">
                            <input type="hidden" name="orderId" value="${order.id}">
                            <button type="submit">Отменить</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>

</body>
</html>
