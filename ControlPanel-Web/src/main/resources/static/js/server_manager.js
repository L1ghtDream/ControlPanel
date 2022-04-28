registerEventListener("start", ()=>{
    sendCommandToIFrame("start");
})

registerEventListener("stop", ()=>{
    sendCommandToIFrame("stop");
})

registerEventListener("kill", ()=>{
    sendCommandToIFrame("__kill");
})