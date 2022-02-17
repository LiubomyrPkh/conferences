<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="static/css/main.css" media="screen" type="text/css" />
</head>
<body>
<div class="alightCenter">
    <div id="login">
        <div class="loginText">Registration</div>
        <form action="registration" method="post">
            <fieldset class="clearfix">
                <p><span class="fontawesome-pencil loginIcon"></span>
                    <label>
                        <input pattern="[A-Za-zа-яА-яіїє]{2,30}" name="name" type="text" placeholder="Name" required>
                    </label>
                </p>
                <p><span class="fontawesome-pencil loginIcon"></span>
                    <label>
                        <input pattern="[A-Za-zа-яА-яіїє]{2,30}" name="surname" type="text" placeholder="Surname" required>
                    </label>
                </p>
                <p><span class="fontawesome-user loginIcon"></span>
                    <label>
                        <input pattern="^[\w\.]+@([\w-]+\.)+[\w-]{2,4}$" name="email" type="email" placeholder="Email" required>
                    </label>
                </p>
                <p><span class="fontawesome-lock loginIcon"></span>
                    <label>
                        <input pattern="^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$" name="password" type="password" placeholder="Password" required>
                    </label>
                </p>
                <p><span class="fontawesome-lock loginIcon"></span>
                    <label>
                        <input name="passwordRepeat" type="password" placeholder="Repeat Password" required>
                    </label>
                </p>
                <p><input type="submit" value="Sing Up"></p>
            </fieldset>
        </form>
        <p>Have an account? &nbsp;&nbsp;<a href="login">Login</a><span class="fontawesome-arrow-right"></span></p>
        <span>${requestScope.error}</span>
    </div>
</div>

</body>
</html>
