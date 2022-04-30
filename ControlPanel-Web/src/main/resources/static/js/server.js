registerEventListener("save", save);
registerEventListener("reset", reload);
registerEventListener("delete", deleteServer);

async function save() {
    let serverID = document.getElementById('id').value;

    console.log(serverID)
    console.log(document.getElementById('name').value);
    console.log(document.getElementById('path').value);
    console.log(document.getElementById('port').value);
    console.log(document.getElementById('java').value);
    console.log(document.getElementById('ram').value);
    console.log(document.getElementById('server_jar').value);
    console.log(document.getElementById('args').value);
    console.log(document.getElementById('start_if_offline').value);

    callAPI("/api/server/" + serverID + "/save", {
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

async function deleteServer() {
    let nodeID = document.getElementById('id').value;

    callAPI("/api/node/" + nodeID + "/delete", {}, () => {
        reload();
    });
}