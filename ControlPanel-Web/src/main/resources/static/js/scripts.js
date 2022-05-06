// noinspection JSIgnoredPromiseFromCall,ES6MissingAwait,JSCheckFunctionSignatures

error = document.getElementById("error");
if (error !== null) {
    error.hidden = true;
}
let user;

//Login
verifyCookie();

async function verifyCookie() {
    if (document.title.includes("Login")) {
        return;
    }

    if (!hasLoginDataInCookies()) {
        redirect("/");
    }

    callAPI("/api/login/cookie", {}, () => {
    }, () => {
        eraseCookie("login_data")
        redirect("/");
    })

    let cookie = getCookie("login_data");
    if (cookie === null || cookie === "" || cookie === undefined) {
        return;
    }

    console.log("[base64] cookie: " + cookie);
    console.log("[ascii ] cookie: " + cookie);

    user = JSON.parse(atob(cookie));
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
        callAPI("/api/login/cookie", {}, () => {
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
    console.log("Calling API: " + api + " with data: " + JSON.stringify(data));

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
                errorNotification(obj.text + ". " + obj.data);
                return;
            }
            failCallback(obj);
            return
        }
        callback(obj.data);
    } catch (error) {
        if (failCallback === undefined) {
            errorNotification(obj.text + ". " + obj.data);
            return;
        }
        failCallback(obj);
    }
}

function reload() {
    location.reload();
}

function registerEventListener(objectID, callback, event = "click") {
    let element = document.getElementById(objectID);
    if (element == null) {
        console.log("Element not found: " + objectID);
        return;
    }
    element.addEventListener(event, callback);
}

function openTab(url, focus = false) {
    if (focus) {
        window.open(url, '_blank').focus();
        return;
    }
    window.open(url, '_blank');
}

function errorNotification(text) {
    new Notification({
        text: text,
        showProgress: false,
        style: {
            background: '#DC2626',
            color: '#ffffff',
            transition: 'all 350ms linear',
        },
    });
}
