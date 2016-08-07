<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="i18n.text" var="msg"/>
<!DOCTYPE html>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/header.jsp"/>

<br><br><br><br>

<jsp:useBean id="user" type="ru.yandex.saver12.model.User" scope="session"/>
<div class="allPhotos"><fmt:message key="music.audios" bundle="${msg}"/>:</div>
<c:if test="${empty param}">
<div class="downlPhoto">
    <form action="<c:url value="audios"/>" method="post" enctype="multipart/form-data">
        <fmt:message key="music.upload" bundle="${msg}"/>:<input type="file" name="audio" accept="audio/*"/><br>
        <input type="hidden" name="request" value="upload">
        <input type="hidden" name="id" value="${user.id}">
        <fmt:message key="music.button.upload" var="upload" bundle="${msg}" />
        <input type="submit" class="downlPhotoButton" value="${upload}">
    </form>
</div>
</c:if>

<div class="list3">
    <c:forEach items="${audiosList}" var="item" varStatus="loop">
        <div class="description-audio">${item.name}</div>
        <audio controls class="audio">
            <source src="<c:url value="media/${item.id}.mp3"/>" type="audio/mpeg">
            Your browser does not support the audio element.
        </audio>
        <c:if test="${empty param}">
            <form action="<c:url value="audios"/>" method="post">
                <input type="hidden" name="request" value="delete">
                <input type="hidden" name="id" value="${item.id}">
                <fmt:message key="music.button.delete" var="delete" bundle="${msg}" />
                <input type="submit" class="deleteAudioButton" value="${delete}">
            </form>
            <div class="textarea-audio">
                <form action="<c:url value="audios"/>" method="post">
                    <fmt:message key="music.place.description" var="description" bundle="${msg}"/>
                    <textarea rows="2" cols="44" name="message" placeholder="${description}"></textarea><br>
                    <input type="hidden" name="request" value="change">
                    <input type="hidden" name="id" value="${item.id}">
                    <fmt:message key="music.button.send" var="send" bundle="${msg}" />
                    <input type="submit" class="changeDescButton" value="${send}">
                </form>
            </div>
        </c:if>
        ${!loop.last ? '<hr class="underline-audio">' : ''}
    </c:forEach>
</div>
</body>
</html>