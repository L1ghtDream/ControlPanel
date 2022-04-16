package dev.lightdream.common.dto;

import dev.lightdream.messagebuilder.MessageBuilder;

public class CommonConfig {

    public boolean debug = false;

    public String sftpModuleInstallPath = "/home/ControlPanel/modules/SFTP";

    @SuppressWarnings("unused")
    public MessageBuilder sftpURL = new MessageBuilder(
            "sftp://username@host:port/"
    );

    // ---------- Urls ----------

    public String nodeInstallScriptURL = "https://raw.githubusercontent.com/L1ghtDream/ControlPanel/master/scripts/node_install.sh";
    public MessageBuilder sftpModuleDownloadURL = new MessageBuilder(
            "https://github.com/L1ghtDream/ControlPanel/releases/download/latest/ControlPanel-SFTP-%version%.jar"
    );

    // ---------- Linux Commands ----------

    public MessageBuilder SERVER_START_CMD = new MessageBuilder(
            "cd %path%; screen -dmS %id% -L -Logfile session.log bash -c \"sh start.sh\"; screen -S %id% -X colon \"logfile flush 0^M\""
    );

    public MessageBuilder SFTP_MODULE_INSTALL_CMD = new MessageBuilder(
            "mkdir %path%; cd %path%; wget %url% -O SFTP_module.jar"
    );

    public MessageBuilder MEMORY_USAGE_CMD = new MessageBuilder(
            "pmap -x %pid% | tail -n 1 | awk '{print $4}'"
    );

    public MessageBuilder MEMORY_ALLOCATED_CMD = new MessageBuilder(
            "pmap -x %pid% | tail -n 1 | awk '{print $3}'"
    );

    public MessageBuilder PID_GRAB_CMD = new MessageBuilder(
            "lsof -i :%port% | grep LISTEN | awk '{print $2}'"
    );

    public MessageBuilder CPU_USAGE_CMD = new MessageBuilder(
            "top -b -n 1 -p %pid% | tail -n 1 | awk '{print $9}'"
    );

    public MessageBuilder EXECUTE_SCRIPT_CMD = new MessageBuilder(
            "curl -s %url% | bash"
    );

}



