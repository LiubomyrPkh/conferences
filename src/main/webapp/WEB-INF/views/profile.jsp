<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Profile</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="static/css/main.css" media="screen" type="text/css" />
</head>
<body>
<div class="alightCenter">
    <div id="profile">
        <div class="infoBlock">
            <div class="avatar">
                <img src="${pageContext.request.contextPath}/static/img/avatar.png">
                <div class="email">Email : <c:out value="${sessionScope.loggedUser.email}"/></div>
                <button class="editProfileButton" onclick="document.location='profileEdit'">Edit Profile</button>
                <form action="profile" method="post">
                    <button type="submit" class="exitButton">Log out</button>
                </form>
            </div>
            <div class="info">
                <div class="name">
                    <c:out value="${sessionScope.loggedUser.name} "/><c:out value="${sessionScope.loggedUser.surname}"/>
                </div>
                <div>Role : <c:out value="${sessionScope.loggedUser.role}"/></div>
                <div class="opportunities">
                    <c:if test="${sessionScope.loggedUser.role eq 'user'}">
                        <a href="events">Events</a>
                        <a href="events?sort=joined">Joined Events</a>
                    </c:if>
                    <c:if test="${sessionScope.loggedUser.role eq 'speaker'}">
                        <a href="proposeReport">Propose Report</a>
                        <a href="events?sort=myReports">My Reports</a>
                        <a href="events?sort=freeReports">Free Reports</a>
                    </c:if>
                    <c:if test="${sessionScope.loggedUser.role eq 'moderator'}">
                        <a href="createEvent">Create Event/Report</a>
                        <a href="events?sort=moderatorsEvents">Edit Event/Report</a>
                        <a href="events?sort=proposedEvents">Proposed Event/Report</a>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
