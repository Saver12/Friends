<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="i18n.text" var="msg"/>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><fmt:message key="register.title" bundle="${msg}"/></title>
</head>
<body>
<h3><fmt:message key="register.ask" bundle="${msg}"/></h3>
<form action="<c:url value="register"/>" method="post">
    <strong><fmt:message key="login.label.email" bundle="${msg}"/></strong>:<input type="text" name="email"><br>
    <strong><fmt:message key="login.label.password" bundle="${msg}"/></strong>:<input type="password" name="password"><br>
    <strong><fmt:message key="register.label.name" bundle="${msg}"/></strong>:<input type="text" name="name"><br>
    <strong><fmt:message key="register.label.surname" bundle="${msg}"/></strong>:<input type="text" name="surname"><br>
    <strong><fmt:message key="profile.list.country" bundle="${msg}"/></strong>:<input type="text" name="country"><br>
    <fmt:message key="register.button.submit" var="buttonValue" bundle="${msg}" />
    <input type="submit" value="${buttonValue}">
</form>
<br>
<fmt:message key="register.ask.user" bundle="${msg}"/> <a href="<c:url value="login"/>"><fmt:message key="register.ask.login" bundle="${msg}"/></a>.
</body>
</html>