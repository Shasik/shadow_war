<%@ page import="model.Player" %>
<%@ page import="database.User" %>
<%@ page import="database.Database" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%long time = System.nanoTime();%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width; initial-scale=0.85; maximum-scale=0.85; user-scalable=0;"/>
    <title>Home page</title>
    <link rel="stylesheet" href="../style/style.css">
    <link href="https://fonts.googleapis.com/css?family=Cormorant+Unicase" rel="stylesheet">
    <link rel="stylesheet" href="../style/font-awesome-4.7.0/css/font-awesome.min.css">
</head>
<body>

<%
    Player player = User.getUserByNickname((String)session.getAttribute("nickname"));
%>

<div class="header">
    <div class="title text-center">
        <a href="${pageContext.request.contextPath}/logout" class="exit reverse"><i class="fa fa-sign-out" aria-hidden="true"></i></a>
        <h2>Ваш личный кабинет</h2>
    </div>
</div>
<div class="section">
    <div class="left-box">
        <div class="nickname text-center">
            <h2><%=player.getNickname()%>
            </h2>
        </div>
        <div class="player"></div>
    </div>
    <div class="right-box">
        <div class="stats">
            <table>
                <tr>
                    <td>ваш урон:</td>
                    <td><%=player.getDamage()%>
                    </td>
                </tr>
                <tr>
                    <td>ваше здоровье:</td>
                    <td><%=player.getHp()%>
                    </td>
                </tr>
                <tr>
                    <td>очков рейтинга:</td>
                    <td><%=player.getRating()%>
                    </td>
                </tr>
                <tr>
                    <td>позиция в рейтинге:</td>
                    <td><%=User.getRatePosition(player.getNickname())%>
                    </td>
                </tr>
            </table>
        </div>
    </div>

    <div class="bottom-box-first">
        <div class="battle">
            <div class="button text-center">
                <form action="${pageContext.request.contextPath}/pages/battle.jsp">
                    <button class="btn">ДУЭЛЬ</button>
                </form>
            </div>
        </div>
    </div>
    <div style="color: white;">page: <%=(System.nanoTime() - time) / 1000000.0%>ms, db: <%=Database.getCount()%>req (<%=User.getTimeExecFullInMillis()%>ms)</div>
</div>
</body>
</html>