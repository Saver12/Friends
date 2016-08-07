<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="i18n.text" var="msg"/>
<!DOCTYPE html>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/header.jsp"/>

<br><br><br><br>

<div class="list2">
    <div class="talkName"> <fmt:message key="requests.all" bundle="${msg}"/>: </div>
    <c:forEach items="${requestsList}" var="item" varStatus="loop">
        <a href="<c:url value="profile?id=${item.id}"/>" class="names2">${item.name} ${item.surname}</a>
        <form action="<c:url value="requests"/>" method="post">
            <input type="hidden" name="request" value="accept">
            <input type="hidden" name="id" value="${item.id}">
            <fmt:message key="requests.button.accept" var="accept" bundle="${msg}" />
            <input type="submit" class="addRequestButton" value="${accept}">
        </form>
        <form action="<c:url value="requests"/>" method="post">
            <input type="hidden" name="request" value="decline">
            <input type="hidden" name="id" value="${item.id}">
            <fmt:message key="requests.button.decline" var="decline" bundle="${msg}" />
            <input type="submit" class="deleteRequestButton" value="${decline}">
        </form>
        ${!loop.last ? '<hr>' : ''}
    </c:forEach>
</div>
</body>
</html>