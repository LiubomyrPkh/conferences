<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8"  %>
<html>
<head>
    <title>Events</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="static/css/main.css" media="screen" type="text/css"/>
</head>
<body>

<div id="events">
    <c:forEach var="entrySet" items="${requestScope.eventsMap.entrySet()}">
        <a href="events?date=${entrySet.getKey().get(0).eventDate}">
            <div class="eventItem">
    <span class="themes">
        <c:forEach var="event" items="${entrySet.getKey()}">
            <div class="theme"><c:out value="${event.title} by ${event.speaker} (${event.address})"/></div>
        </c:forEach>
        <div><c:out value="Users : ${entrySet.getValue()}"/></div>
    </span>
                <span class="info">
        <div class="date"><c:out value="${entrySet.getKey().get(0).eventDate}"/></div>
    </span>
            </div>
        </a>
    </c:forEach>
</div>

</body>
</html>
