// noinspection JSCheckFunctionSignatures
async function userTemplate() {
    document.getElementById("logout").hidden = true;

    //Check if the user is already logged in and if so redirect to the home page
    if (!hasLoginDataInCookies()) {
        redirect("/");
    }

    registerEventListener("save", save);
    registerEventListener("create", create);
    registerEventListener("reset", reload);
    registerEventListener("delete", deleteUser);
    registerEventListener("disable-2fa", disable2FA);
    registerEventListener("save-noperms", () => save(false));
}

async function save(savePermissions = true) {
    sendUser("/api/user/%user_id%/save", savePermissions);
}

async function create() {
    sendUser("/api/user/create", true);
}

async function sendUser(api, savePermissions = true) {
    let element = document.getElementById('id');
    let userID = null
    if (element != null) {
        userID = element.value;
    }

    api = api.replace("%user_id%", userID);

    data = {
        password: document.getElementById('password').value,
        username: document.getElementById('username').value,
    }

    if (savePermissions) {
        data.GLOBAL_ADMIN = document.getElementById('GLOBAL_ADMIN').checked;
        data.GLOBAL_MANAGE_USERS = document.getElementById('GLOBAL_MANAGE_USERS').checked;
        data.GLOBAL_MANAGE_NODES = document.getElementById('GLOBAL_MANAGE_NODES').checked;
    }

    callAPI(api, data, () => {
        if (!savePermissions) {
            reload();
            return;
        }
        redirect("/admin/users")
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