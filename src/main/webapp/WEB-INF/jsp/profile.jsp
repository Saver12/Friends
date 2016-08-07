<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : 'EN'}" scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="i18n.text" var="msg"/>
<!DOCTYPE html>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/header.jsp"/>

<c:choose>
    <c:when test="${not empty requestScope.id}">
        <jsp:useBean id="id" type="ru.yandex.saver12.model.User" scope="request"/>
        <c:set var="user" value="${id}"/>
    </c:when>
    <c:otherwise>
        <jsp:useBean id="user" type="ru.yandex.saver12.model.User" scope="session"/>
        <c:set var="user" value="${user}"/>
    </c:otherwise>
</c:choose>
<br><br>

<div class="profpic">
    <img src="<c:url value="img/${user.photoId}.jpg"/>" class="profile-picture">
</div>

<div class="name"><c:out value="${user.name} ${user.surname}"/></div>

<div class="members">
    <fmt:message key="profile.href.friends" var="friends" bundle="${msg}"/>
    <c:choose>
        <c:when test="${not empty requestScope.id}">
            <a href="<c:url value="friends?id=${user.id}"/>" class="names"> ${friends} </a>
        </c:when>
        <c:otherwise>
            <a href="<c:url value="friends"/>" class="names"> ${friends} </a>
        </c:otherwise>
    </c:choose>
</div>

<div class="photos">
    <fmt:message key="profile.href.photo" var="photos" bundle="${msg}"/>
    <c:choose>
        <c:when test="${not empty requestScope.id}">
            <a href="<c:url value="photos?id=${user.id}"/>" class="names"> ${photos} </a>
        </c:when>
        <c:otherwise>
            <a href="<c:url value="photos"/>" class="names"> ${photos} </a>
        </c:otherwise>
    </c:choose>
</div>

<div class="cube">
    <fmt:message key="profile.href.music" var="music" bundle="${msg}"/>
    <c:choose>
        <c:when test="${not empty requestScope.id}">
            <a href="<c:url value="audios?id=${user.id}"/>" class="names"> ${music} </a>
        </c:when>
        <c:otherwise>
            <a href="<c:url value="audios"/>" class="names"> ${music} </a>
        </c:otherwise>
    </c:choose>
</div>

<c:choose>
    <c:when test="${not empty requestScope.id}">
        <div class="message">
            <a href="<c:url value="messages?id=${user.id}"/>" class="names"><fmt:message key="profile.href.message" bundle="${msg}"/></a>
        </div>
        <div class="friends">
            <c:choose>
                <c:when test="${request == 0}">
                    <c:choose>
                        <c:when test="${user.id == senderId}">
                            <form action="<c:url value="friends"/>" method="post">
                                <input type="hidden" name="request" value="accept">
                                <input type="hidden" name="id" value="${user.id}">
                                <fmt:message key="profile.button.accept" var="accept" bundle="${msg}"/>
                                <input type="submit" class="friendButton" value="${accept}">
                            </form>
                            <form action="<c:url value="friends"/>" method="post">
                                <input type="hidden" name="request" value="decline">
                                <input type="hidden" name="id" value="${user.id}">
                                <fmt:message key="profile.button.decline" var="decline" bundle="${msg}"/>
                                <input type="submit" class="declineButton" value="${decline}">
                            </form>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="profile.sent" bundle="${msg}"/>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:when test="${request == 1}">
                    <form action="<c:url value="friends"/>" method="post">
                        <input type="hidden" name="request" value="delete">
                        <input type="hidden" name="id" value="${user.id}">
                        <fmt:message key="profile.button.delete" var="delete" bundle="${msg}"/>
                        <input type="submit" class="friendButton" value="${delete}">
                    </form>
                </c:when>
                <c:otherwise>
                    <form action="<c:url value="friends"/>" method="post">
                        <input type="hidden" name="request" value="add">
                        <input type="hidden" name="id" value="${user.id}">
                        <fmt:message key="profile.button.accept" var="accept" bundle="${msg}"/>
                        <input type="submit" class="friendButton" value="${accept}">
                    </form>
                </c:otherwise>
            </c:choose>
        </div>
    </c:when>
    <c:otherwise>
        <div class="message">
            <a href="<c:url value="edit"/>" class="names"><fmt:message key="profile.href.edit" bundle="${msg}"/></a>
        </div>
    </c:otherwise>
</c:choose>

<ul class="user-information">
    <li><fmt:message key="profile.list.birthday" bundle="${msg}"/>: <fmt:formatDate pattern="yyyy-MM-dd" value="${user.birthdate}"/></li>
    <li><fmt:message key="profile.list.country" bundle="${msg}"/>: <c:out value="${user.country}"/></li>
    <li><fmt:message key="profile.list.job" bundle="${msg}"/>: <c:out value="${user.job}"/></li>
    <li><fmt:message key="profile.list.education" bundle="${msg}"/>: <c:out value="${user.education}"/></li>
    <li><fmt:message key="profile.list.relationship" bundle="${msg}"/>: <c:out value="${user.relationship}"/></li>
</ul>

<c:if test="${empty requestScope.id}">
    <form class="localeForm">
        <input type="submit" name="language" value="RU" class="localeButton">
        <input type="submit" name="language" value="EN" class="localeButton">
    </form>
</c:if>
</body>
</html>