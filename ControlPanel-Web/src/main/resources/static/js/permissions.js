serverID = document.getElementById("id").value;


async function removePermission(userID){
    callAPI("/api/server/" + serverID + "/permission/remove", {
        "id": userID,
    })
}