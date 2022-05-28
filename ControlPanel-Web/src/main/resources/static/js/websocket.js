// noinspection JSUnresolvedFunction,JSUnresolvedVariable,JSCheckFunctionSignatures,JSIgnoredPromiseFromCall

let stompClient = null;

connect();

function connect() {
    const server = document.getElementById("server").innerText;
    const socket = new SockJS("/server/api/server");
    stompClient = Stomp.over(socket);

    stompClient.connect({
        "username": user.username,
        "password": user.hash
    }, () => {
        stompClient.subscribe("/server/api/console", function (messageOutput) {
            obj = JSON.parse(messageOutput.body);
            if (obj.server === server) {
                showMessageOutput(obj.data);
            }
        }, {
            "username": user.username,
            "password": user.hash,
            "server": server
        });
    });
}

function sendMessage(command) {
    console.log("Sending message @ " + Date.now());
    const server = document.getElementById("server").innerText;

    stompClient.send("/app/server/api/server", {
            "username": user.username,
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