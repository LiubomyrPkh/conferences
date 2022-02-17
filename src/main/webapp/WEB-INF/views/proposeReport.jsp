<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Propose Report</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="static/css/main.css" media="screen" type="text/css" />
</head>
<body>
<div class="alightCenter">
    <div id="login">
        <div class="loginText">Propose Report</div>
        <form action="proposeReport" method="post">
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
