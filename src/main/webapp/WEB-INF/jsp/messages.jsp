<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="i18n.text" var="msg"/>
<!DOCTYPE html>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body onLoad="moveWin();">
<div class="wrapper">
    <jsp:include page="fragments/header.jsp"/>

    <br><br><br><br>

    <jsp:useBean id="user" type="ru.yandex.saver12.model.User" scope="session"/>
    <c:choose>
    <c:when test="${not empty userId}">
    <div class="list2">
        <c:forEach items="${messageList}" var="entry" varStatus="loop">
            <c:choose>
                <c:when test="${entry.firstUserId == user.id}">
                    <div class="small"> <fmt:message key="messages.me" bundle="${msg}"/>:</div>
                </c:when>
                <c:otherwise>
                    <div class="small"> ${name} ${surname}:</div>
                </c:otherwise>
            </c:choose>
            <div class="white-space-pre">${entry.message}</div>
            ${!loop.last ? '<hr>' : ''}
        </c:forEach>
    </div>
    <br>
    <div class="push"></div>
</div>
<div class="textarea">
    <form action="<c:url value="messages"/>" method="post">
        <fmt:message key="messages.place.message" var="message" bundle="${msg}"/>
        <textarea rows="3" cols="150" name="message" placeholder="${message}"></textarea><br>
        <input type="hidden" name="request" value="send">
        <input type="hidden" name="userId" value="${userId}">
        <fmt:message key="music.button.send" var="send" bundle="${msg}"/>
        <input type="submit" class="footerButton" value="${send}">
    </form>
</div>
</c:when>
<c:otherwise>
    <div class="list">
        <div class="talkName"> <fmt:message key="messages.dialogues" bundle="${msg}"/>:</div>
        <c:forEach items="${dialogueList}" var="entry" varStatus="loop">
            <a href="<c:url value="messages?id=${entry.id}"/>" class="names">${entry.name} ${entry.surname}</a>
            <form action="<c:url value="messages"/>" method="post">
                <input type="hidden" name="request" value="delete">
                <input type="hidden" name="userId" value="${entry.id}">
                <fmt:message key="music.button.delete" var="delete" bundle="${msg}" />
                <input type="submit" class="deleteDialogueButton" value="${delete}">
            </form>
        </c:forEach>
    </div>
</c:otherwise>
</c:choose>
</body>
</html>