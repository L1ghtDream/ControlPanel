// noinspection JSCheckFunctionSignatures

async function loginTemplate() {
    document.getElementById("logout").hidden = true;

    //Check if the user is already logged in and if so redirect to the home page
    if (hasLoginDataInCookies()) {
        redirect("/");
    }

    document.getElementById('login-submit').addEventListener('click', function () {
        login();
    });
}

async function login() {
    callAPI("/api/login", {
        username: document.getElementById('username').value,
        password: document.getElementById('password').value,
        otp: document.getElementById('otp').value
    }, (obj) => {
        console.log(obj)
        console.log(obj.data)
        setCookie("login_data", btoa(JSON.stringify(obj.data)), 10 * 365);
        redirect("/")
    });
}