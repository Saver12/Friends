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

<div class="list">
    <div class="talkName"> <fmt:message key="friends.all" bundle="${msg}"/>: </div>
    <c:if test="${not empty request}">
        <div class="request"><a href="<c:url value="requests"/>" class="names"> <fmt:message key="friends.href.requests" bundle="${msg}"/> </a></div>
    </c:if>
    <m:show list="${friendsList}"/>
</div>
</body>
</html>