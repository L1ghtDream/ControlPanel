function addStats() {
    setStat("test", "cpu_usage", cpuUsage => {
        document.getElementsByClassName("cpu_usage")[0].innerHTML = cpuUsage;
    })
}

async function setStat(server, stat, callback) {
    switch (stat) {
        case "cpu_usage":
            getCpuUsage(server, callback);
            return;
    }
}

async function getCpuUsage(server, callback) {
    console.log("Getting CPU usage for " + server);
    callAPI("/api/stats/" + server, {}, callback);
}