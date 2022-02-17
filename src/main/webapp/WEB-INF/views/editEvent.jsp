<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Event</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="static/css/main.css" media="screen" type="text/css"/>
</head>
<body>
<div class="alightCenter">
    <div id="login">
        <div class="loginText">Edit Event</div>
        <form action="createEvent" method="post">
            <fieldset class="clearfix">
                <p><span class="fontawesome-comment loginIcon"></span>
                    <label>
                        <input pattern="[A-Za-zа-яА-яіїє]{2,100}" value="${requestScope.event.title}" name="title" type="text" placeholder="Title" required>
                    </label>
                </p>
                <p><span class="fontawesome-road loginIcon"></span>
                    <label>
                        <input pattern="[A-Za-zа-яА-яіїє]{2,30}" value="${requestScope.event.address.country}" name="country" type="text" placeholder="Country" required>
                    </label>
                </p>
                <p><span class="fontawesome-road loginIcon"></span>
                    <label>
                        <input pattern="[A-Za-zа-яА-яіїє]{2,30}" value="${requestScope.event.address.city}" name="city" type="text" placeholder="City" required>
                    </label>
                </p>
                <p><span class="fontawesome-road loginIcon"></span>
                    <label>
                        <input pattern="[A-Za-zа-яА-яіїє]{2,30}" value="${requestScope.event.address.street}" name="street" type="text" placeholder="Street" required>
                    </label>
                </p>
                <p><span class="fontawesome-road loginIcon"></span>
                    <label>
                        <input value="${requestScope.event.address.houseNum}" name="houseNum" type="number" placeholder="House Number" required>
                    </label>
                </p>
                <p><span class="fontawesome-road loginIcon"></span>
                    <label>
                        <input value="${requestScope.event.address.flatNum}" name="flatNum" type="number" placeholder="Flat Number">
                    </label>
                </p>
                <p><span class="fontawesome-table loginIcon"></span>
                    <label>
                        <input value="${requestScope.event.eventDate}" name="datetime" type="datetime-local" placeholder="Date" required>
                    </label>
                </p>
                <p><input type="submit" value="Propose"></p>
            </fieldset>
        </form>
        <span>${requestScope.error}</span>
    </div>
</div>
</body>
</html>
