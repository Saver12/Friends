<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="i18n.text" var="msg"/>

<div class="headermenu">
    <div class="exit">
        <form action="Logout" method="post">
            <fmt:message key="header.button.logout" var="logout" bundle="${msg}"/>
            <input type="submit" class="headerButton" value="${logout}">
        </form>
    </div>
    <div class="messages"><a href="<c:url value="messages"/>"> <fmt:message key="header.href.messages" bundle="${msg}"/> </a></div>
    <div class="page"><a href="<c:url value="profile"/>"> <fmt:message key="header.href.profile" bundle="${msg}"/> </a></div>
    <div class="search"><a href="<c:url value="search"/>"> <fmt:message key="header.href.search" bundle="${msg}"/> </a></div>
</div>