function addStats() {
    serverIDs = document.getElementsByClassName("server_id");
    memoryUsages = document.getElementsByClassName("memory_usage");
    memoryAllocations = document.getElementsByClassName("memory_allocation");
    cpuUsages = document.getElementsByClassName("cpu_usage");
    storageUsages = document.getElementsByClassName("storage_usage");
    onlineStatuses = document.getElementsByClassName("in_online");

    setInterval(() => {
        console.log("Executing stats task for multiple servers");

        for (let i = 0; i < serverIDs.length; i++) {
            let serverID = serverIDs[i].classList[1];
            getStats(serverID, stats => {
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
    }, 20 * 1000); //20 seconds

}

function statsServerDetails() {
    console.log("Executing stats task for server " + serverID);
    getStats(serverID, stats => {
        console.log("Settings the stats for server " + serverID + " to " + JSON.stringify(stats));
        document.getElementById("ram-usage").innerText = formatMemory(stats.memoryUsage);
        document.getElementById("ram-allocation").innerText = formatMemory(stats.memoryAllocation);
        document.getElementById("cpu-usage").innerText = stats.cpuUsage + "%";
        let online_status = document.getElementById("online-status");
        if (stats.isOnline) {
            online_status.innerHTML = "" +
                "<div class=\"status-label lime-bg\">Online</div>"
        } else {
            online_status.innerHTML = "" +
                "<div class=\"status-label red-bg\">Offline</div>"
        }
    })
}

function addStatsServerDetails() {
    let serverID = document.getElementById("server-id").innerText;
    statsServerDetails();
    setInterval(statsServerDetails, 5 * 1000);// 5 seconds
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

async function getStats(server, callback) {
    callAPI("/api/stats/" + server, {}, callback);
}