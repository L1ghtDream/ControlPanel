package dev.lightdream.common.dto.config;

import dev.lightdream.messagebuilder.MessageBuilder;

public class CommonConfig {

    public boolean debug = false;

    public String sftpModuleInstallPath = "/home/ControlPanel/modules/SFTP";
    public String qrStorageLocation = "/var/www/html/google_auth/qr/";
    public String qrURL = "http://htz1.original.gg/google_auth/qr/";

    @SuppressWarnings("unused")
    public MessageBuilder sftpURL = new MessageBuilder(
            "sftp://%username%@%host%:%port%/"
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
            "jstat -gccapacity %pid% | tail -n 1 | awk '{print $2}'"
            //"pmap -x %pid% | tail -n 1 | awk '{print $3}'"
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

    public MessageBuilder STORAGE_USAGE_CMD = new MessageBuilder(
            "du -s %path% | awk '{print $1}'"
    );

    public MessageBuilder KILL_CMD = new MessageBuilder(
            "kill -9 $(lsof -t -i :%port%)"
    );


}



