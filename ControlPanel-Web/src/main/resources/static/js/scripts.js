error = document.getElementById("error");
if (error !== null) {
    error.hidden = true;
}

//Login
verifyCookie();

//Cancel button
//const cancel = document.getElementById('cancel');
//if (cancel !== null) {
//    cancel.addEventListener('click', function () {
//        window.location.replace("/");
//    });
//}


//Dashboard sanitization
//dashBoard();

async function verifyCookie() {
    callAPI("/api/login/cookie", {}, () => {
    }, () => {
        eraseCookie("login_data")
    })
}

function setCookie(name, value, days) {
    let expires = "";
    if (days) {
        const date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + (value || "") + expires + "; path=/";
}

function getCookie(name) {
    const nameEQ = name + "=";
    const ca = document.cookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
}

function hasLoginDataInCookies() {
    let cookie = getCookie("login_data");
    return cookie != null && cookie !== ""
}

// noinspection JSUnusedGlobalSymbols
function eraseCookie(name) {
    document.cookie = name + '=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

// noinspection JSUnusedGlobalSymbols
async function loginCookie() {
    if (hasLoginDataInCookies()) {
        callAPI("/api/cookie-check", {}, () => {
        }, () => {
            eraseCookie("login_data");
            redirect("/login");
        });
    }
}

function redirect(path, removeQuotes = true) {
    if (removeQuotes) {
        path = path.replace(/'/g, "");
        path = path.replace(/"/g, "");
    }

    window.location.href = path
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

function reload() {
    location.reload();
}

function registerEventListener(object, callback, event = "click") {
    let element = document.getElementById(object);
    if (element == null) {
        console.log("Element not found: " + object);
        return;
    }
    element.addEventListener(event, callback);
}