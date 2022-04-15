var stompClient = null;
var user = JSON.parse(atob(getCookie("login_data")));

function setConnected(connected) {
    document.getElementById("connect").disabled = connected;
    document.getElementById("disconnect").disabled = !connected;
    document.getElementById("conversationDiv").style.visibility
        = connected ? "visible" : "hidden";
    document.getElementById("response").innerHTML = "";
}

function connect() {
    var server = document.getElementById("server").innerText;

    var socket = new SockJS("/server/api/server");
    stompClient = Stomp.over(socket);

    stompClient.connect({
        "username": user.username,
        "password": user.hash
    }, () => {
        setConnected(true);
        console.log("Connected");
        stompClient.subscribe("/server/" + server + "/api/console", function (messageOutput) {
            showMessageOutput(messageOutput.body);
        }, {
            "username": user.username,
            "password": user.hash
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    var server = document.getElementById("server").innerText;

    var text = document.getElementById("text").value;
    stompClient.send("/app/server/api/server", {
            "username": user.username,
            "password": user.hash
        },
        JSON.stringify({
            "command": text,
            "server": server,
            "cookie": user
        }));
}

function getIndex(string, subString, index) {
    var len = string.length;
    var i = -1;
    while (index-- && i++ < len) {
        i = string.indexOf(subString, i);
        if (i < 0) {
            break;
        }
    }
    return i;
}

async function showMessageOutput(messageOutput) {
    var response = document.getElementById("response");
    response.innerHTML = response.innerHTML + messageOutput;
    var size = response.innerHTML.split("<br>").length;

    if (size > 25) {
        //Remove the first line size-25 lines
        response.innerHTML = response.innerHTML.substring(getIndex(response.innerHTML, "<br>", size - 25) + 4);
    }

    //Scroll to the bottom
    window.scrollTo(0, document.body.scrollHeight);
}