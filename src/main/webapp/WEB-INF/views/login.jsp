<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>Login</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="static/css/main.css" media="screen" type="text/css" />
</head>
<body>
<div class="alightCenter">
    <div id="login">
        <div class="loginText">Login</div>
        <form action="login" method="post">
            <fieldset class="clearfix">
                <p><span class="fontawesome-user loginIcon"></span>
                    <label>
                        <input pattern="^[\w\.]+@([\w-]+\.)+[\w-]{2,4}$" name="email" type="email" placeholder="Email" required>
                    </label>
                </p>
                <span class="error"></span>
                <p><span class="fontawesome-lock loginIcon"></span>
                    <label>
                        <input <%--pattern="^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$"--%> name="password" type="password" placeholder="Password" required>
                    </label>
                </p>
                <span class="error"></span>
                <p><input type="submit" value="Sing In"></p>
            </fieldset>
        </form>
        <p>Don't have an account? &nbsp;&nbsp;<a href="registration">Registration</a><span class="fontawesome-arrow-right"></span></p>
        <span>${requestScope.error}</span>
    </div>
</div>
</body>
</html>
