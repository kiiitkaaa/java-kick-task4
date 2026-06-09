<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Каталог товаров</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .product-card { border: 1px solid #ccc; padding: 15px; margin-bottom: 10px; border-radius: 5px; }
        .price { font-weight: bold; color: green; }
        .nav { margin-bottom: 20px; padding: 10px; background-color: #f4f4f4; border-radius: 5px; }
        .admin-btn { color: white; background-color: #007bff; padding: 5px 10px; text-decoration: none; border-radius: 3px; }
    </style>
</head>
<body>

<div class="nav">
    <b>Привет, ${sessionScope.user.login}!</b> |

    <c:choose>
        <c:when test="${sessionScope.user.role == 'CLIENT'}">
            <a href="${pageContext.request.contextPath}/controller?command=VIEW_MY_ORDERS">Мои заказы</a> |
            <a href="${pageContext.request.contextPath}/controller?command=UPDATE_PASSWORD">Настройки</a> |
        </c:when>

        <c:when test="${sessionScope.user.role == 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/controller?command=TO_ADD_PRODUCT_PAGE" class="admin-btn">+ Добавить товар</a> |
        </c:when>
    </c:choose>

    <a href="${pageContext.request.contextPath}/controller?command=LOGOUT" style="color: red;">Выйти</a>
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

                <c:if test="${sessionScope.user.role == 'CLIENT'}">
                    <form action="${pageContext.request.contextPath}/controller" method="post" style="display:inline-block;">
                        <input type="hidden" name="command" value="MAKE_ORDER">
                        <input type="hidden" name="productId" value="${product.id}">
                        <button type="submit">Купить</button>
                    </form>
                </c:if>

                <c:if test="${sessionScope.user.role == 'ADMIN'}">
                    <form action="${pageContext.request.contextPath}/controller" method="post" style="display:inline-block;">
                        <input type="hidden" name="command" value="DELETE_PRODUCT">
                        <input type="hidden" name="productId" value="${product.id}">
                        <button type="submit" style="color: red;">Удалить товар</button>
                    </form>
                </c:if>
            </div>
        </c:forEach>
    </c:otherwise>
</c:choose>

</body>
</html>