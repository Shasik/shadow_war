<%@ page import="database.Database" %>
<%@ page import="database.User" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%long time = System.nanoTime();%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width; initial-scale=0.85; maximum-scale=0.85; user-scalable=0;" />
    <title>Shadow War</title>
    <link href="https://fonts.googleapis.com/css?family=Cormorant+Unicase" rel="stylesheet">
    <link rel="stylesheet" href="style/font-awesome-4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="style/style.css">
</head>
<body>
<div class="container">
    <div class="logo"></div>
    <div class="form">
        <form action="${pageContext.request.contextPath}/login" method="post" class="reg-form">
            <div class="form-row">
                <label for="form_name"></label>
                <input type="text" id="form_name" name="nickname" placeholder="введите имя">
            </div>
            <div class="form-row">
                <label for="password"></label>
                <input type="password" id="password" name="password" placeholder="введите пароль">
            </div>
            <div class="form-submit">
                <input type="submit" value="Войти">
            </div>
        </form>
    </div>
    <div style="color: white;">page: <%=(System.nanoTime() - time) / 1000000.0%>ms, db: <%=Database.getCount()%>req (<%=User.getTimeExecFullInMillis()%>ms)</div>
</div>


</body>
</html>