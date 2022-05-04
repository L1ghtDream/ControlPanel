// noinspection JSIgnoredPromiseFromCall,ES6MissingAwait,JSCheckFunctionSignatures

registerEventListener("save", save);
registerEventListener("reset", reload);
registerEventListener("delete", deleteServer);
registerEventListener("create", createServer);

async function sendServerData(api, data = {}, callback) {

    data["id"] = document.getElementById('id').value;
    data["name"] = document.getElementById('name').value;
    data["path"] = document.getElementById('path').value;
    data["port"] = document.getElementById('port').value;
    data["java"] = document.getElementById('java').value;
    data["ram"] = document.getElementById('ram').value;
    data["serverJar"] = document.getElementById('server_jar').value;
    data["args"] = document.getElementById('args').value;
    data["startIfOffline"] = document.getElementById('start_if_offline').value;

    callAPI(api, data, () => {
        reload();
    }, callback);
}

async function save() {
    let serverID = document.getElementById('id').value;
    sendServerData("/api/server/" + serverID + "/save")
}

async function createServer() {
    sendServerData("/api/server/create", {
        nodeID: document.getElementById('node').value,
    }, () => {
        redirect("/server/" + document.getElementById('id').value);
    })
}

async function deleteServer() {
    let nodeID = document.getElementById('id').value;

    callAPI("/api/node/" + nodeID + "/delete", {}, () => {
        reload();
    });
}

