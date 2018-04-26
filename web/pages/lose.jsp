<%@ page import="database.Database" %>
<%@ page import="database.User" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%long time = System.nanoTime();%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width; initial-scale=0.85; maximum-scale=0.85; user-scalable=0;"/>
    <title>you lose</title>
    <link rel="stylesheet" href="../style/font-awesome-4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="../style/style.css">
    <link href="https://fonts.googleapis.com/css?family=Cormorant+Unicase" rel="stylesheet">
</head>
<body>
<div class="header">
    <div class="title text-center">
        <a href="${pageContext.request.contextPath}/logout" class="exit reverse"><i class="fa fa-sign-out" aria-hidden="true"></i></a>
        <h2>ВЫ ПРОИГРАЛИ..</h2>
    </div>
</div>
<div class="section">
    <div class="left-box">
        <div class="stats">
            <table>
                <tr>
                    <td>урон:</td>
                    <td>+1</td>
                </tr>
                <tr>
                    <td>здоровье:</td>
                    <td>+1</td>
                </tr>
                <tr>
                    <td>очков рейтинга:</td>
                    <td>-1</td>
                </tr>
            </table>
        </div>
    </div>
    <%--<div class="right-box">--%>
        <%--<div class="stats">--%>
            <%--<table>--%>
                <%--<tr>--%>
                    <%--<td>ваш урон:</td>--%>
                    <%--<td>11</td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                    <%--<td>ваше здоровье:</td>--%>
                    <%--<td>101</td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                    <%--<td>ваш рейтинг:</td>--%>
                    <%--<td>49</td>--%>
                <%--</tr>--%>
            <%--</table>--%>
        <%--</div>--%>
    <%--</div>--%>
</div>
<div class="bottom-box-first">
    <%--<div class="title text-center">--%>
        <%--<h2>Ваша позиция в рейтинге: <strong>2</strong></h2>--%>
        <%--<br>--%>
    <%--</div>--%>
    <form action="${pageContext.request.contextPath}/pages/homePage.jsp">
        <div class="button text-center">
            <button class="btn btn-ok">OK</button>
        </div>
    </form>
</div>
</div>
<div class="footer">
    <div style="color: white;">page: <%=(System.nanoTime() - time) / 1000000.0%>ms, db: <%=Database.getCount()%>req (<%=User.getTimeExecFullInMillis()%>ms)</div></div>
</body>
</html>