function addStats() {
    setStat("test", "cpu_usage", stats => {
        document.getElementsByClassName("memory_usage")[0].innerHTML = memoryUsage;
        document.getElementsByClassName("memory_allocation")[0].innerHTML = memoryAllocation;
        document.getElementsByClassName("cpu_usage")[0].innerHTML = cpuUsage;
        document.getElementsByClassName("storage_usage")[0].innerHTML = storageUsage;
        document.getElementsByClassName("is_online")[0].innerHTML = isOnline;
    })
}

async function setStat(server, stat, callback) {
    switch (stat) {
        case "cpu_usage":
            getStats(server, callback);
            return;
    }
}

async function getStats(server, callback) {
    console.log("Getting CPU usage for " + server);
    callAPI("/api/stats/" + server, {}, callback);
}