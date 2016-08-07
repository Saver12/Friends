<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : 'EN'}" scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="i18n.text" var="msg"/>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><fmt:message key="login.title" bundle="${msg}"/></title>
</head>
<body>
<h3><fmt:message key="login.ask" bundle="${msg}"/></h3>
<form action="<c:url value="login"/>" method="post">
    <strong><fmt:message key="login.label.email" bundle="${msg}"/></strong>:<input type="text" name="email"><br>
    <strong><fmt:message key="login.label.password" bundle="${msg}"/></strong>:<input type="password" name="password"><br>
    <fmt:message key="login.button.submit" var="buttonValue" bundle="${msg}" />
    <input type="submit" value="${buttonValue}">
</form>
<br>
<fmt:message key="login.ask.new" bundle="${msg}"/> <a href="<c:url value="register"/>"><fmt:message key="login.ask.register" bundle="${msg}"/></a>.
<br><br>
<form>
    <input type="submit" name="language" value="RU" >
    <input type="submit" name="language" value="EN" >
</form>
</body>
</html>