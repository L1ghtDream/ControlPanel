// noinspection JSUnresolvedFunction,JSUnresolvedVariable,JSCheckFunctionSignatures,JSIgnoredPromiseFromCall

let stompClient = null;

connect();

function connect() {
    const server = document.getElementById("server").innerText;
    const socket = new SockJS("/server/" + server + "/api/server");
    stompClient = Stomp.over(socket);

    stompClient.connect({
        "username": user.id,
        "password": user.hash
    }, () => {
        stompClient.subscribe("/server/" + server + "/api/console", function (messageOutput) {
            console.log("Received response @ " + Date.now());
            showMessageOutput(messageOutput.body);
        }, {
            "username": user.id,
            "password": user.hash
        });
    });
}

function sendMessage(command) {
    console.log("Sending message @ " + Date.now());
    const server = document.getElementById("server").innerText;

    stompClient.send("/app/server/api/server", {
            "username": user.id,
            "password": user.hash
        },
        JSON.stringify({
            "command": command,
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

    if (size > 50) {
        //Remove the first line size-50 lines
        response.innerHTML = response.innerHTML.substring(getIndex(response.innerHTML, "<br>", size - 50) + 4);
    }

    //Scroll to the bottom
    window.scrollTo(0, document.body.scrollHeight);
}