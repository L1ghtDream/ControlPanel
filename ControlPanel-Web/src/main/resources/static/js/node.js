// noinspection ES6MissingAwait,JSCheckFunctionSignatures

registerEventListener("save", save)
registerEventListener("reset", reload)
registerEventListener("delete", deleteNode)
registerEventListener("create", createNode)

async function sendNodeData(api){
    callAPI(api, {
        id: document.getElementById('id').value,
        name: document.getElementById('name').value,
        ip: document.getElementById('ip').value,
        username: document.getElementById('username').value,
        sshPort: document.getElementById('ssh_port').value
    }, () => {
        reload();
    });
}

async function createNode(){
    sendNodeData("/api/node/create")
}

async function save() {
    let nodeID = document.getElementById('id').value;
    sendNodeData("/api/node/" + nodeID + "/save")
}

async function deleteNode() {
    let nodeID = document.getElementById('id').value;

    callAPI("/api/node/" + nodeID + "/delete", {}, () => {
        reload();
    });
}