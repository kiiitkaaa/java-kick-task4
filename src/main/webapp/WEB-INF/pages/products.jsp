<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Каталог товаров</title></head>
<body>
<jsp:include page="/WEB-INF/pages/header.jsp" />

<h2>Доступные товары</h2>

<c:if test="${sessionScope.user.role == 'ADMIN'}">
    <form action="${pageContext.request.contextPath}/controller" method="post" style="margin-bottom: 20px;">
        <input type="hidden" name="command" value="ADD_PRODUCT">
        Название: <input type="text" name="name" required>
        Цена: <input type="number" step="0.01" name="price" required>
        <button type="submit">Добавить новый товар</button>
    </form>
</c:if>

<table border="1" cellpadding="5">
    <tr>
        <th>Название</th>
        <th>Цена</th>
        <th>Действия</th>
    </tr>
    <c:forEach var="product" items="${requestScope.productList}">
        <tr>
            <td>${product.name}</td>
            <td>${product.price} руб.</td>
            <td>
                <c:if test="${not empty sessionScope.user}">
                    <form action="${pageContext.request.contextPath}/controller" method="post" style="display:inline;">
                        <input type="hidden" name="command" value="CREATE_ORDER">
                        <input type="hidden" name="productId" value="${product.id}">
                        <button type="submit">Сделать заказ</button>
                    </form>
                </c:if>

                <c:if test="${sessionScope.user.role == 'ADMIN'}">
                    <form action="${pageContext.request.contextPath}/controller" method="post" style="display:inline;">
                        <input type="hidden" name="command" value="DELETE_PRODUCT">
                        <input type="hidden" name="productId" value="${product.id}">
                        <button type="submit">Удалить</button>
                    </form>
                </c:if>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>