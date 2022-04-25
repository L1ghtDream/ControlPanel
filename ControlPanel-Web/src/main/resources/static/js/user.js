// noinspection JSCheckFunctionSignatures
async function userTemplate() {
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
        deleteUser();
    });

    document.getElementById('disable-2fa').addEventListener('click', function () {
        disable2FA();
    });
}

async function save() {
    let userID = document.getElementById('id').value;

    callAPI("/api/user/" + userID + "/save", {
        password: document.getElementById('password').value,
        username: document.getElementById('username').value
    }, () => {
        reload();
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
        reload();
    });
}