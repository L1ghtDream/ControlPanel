registerEventListener("save", save);
registerEventListener("reset", reload);
registerEventListener("delete", deleteServer);
registerEventListener("create", createServer);

async function sendServerData(api) {

    callAPI(api, {
        name: document.getElementById('name').value,
        path: document.getElementById('path').value,
        port: document.getElementById('port').value,
        java: document.getElementById('java').value,
        ram: document.getElementById('ram').value,
        serverJar: document.getElementById('server_jar').value,
        args: document.getElementById('args').value,
        startIfOffline: document.getElementById('start_if_offline').value,
    }, () => {
        reload();
    });
}

async function save() {
    let serverID = document.getElementById('id').value;
    sendServerData("/api/server/" + serverID + "/save")
}

async function createServer() {
    let serverID = document.getElementById('id').value;
    sendServerData("/api/server/" + serverID + "/create")
}

async function deleteServer() {
    let nodeID = document.getElementById('id').value;

    callAPI("/api/node/" + nodeID + "/delete", {}, () => {
        reload();
    });
}

