// noinspection JSUnresolvedFunction,JSUnresolvedVariable,JSCheckFunctionSignatures,JSIgnoredPromiseFromCall

let stompClient = null;
const user = JSON.parse(atob(getCookie("login_data")));

function setConnected(connected) {
    document.getElementById("connect").disabled = connected;
    document.getElementById("disconnect").disabled = !connected;
    document.getElementById("conversationDiv").style.visibility
        = connected ? "visible" : "hidden";
    document.getElementById("response").innerHTML = "";
}

function connect() {
    const server = document.getElementById("server").innerText;

    const socket = new SockJS("/server/" + server + "/api/server");
    stompClient = Stomp.over(socket);

    stompClient.connect({
        "username": user.username,
        "password": user.hash
    }, () => {
        setConnected(true);
        console.log("Connected");
        stompClient.subscribe("/server/" + server + "/api/console", function (messageOutput) {
            console.log("Received response @ " + Date.now());
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
    console.log("Sending message @ " + Date.now());
    const server = document.getElementById("server").innerText;

    const text = document.getElementById("text").value;
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
    const len = string.length;
    let i = -1;
    while (index-- && i++ < len) {
        i = string.indexOf(subString, i);
        if (i < 0) {
            break;
        }
    }
    return i;
}

async function showMessageOutput(messageOutput) {
    const response = document.getElementById("response");
    response.innerHTML = response.innerHTML + messageOutput;
    const size = response.innerHTML.split("<br>").length;

    if (size > 25) {
        //Remove the first line size-25 lines
        response.innerHTML = response.innerHTML.substring(getIndex(response.innerHTML, "<br>", size - 25) + 4);
    }

    //Scroll to the bottom
    window.scrollTo(0, document.body.scrollHeight);
}