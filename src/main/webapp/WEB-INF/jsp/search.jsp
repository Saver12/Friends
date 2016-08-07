<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="m" uri="/WEB-INF/taglib.tld" %>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="i18n.text" var="msg"/>
<!DOCTYPE html>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/header.jsp"/>

<br><br><br><br>

<div class="searchForm">
<div class="searchName"><fmt:message key="search.ask" bundle="${msg}"/>:</div>
<form action="<c:url value="search"/>" method="post">
    <input type="text" name="name"><br>
    <fmt:message key="search.button.find" var="find" bundle="${msg}" />
    <input type="submit" class="searchFormButton" value="${find}">
</form>
</div>
<br><br>

<div class="list">
    <m:show list="${list}"/>
</div>
</body>
</html>