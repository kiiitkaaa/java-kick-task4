<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<nav>
    <a href="${pageContext.request.contextPath}/controller?command=VIEW_PRODUCTS">Каталог товаров</a> |

    <c:choose>
        <%-- Если пользователь не авторизован --%>
        <c:when test="${empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/controller?command=TO_LOGIN_PAGE">Вход</a> |
            <a href="${pageContext.request.contextPath}/controller?command=TO_SIGN_UP_PAGE">Регистрация</a>
        </c:when>

        <%-- Если пользователь авторизован --%>
        <c:otherwise>
            <span>Привет, ${sessionScope.user.login}!</span> |
            <a href="${pageContext.request.contextPath}/controller?command=VIEW_MY_ORDERS">Мои заказы</a> |
            <a href="${pageContext.request.contextPath}/controller?command=UPDATE_PASSWORD">Настройки</a> |

            <form action="${pageContext.request.contextPath}/controller" method="post" style="display:inline;">
                <input type="hidden" name="command" value="LOGOUT">
                <button type="submit">Выйти</button>
            </form>
        </c:otherwise>
    </c:choose>
</nav>
<hr/>