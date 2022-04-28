registerEventListener("start", () => {
    console.log("Sending start command from button")
    sendCommandToIFrame("start");
})

registerEventListener("stop", () => {
    console.log("Sending stop command from button")
    sendCommandToIFrame("stop");
})

registerEventListener("kill", () => {
    console.log("Sending kill(__kill) command from button")
    sendCommandToIFrame("__kill");
})