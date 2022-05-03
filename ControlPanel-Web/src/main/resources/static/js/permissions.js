// noinspection JSIgnoredPromiseFromCall,ES6MissingAwait,JSCheckFunctionSignatures

serverID = document.getElementById("id").innerText;

registerEventListener("add-user", () => {
    let username = document.getElementById("username").value;
    console.log(username);

    callAPI("/api/user/get/id/" + username, {}, data => {
        let user_ID = data;
        console.log(user_ID);

        let permissionsMap = {}

        console.log(1);
        console.log(document.getElementsByClassName("permission-input"))

        let elements = document.getElementsByClassName("permission-input");

        for (let i = 0; i < elements.length; i++) {
            element = elements[i];
            console.log(2);
            console.log(element);
            console.log(element.id);
            if (!element.id.endsWith("_new")) {
                console.log(3);
                continue;
            }
            console.log(4);
            permissionsMap[element.id.replace("_new", "")] = document.getElementById(element.id).checked;
        }

        console.log(5);
        console.log({
            userID: user_ID,
            permissions: permissionsMap
        });

        console.log(6);
        callAPI("/api/server/" + serverID + "/permissions/update", {
            userID: user_ID,
            permissions: permissionsMap
        }, () => {
            reload();
        })
    })
})

for (let i = 0; i < document.getElementsByClassName("permission-input-update").length; i++) {
    let element = document.getElementsByClassName("permission-input-update")[i];
    registerEventListener(element.id, () => {
        let permission = element.id.split("__")[0];
        let user_ID = element.id.split("__")[1];

        let permissionsMap = {}

        permissionsMap[permission] = document.getElementById(element.id).checked;

        callAPI("/api/server/" + serverID + "/permissions/update", {
            userID: user_ID,
            permissions: permissionsMap
        }, () => {
            reload();
        })
    }, "change")
}


// noinspection JSUnusedGlobalSymbols
async function removePermission(userID) {
    callAPI("/api/server/" + serverID + "/permissions/remove", {
        "id": userID,
    })
}