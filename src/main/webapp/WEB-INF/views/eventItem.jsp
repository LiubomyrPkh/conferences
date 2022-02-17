<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Event</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="static/css/main.css" media="screen" type="text/css"/>
</head>
<body>
    <div class="alightCenter">
        <div id="event">
            <div class="themes">
                <c:forEach var="event" items="${requestScope.events}">
                    <div class="theme">
                        <span><p>Theme : </p><div><c:out value="${event.title}"/></div></span>
                        <span><p>Speaker : </p><div><c:out value="${event.speaker}"/></div></span>
                        <span><p>Address : </p><div><c:out value="${event.address}"/></div></span>
                    </div>
                </c:forEach>
            </div>
            <div class="info">
                <span><p>Date/Time : </p><div><c:out value="${requestScope.events.get(0).eventDate}"/></div></span>
                <span><p>Moderators : </p><div><c:out value="${requestScope.moderators}"/></div></span>
            </div>
            <div class="description"><span><div><c:out value="${requestScope.eventDescription}"/></div></span></div>



                <%--role : user--%>
                <c:if test="${sessionScope.loggedUser.role eq 'user'}">
                <form action="events?date=${requestScope.events.get(0).eventDate}" method="post">
                    <c:if test="${requestScope.isJoined}">
                        <button type="submit" class="joinButton">Leave</button>
                    </c:if>
                    <c:if test="${!requestScope.isJoined}">
                        <button type="submit" class="joinButton">Join</button>
                    </c:if>
                </form>
                </c:if>

                    <%--role : speaker--%>
                <c:if test="${sessionScope.loggedUser.role eq 'speaker' and requestScope.hasNullSpeakers}">
                <form action="events?date=${requestScope.events.get(0).eventDate}" method="post">
                    <label>
                        <select name="selectedReport" required>
                            <c:forEach var="event" items="${requestScope.events}">
                                <c:if test="${event.speaker == null}">
                                    <option value="${event.eventId}"><c:out value="${event.title}"/></option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </label>
                    <button type="submit" class="getFreeReportButton">Get Report</button>
                </form>
                </c:if>

                    <%--role : moderator--%>
                <c:if test="${sessionScope.loggedUser.role eq 'moderator' and requestScope.hasAnyEventCreatedByModerator}">
                <form action="events" method="post">
                    <label>
                        <select name="selectedReport" required>
                            <c:forEach var="event" items="${requestScope.events}">
                                <option value="${event.eventId}"><c:out value="${event.title}"/></option>
                            </c:forEach>
                        </select>
                    </label>
                    <button type="submit" class="getFreeReportButton">Edit Event</button>
                </form>
                </c:if>
        </div>
    </div>
</body>
</html>
