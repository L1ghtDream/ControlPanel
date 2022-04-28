registerEventListener("console-input", (e) => {
    if (e.key === 'Enter') {
        console.log("1")
        sendCommand();
    }
}, "keypress");


registerEventListener("console-input", (e) => {
    if (e.key === 'ArrowUp') {
        commandHistoryIndex++;
        updateCommandHistory();
        setCommand(getCommand(commandHistoryIndex));
        e.preventDefault();
    }
    if (e.key === 'ArrowDown') {
        commandHistoryIndex--;
        updateCommandHistory();
        setCommand(getCommand(commandHistoryIndex));
        e.preventDefault();
    }
}, "keydown");

let commandHistory = [""];
let commandHistoryIndex = 0;

function updateCommandHistory() {
    commandHistory[0] = document.getElementById("console-input").value;
}

function setCommand(command = "") {
    document.getElementById("console-input").value = command;
}

/*
0 = Current command
1 = Previous command
*/
function getCommand(index = 0) {

    if (index <= 0) {
        index = 0;
        commandHistoryIndex = 0;
    }

    if (commandHistory.length <= index) {
        index = commandHistory.length - 1;
        commandHistoryIndex = index;
    }

    return commandHistory[index];
}

function registerCommandInMemory(command) {
    commandHistory.splice(1, 0, command)
}

function sendCommand() {
    updateCommandHistory();
    sendCommandToIFrame(getCommand())

    registerCommandInMemory(getCommand());
    setCommand();
}

function sendCommandToIFrame(command = "") {
    document.getElementById("console-iframe").contentWindow.sendMessage(command);
}