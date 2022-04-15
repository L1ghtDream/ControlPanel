package dev.lightdream.common.dto;

import dev.lightdream.messagebuilder.MessageBuilder;

public class CommonConfig {

    public MessageBuilder SERVER_START_CMD = new MessageBuilder(
            "cd %path%; screen -dmS %id% -L -Logfile session.log bash -c \"sh start.sh\"; screen -S %id% -X colon \"logfile flush 0^M\""
    );
    public MessageBuilder SFTP_MODULE_INSTALL_CMD = new MessageBuilder(
            "mkdir %path%; cd %path%; wget %url% -O SFTP_module.jar"
    );

    public String sftpModuleInstallPath = "/home/ControlPanel/modules/SFTP";
    public String sftpModuleDownloadURL = "https://google.com";

}



