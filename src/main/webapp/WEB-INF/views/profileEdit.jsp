<%@ page contentType="text/html;charset=UTF-8" %>


<html>
<head>
    <title>Profile Edit</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="static/css/main.css" media="screen" type="text/css" />
</head>
<body>
<div class="alightCenter">
    <div id="login">
        <div class="loginText">Edit Profile</div>
        <form action="registration" method="post">
            <fieldset class="clearfix">
                <p><span class="fontawesome-pencil loginIcon"></span>
                    <label>
                        <input value="${sessionScope.loggedUser.name}" name="name" type="text" placeholder="Name" required>
                    </label>
                </p>
                <p><span class="fontawesome-pencil loginIcon"></span>
                    <label>
                        <input value="${sessionScope.loggedUser.surname}" name="surname" type="text" placeholder="Surname" required>
                    </label>
                </p>
                <p><span class="fontawesome-user loginIcon"></span>
                    <label>
                        <input value="${sessionScope.loggedUser.email}" name="email" type="email" placeholder="Email" required>
                    </label>
                </p>
                <p><input type="submit" value="Edit"></p>
            </fieldset>
        </form>
        <p><a href="login">Edit Password</a><span class="fontawesome-arrow-right"></span></p>
        <span>${requestScope.error}</span>
    </div>
</div>
</body>
</html>
