serverID = document.getElementById("id").value;

registerEventListener("add-user", () => {
    let username = document.getElementById("username").value;

    callAPI("/api/user/get/id/" + username, {}, data => {
        let user_ID = data.id;

        let permissionsMap = {}

        for (let element in document.getElementsByClassName("permission-input")) {
            if(!element.id.endsWith("_new")){
                continue;
            }
            permissionsMap[element.id.replace("_new", "")] = document.getElementById(element.id).checked;
        }

        callAPI("/api/server/"+serverID + "/permissions/update", {
            userID: user_ID,
            permissions: permissionsMap
        })
    })
})

async function removePermission(userID) {
    callAPI("/api/server/" + serverID + "/permission/remove", {
        "id": userID,
    })
}