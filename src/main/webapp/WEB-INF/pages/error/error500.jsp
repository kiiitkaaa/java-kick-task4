<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Internal Server Error - 500</title>
</head>
<body>
<div class="error-container">
    <h1>Произошла ошибка: 500</h1>

    <h2>Детали ошибки:</h2>
    <div class="code">
        <strong>Request from:</strong> ${pageContext.errorData.requestURI} is failed<br/>
        <strong>Servlet name:</strong> ${pageContext.errorData.servletName}<br/>
        <strong>Status code:</strong> ${pageContext.errorData.statusCode}<br/>
        <strong>Exception:</strong> ${pageContext.exception}<br/>
        <strong>Message from exception:</strong> ${pageContext.exception.message}
    </div>

    <br/>
    <a href="${pageContext.request.contextPath}/">Вернуться на главную страницу</a>
</div>
</body>
</html>
