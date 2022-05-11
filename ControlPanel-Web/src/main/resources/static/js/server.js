// noinspection JSIgnoredPromiseFromCall,ES6MissingAwait,JSCheckFunctionSignatures

registerEventListener("save", save);
registerEventListener("reset", reload);
registerEventListener("delete", deleteServer);
registerEventListener("create", createServer);

async function sendServerData(api, callback) {
    callAPI(api, {
        nodeID: document.getElementById('node').value,
        id: document.getElementById('id').value,
        name: document.getElementById('name').value,
        path: document.getElementById('path').value,
        port: document.getElementById('port').value,
        java: document.getElementById('java').value,
        ram: document.getElementById('ram').value,
        serverJar: document.getElementById('server_jar').value,
        args: document.getElementById('args').value,
        startIfOffline: document.getElementById('start_if_offline').value
    }, callback);
}

async function save() {
    let serverID = document.getElementById('id').value;
    sendServerData("/api/server/" + serverID + "/save", reload)
}

async function createServer() {
    sendServerData("/api/server/create", () => {
        redirect("/server/" + document.getElementById('id').value);
    })
}

async function deleteServer() {
    let serverID = document.getElementById('id').value;

    callAPI("/api/server/" + serverID + "/delete", {}, () => {
        redirect("/")
    });
}

