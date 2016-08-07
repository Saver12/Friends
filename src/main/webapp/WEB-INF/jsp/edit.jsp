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
<jsp:useBean id="user" type="ru.yandex.saver12.model.User" scope="session"/>
<br><br><br><br>

<div class="list2">
    <div class="talkName"> <fmt:message key="edit.profile" bundle="${msg}"/>:</div>
    <form action="<c:url value="edit"/>" method="post">
        <fmt:message key="register.label.name" bundle="${msg}"/>: &nbsp; <input type="text" name="name" value="${user.name}"><br>
        <fmt:message key="register.label.surname" bundle="${msg}"/>: &nbsp; <input type="text" name="surname" value="${user.surname}"><br>
        <fmt:message key="profile.list.birthday" bundle="${msg}"/>: &nbsp; <input type="date" name="birthdate" value=<fmt:formatDate pattern="yyyy-MM-dd" value="${user.birthdate}"/>><br>
        <fmt:message key="profile.list.country" bundle="${msg}"/>: &nbsp; <input type="text" name="country" value="${user.country}"><br>
        <fmt:message key="profile.list.job" bundle="${msg}"/>: &nbsp; <input type="text" name="job" value="${user.job}"><br>
        <fmt:message key="profile.list.education" bundle="${msg}"/>: &nbsp; <input type="text" name="education" value="${user.education}"><br>
        <fmt:message key="profile.list.relationship" bundle="${msg}"/>: &nbsp;
        <c:set var="stats" value="${language eq 'EN' ? statsEN : statsRU}"/>
        <select name="relationship">
            <c:forEach items="${stats}" var="item">
                <c:choose>
                    <c:when test="${user.relationship eq item}">
                        <option selected value="${item}">${item}</option>
                    </c:when>
                    <c:otherwise>
                        <option value="${item}">${item}</option>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </select><br>
        <fmt:message key="edit.button.update" var="update" bundle="${msg}" />
        <input type="submit" class="downlPhotoButton"  value="${update}">
    </form>
</div>
</body>
</html>