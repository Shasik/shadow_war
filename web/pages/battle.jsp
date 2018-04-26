<%@ page import="database.Database" %>
<%@ page import="database.User" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width; initial-scale=0.85; maximum-scale=0.85; user-scalable=0;"/>
    <title>battle page</title>
    <link rel="stylesheet" href="../style/style.css">
    <link rel="stylesheet" href="../style/font-awesome-4.7.0/css/font-awesome.min.css">
    <link href="https://fonts.googleapis.com/css?family=Cormorant+Unicase" rel="stylesheet">
</head>
<%long time = System.nanoTime();%>
<body>
<div class="header">
    <div class="title text-center">
        <div id="timer_inp" style="color: white; font-size: 65px">30</div>
    </div>
</div>
<div class="section">
    <div class="left-box">
        <div class="nickname text-center">
            <h2 id="name-first-player"><%=(String) session.getAttribute("nickname")%></h2>
        </div>
        <div class="player"></div>
        <div class="stats text-center">
            <table>
                <tr>
                    <td>урон:</td>
                    <td id="damage-first-player"></td>
                </tr>
            </table>
            <progress id="progress-bar-first" value="10" max="100"></progress>
        </div>
    </div>
    <div class="right-box" style="display: none;">
        <div class="nickname text-center">
            <h2 id="name-second-player"></h2>
        </div>
        <div class="player"></div>
        <div class="stats text-center">
            <table>
                <tr>
                    <td>урон:</td>
                    <td id="damage-second-player"></td>
                </tr>
            </table>
            <progress id="progress-bar-second" class="reverse" value="10" max="100"></progress>
        </div>
    </div>
</div>
<div class="bottom-box-first">
    <div class="battle" style="display: none;">
        <div class="button text-center">
            <button class="btn" onclick="attack()">АТАКА</button>
        </div>
    </div>
    <div class="finish-battle" style="display: none;">
        <div class="button text-center">
            <button class="btn" onclick="redirect()">OK</button>
        </div>
    </div>
</div>
<div class="footer">
    <div style="color: white;">page: <%=(System.nanoTime() - time) / 1000000.0%>ms, db: <%=Database.getCount()%>req (<%=User.getTimeExecFullInMillis()%>ms)</div>
    <label for="battle-log">
        <textarea name="battle-log" id="battle-log" cols="200" rows="6" disabled="disabled" style="background-color: #555555; color: white; font-size: 15px; border: 1px solid black"></textarea>
    </label>
</div>
<script src="../js/websocket.js"></script>
</body>
</html>