// noinspection JSCheckFunctionSignatures,JSUnresolvedVariable

error = document.getElementById("error");
if (error !== null) {
    error.hidden = true;
}

//Login
//loginCookie();

//Cancel button
//const cancel = document.getElementById('cancel');
//if (cancel !== null) {
//    cancel.addEventListener('click', function () {
//        window.location.replace("/");
//    });
//}

//Dashboard sanitization
//dashBoard();

function getSkinURL(name) {
    return `https://cravatar.eu/helmavatar/${name}/190.png`
}

async function verifyCookie() {
    let blob = await fetch("/api/login/validate", {
        method: "post",
        body: getCookie("login_data")
    }).then(response => response.blob());

    return JSON.parse(await blob.text());
}

function setCookie(/*name, value*/name, value, days) {
    //window.localStorage.setItem(name, value);

    let expires = "";
    if (days) {
        const date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + (value || "") + expires + "; path=/";
}

function getCookie(name) {
    //return window.localStorage.getItem(name);

    const nameEQ = name + "=";
    const ca = document.cookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
}

// noinspection JSUnusedGlobalSymbols
function eraseCookie(name) {
    document.cookie = name + '=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

// noinspection JSUnusedGlobalSymbols
async function loginCookie() {
    let url;
    if (getCookie("login_data") !== null && getCookie("login_data") !== "" && getCookie("login_data") !== undefined) {

        try {
            if ((await verifyCookie()).code !== "200") {
                setCookie("login_data", "", 0)
            }

        } catch (error) {
            setCookie("login_data", "", 0)
        }


        //Login
        if (getCookie("login_data") !== null) {
            login = document.getElementById("login-button");

            let obj = JSON.parse(getCookie("login_data"));
            url = getSkinURL(obj.username);

            login.outerHTML = "" +
                "<img class='user-icon' src='" + url + "' onclick=profile('" + obj.username + "') alt='profile'> " +
                "<button class='top-btn login' id='logout-button' style='margin-left: 20px'>Logout</button>";
            document.getElementById("logout-button").addEventListener("click", () => {
                setCookie("login_data", "");
                location.reload();
            })
        }
    }
}

function profile(name) {
    window.location.replace(`/profile/?user=${name}`);
}

async function isLoggedIn() {
    if (getCookie("login_data") === null || getCookie("login_data") === undefined || getCookie("login_data") === "") {
        return false;
    }
    let verifier = await verifyCookie(getCookie("login_data"));
    // noinspection EqualityComparisonWithCoercionJS
    return verifier.code == 200;
}

// noinspection JSUnusedGlobalSymbols
async function checkLoggedStatus() {
    let loggedStatus = await isLoggedIn();
    if (!loggedStatus) {
        redirect("/401-login");
        return;
    }
    let body = document.getElementById("logged-in-required");
    if (body !== undefined && body !== null) {
        body.style.visibility = "visible";
    }
}

function redirect(path, removeQuotes = true) {
    if (removeQuotes) {
        path = path.replace(/'/g, "");
        path = path.replace(/"/g, "");
    }

    window.location.href = path
}

// noinspection JSUnusedGlobalSymbols
async function callPutAPI(api, data) {
    await fetch(api, {
        method: 'post',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
}

/**
 * @param api The api you want to call
 * @param data The body of the request
 * @param callback The successful callback (the argument is the data of the response)
 * @param failCallback The failed callback (the argument is the data itself)
 * @returns {Promise<void>}
 */
async function callAPI(api, data, callback, failCallback) {
    const blob = await fetch(api, {
        method: 'post',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(response => response.blob());

    let json = await blob.text();

    let obj;
    try {
        console.log("JSON received: " + json);
        obj = JSON.parse(json);

        if (obj.code !== "200") {
            if (failCallback === undefined) {
                error.hidden = false;
                error.innerText = obj.text;
                return;
            }
            failCallback(obj);
            return
        }
        callback(obj.data);
    } catch (error) {
        if (error !== undefined) {
            error.hidden = false;
        }
        if (failCallback === undefined) {
            error.innerText = obj.messageEn;
            return;
        }
        failCallback(obj);
    }
}
