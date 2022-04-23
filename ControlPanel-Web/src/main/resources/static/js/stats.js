function addStats() {
    serverIDs = document.getElementsByClassName("server_id");
    memoryUsages = document.getElementsByClassName("memory_usage");
    memoryAllocations = document.getElementsByClassName("memory_allocation");
    cpuUsages = document.getElementsByClassName("cpu_usage");
    storageUsages = document.getElementsByClassName("storage_usage");
    onlineStatuses = document.getElementsByClassName("in_online");

    for (let i = 0; i < serverIDs.length; i++) {
        let serverID = serverIDs[i].classList[1];
        setStat(serverID, "cpu_usage", stats => {
            console.log("Settings the stats for server " + serverID + "(" + i + ") to " + JSON.stringify(stats));
            memoryUsages[i].innerHTML = formatMemory(stats.memoryUsage);
            memoryAllocations[i].innerHTML = formatMemory(stats.memoryAllocation);
            cpuUsages[i].innerHTML = stats.cpuUsage + "%";
            storageUsages[i].innerHTML = formatMemory(stats.storageUsage);
            if (stats.isOnline) {
                onlineStatuses[i].innerHTML = "" +
                    "<div class=\"bar\"\n style=\"background: #8bc34a !important; box-shadow: 0 0 10px #8bc34a !important; \"\n></div>"
            } else {
                onlineStatuses[i].innerHTML = "" +
                    "<div class=\"bar\"\n style=\"background: #f44336 !important; box-shadow: 0 0 10px #f44336 !important; \"\n></div>"
            }
        })
    }


}

function formatMemory(memory) {
    if (memory < 1024) {
        return memory + " KB";
    }
    if (memory < 1024 * 1024) {
        return (memory / 1024).toFixed(2) + " MB";
    }
    if (memory < 1024 * 1024 * 1024) {
        return (memory / 1024 / 1024).toFixed(2) + " GB";
    }
    return (memory / 1024 / 1024 / 1024).toFixed(2) + " TB";
}

async function setStat(server, stat, callback) {
    switch (stat) {
        case "cpu_usage":
            getStats(server, callback);
            return;
    }
}

async function getStats(server, callback) {
    callAPI("/api/stats/" + server, {}, callback);
}