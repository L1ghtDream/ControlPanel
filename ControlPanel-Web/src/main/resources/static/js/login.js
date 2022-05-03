// noinspection JSIgnoredPromiseFromCall,ES6MissingAwait,JSCheckFunctionSignatures

document.getElementById("logout").hidden = true;

//Check if the user is already logged in and if so redirect to the home page
if (hasLoginDataInCookies()) {
    callAPI("/api/login/cookie", {}, () => {
        redirect("/")
    }, () => {
        eraseCookie("login_data")
    })
}

registerEventListener("submit", login)

async function login() {
    callAPI("/api/login", {
        username: document.getElementById('username').value,
        password: document.getElementById('password').value,
        otp: document.getElementById('otp').value
    }, (data) => {
        setCookie("login_data", btoa(JSON.stringify(data)), 10 * 365);
        redirect("/")
    });
}