// noinspection JSIgnoredPromiseFromCall,ES6MissingAwait,JSCheckFunctionSignatures

registerEventListener("save", save);
registerEventListener("create", create);
registerEventListener("reset", reload);
registerEventListener("delete", deleteUser);
registerEventListener("disable-2fa", disable2FA);
registerEventListener("save-noperms", save);
registerEventListener("enable-2fa", enable2FARequest);

async function save() {
    sendUser("/api/user/%user_id%/save");
}

async function create() {
    sendUser("/api/user/create");
}

async function sendUser(api) {
    let element = document.getElementById('id');
    let userID = null
    if (element != null) {
        userID = element.value;
    }

    api = api.replace("%user_id%", userID);

    callAPI(api, {
        id: userID,
        password: document.getElementById('password').value,
        username: document.getElementById('username').value,
        GLOBAL_ADMIN: document.getElementById('GLOBAL_ADMIN').checked,
        GLOBAL_MANAGE_USERS: document.getElementById('GLOBAL_MANAGE_USERS').checked,
        GLOBAL_MANAGE_NODES: document.getElementById('GLOBAL_MANAGE_NODES').checked,
        GLOBAL_VIEW: document.getElementById('GLOBAL_VIEW').checked
    }, () => {
        if (api === "/api/user/create") {
            redirect("/admin/users")
        } else {
            reload();
        }
    });
}

async function disable2FA() {
    let userID = document.getElementById('id').value;

    callAPI("/api/user/" + userID + "/2fa/disable", {}, () => {
        reload();
    });
}

async function deleteUser() {
    let userID = document.getElementById('id').value;

    callAPI("/api/user/" + userID + "/delete", {}, () => {
        redirect("/admin/users");
    });
}

async function enable2FA() {
    location.href = '#enable2fa';
}

async function enable2FARequest() {
    let userID = document.getElementById('id').value;

    callAPI("/api/user/" + userID + "/enable-2fa", {
        otp: document.getElementById("2fa-otp").value
    }, () => {
        reload();
    });
}