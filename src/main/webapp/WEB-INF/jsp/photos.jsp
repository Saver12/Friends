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
<div class="allPhotos"><fmt:message key="photos.all" bundle="${msg}"/>:</div>
<c:if test="${empty param}">
<div class="downlPhoto">
    <form action="<c:url value="photos"/>" method="post" enctype="multipart/form-data">
        <fmt:message key="photos.upload" bundle="${msg}"/>:<input type="file" name="photo" accept="image/*"/><br>
        <input type="hidden" name="request" value="upload">
        <input type="hidden" name="id" value="${user.id}">
        <fmt:message key="music.button.upload" var="upload" bundle="${msg}" />
        <input type="submit" class="downlPhotoButton" value="${upload}">
    </form>
</div>
</c:if>

<div class="list3">
    <c:forEach items="${photosList}" var="item" varStatus="loop">
        <div class="description">${item.name}</div>
        <img src="<c:url value="img/${item.id}.jpg"/>" class="photo-picture">
        <c:if test="${empty param}">
            <c:if test="${user.photoId != item.id}">
            <form action="<c:url value="photos"/>" method="post">
                <input type="hidden" name="request" value="delete">
                <input type="hidden" name="id" value="${item.id}">
                <fmt:message key="music.button.delete" var="delete" bundle="${msg}" />
                <input type="submit" class="deletePhotoButton" value="${delete}">
            </form>
            <form action="<c:url value="photos"/>" method="post">
                <input type="hidden" name="request" value="changeAvatar">
                <input type="hidden" name="id" value="${item.id}">
                <fmt:message key="photos.button.avatar" var="avatar" bundle="${msg}" />
                <input type="submit" class="changeAvatarButton" value="${avatar}">
            </form>
            </c:if>
            <div class="textarea-photo">
                <form action="<c:url value="photos"/>" method="post">
                    <fmt:message key="photos.place.description" var="description" bundle="${msg}"/>
                    <textarea rows="2" cols="44" name="message" placeholder="${description}"></textarea><br>
                    <input type="hidden" name="request" value="change">
                    <input type="hidden" name="id" value="${item.id}">
                    <fmt:message key="music.button.send" var="send" bundle="${msg}" />
                    <input type="submit" class="changeDescButton" value="${send}">
                </form>
            </div>
        </c:if>
        ${!loop.last ? '<hr class="underline">' : ''}
    </c:forEach>
</div>
</body>
</html>