// noinspection JSCheckFunctionSignatures
async function nodeTemplate() {
    document.getElementById("logout").hidden = true;

    //Check if the user is already logged in and if so redirect to the home page
    if (!hasLoginDataInCookies()) {
        redirect("/");
    }

    document.getElementById('save').addEventListener('click', function () {
        save();
    });

    document.getElementById('reset').addEventListener('click', function () {
        reload();
    });

    document.getElementById('delete').addEventListener('click', function () {
        deleteNode();
    });
}

async function save() {
    let nodeID = document.getElementById('id').value;

    callAPI("/api/node/" + nodeID + "/save", {
        name: document.getElementById('name').value,
        ip: document.getElementById('ip').value,
        username: document.getElementById('username').value,
        sshPort: document.getElementById('ssh_port').value
    }, () => {
        reload();
    });
}

async function deleteNode() {
    let nodeID = document.getElementById('id').value;

    callAPI("/api/node/" + nodeID + "/delete", {}, () => {
        reload();
    });
}