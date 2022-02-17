<%--
  Created by IntelliJ IDEA.
  User: lubom
  Date: 12.02.2022
  Time: 23:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Event</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="static/css/main.css" media="screen" type="text/css"/>
</head>
<body>
<div class="alightCenter">
    <div id="login">
        <div class="loginText">Create Event</div>
        <form action="createEvent" method="post">
            <fieldset class="clearfix">
                <p><span class="fontawesome-comment loginIcon"></span>
                    <label>
                        <input pattern="[A-Za-zа-яА-яіїє]{2,100}" name="title" type="text" placeholder="Title" required>
                    </label>
                </p>
                <p><span class="fontawesome-road loginIcon"></span>
                    <label>
                        <input pattern="[A-Za-zа-яА-яіїє]{2,30}" name="country" type="text" placeholder="Country" required>
                    </label>
                </p>
                <p><span class="fontawesome-road loginIcon"></span>
                    <label>
                        <input pattern="[A-Za-zа-яА-яіїє]{2,30}" name="city" type="text" placeholder="City" required>
                    </label>
                </p>
                <p><span class="fontawesome-road loginIcon"></span>
                    <label>
                        <input pattern="[A-Za-zа-яА-яіїє]{2,30}" name="street" type="text" placeholder="Street" required>
                    </label>
                </p>
                <p><span class="fontawesome-road loginIcon"></span>
                    <label>
                        <input name="houseNum" type="number" placeholder="House Number" required>
                    </label>
                </p>
                <p><span class="fontawesome-road loginIcon"></span>
                    <label>
                        <input name="flatNum" type="number" placeholder="Flat Number">
                    </label>
                </p>
                <p><span class="fontawesome-table loginIcon"></span>
                    <label>
                        <input name="datetime" type="datetime-local" placeholder="Date" required>
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
