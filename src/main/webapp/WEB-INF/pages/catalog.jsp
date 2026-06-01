<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Каталог товаров</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .product-card { border: 1px solid #ccc; padding: 15px; margin-bottom: 10px; border-radius: 5px; }
        .price { font-weight: bold; color: green; }
        .nav { margin-bottom: 20px; }
    </style>
</head>
<body>

<div class="nav">
    <a href="${pageContext.request.contextPath}/index.jsp">На главную</a> |
    <a href="${pageContext.request.contextPath}/controller?command=VIEW_MY_ORDERS">Мои заказы</a> |
    <a href="${pageContext.request.contextPath}/controller?command=LOGOUT">Выйти</a>
</div>

<h2>Каталог товаров</h2>

<c:choose>
    <c:when test="${empty products}">
        <p>К сожалению, товаров пока нет.</p>
    </c:when>
    <c:otherwise>
        <c:forEach var="product" items="${products}">
            <div class="product-card">
                <h3>${product.name}</h3>
                <p>${product.description}</p>
                <p class="price">Цена: ${product.price} руб.</p>

                <form action="${pageContext.request.contextPath}/controller" method="post">
                    <input type="hidden" name="command" value="MAKE_ORDER">
                    <input type="hidden" name="productId" value="${product.id}">
                    <button type="submit">Купить</button>
                </form>

                <c:if test="${sessionScope.user != null && sessionScope.user.role == 'ADMIN'}">
                    <form action="${pageContext.request.contextPath}/controller" method="post">
                        <input type="hidden" name="command" value="DELETE_PRODUCT">
                        <input type="hidden" name="productId" value="${product.id}">
                        <button type="submit">Удалить товар</button>
                    </form>
                </c:if>
            </div>
        </c:forEach>
    </c:otherwise>
</c:choose>

</body>
</html>
