var socket = new WebSocket("ws://localhost:8080/shadow_war/actions");
var currentAttack;
var battlelog = document.getElementById("battle-log");
var nickname = document.querySelector("#name-first-player").textContent;
var actionMessage;

socket.onmessage = onMessage;

function onMessage(event) {
    actionMessage = JSON.parse(event.data);

    if (actionMessage.action === "initScreen") {
        document.querySelector("#name-first-player").textContent = actionMessage.your_nickname;
        document.querySelector("#progress-bar-first").value = actionMessage.your_health;
        document.querySelector("#progress-bar-first").max = actionMessage.your_health;
        document.querySelector("#damage-first-player").textContent = actionMessage.your_damage;
    }

    if (actionMessage.action === "fight") {
        document.getElementsByClassName("right-box")[0].style.display = '';
        initializeGameScreen(actionMessage);
        setTimeout(timer, 1000);
    }

    if (actionMessage.action === "attack") {
        refreshScreen(actionMessage);
    }

    if (actionMessage.action === "finishWin") {
        refreshLog("Вы убили " + (document.querySelector("#name-second-player").textContent + " нанеся " + actionMessage.damage + " смертельного урона!").trim());
        document.getElementsByClassName("battle")[0].style.display = 'none';
        document.getElementsByClassName("finish-battle")[0].style.display = '';
    }

    if (actionMessage.action === "finishLose") {
        refreshLog((document.querySelector("#name-second-player").textContent + " убил Вас нанеся " + actionMessage.damage + " смертельного урона!").trim());
        document.getElementsByClassName("finish-battle")[0].style.display = '';
    }
}

//При открытии сокета, сразу регистрация на дуэль
socket.onopen = function () {
    registerDuel(nickname)
};

function refreshScreen(params) {
    document.querySelector("#progress-bar-first").value = params.your_health;
    document.querySelector("#progress-bar-second").value = params.enemy_health;

    if (actionMessage.step === "yes") {
        document.getElementsByClassName("battle")[0].style.display = '';
        if (params.damage !== undefined) {
            refreshLog((document.querySelector("#name-second-player").textContent + " нанёс Вам " + params.damage + " урона!").trim());
        }
        currentAttack = true;
    } else {
        document.getElementsByClassName("battle")[0].style.display = 'none';
        if (params.damage !== undefined) {
            refreshLog("Вы нанесли " + (document.querySelector("#name-second-player").textContent + " " + params.damage + " урона!").trim())
        }
        currentAttack = false;
    }
}

function registerDuel(nickname) {
    var DeviceAction = {
        action: "registerDuel",
        nickname: nickname
    };
    socket.send(JSON.stringify(DeviceAction));
}

function attack() {
    var fightAction;

    if (currentAttack) {
        fightAction = {
            action: "attack",
            attack: "first"
        };
    } else {
        fightAction = {
            action: "attack",
            attack: "second"
        };
    }
    socket.send(JSON.stringify(fightAction));
}

function refreshLog(action) {
    battlelog.textContent = "\t" + action + "\n" + battlelog.textContent;
}

function redirect() {
    if (currentAttack) {
        window.location.href = "win.jsp";
    } else {
        window.location.href = "lose.jsp";
    }
}

function timer() {
    var obj=document.getElementById('timer_inp');
    obj.innerHTML--;

    if(obj.innerHTML == 0) {
        obj.style.display = 'none';
        initFight(actionMessage);
    } else {
        setTimeout(timer,1000);
    }
}

function initializeGameScreen(actionMessage) {
    document.querySelector("#name-first-player").textContent = actionMessage.your_nickname;
    document.querySelector("#progress-bar-first").value = actionMessage.your_health;
    document.querySelector("#progress-bar-first").max = actionMessage.your_health;
    document.querySelector("#damage-first-player").textContent = actionMessage.your_damage;
    document.querySelector("#name-second-player").textContent = actionMessage.enemy_nickname;
    document.querySelector("#progress-bar-second").value = actionMessage.enemy_health;
    document.querySelector("#progress-bar-second").max = actionMessage.enemy_health;
    document.querySelector("#damage-second-player").textContent = actionMessage.enemy_damage;
}