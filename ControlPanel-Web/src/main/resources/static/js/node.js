// noinspection ES6MissingAwait,JSCheckFunctionSignatures

registerEventListener("save", save)
registerEventListener("reset", reload)
registerEventListener("delete", deleteNode)
registerEventListener("create", createNode)

async function sendNodeData(api, callback) {
    callAPI(api, {
        id: document.getElementById('id').value,
        name: document.getElementById('name').value,
        ip: document.getElementById('ip').value,
        username: document.getElementById('username').value,
        sshPort: document.getElementById('ssh_port').value,
        sftpPort: document.getElementById('sftp_port').value
    }, callback);
}

async function createNode() {
    sendNodeData("/api/node/create", () => {
        redirect("/admin/nodes")
    })
}

async function save() {
    let nodeID = document.getElementById('id').value;
    sendNodeData("/api/node/" + nodeID + "/save", () => {
        reload();
    });
}

async function deleteNode() {
    let nodeID = document.getElementById('id').value;

    callAPI("/api/node/" + nodeID + "/delete", {}, () => {
        reload();
    });
}