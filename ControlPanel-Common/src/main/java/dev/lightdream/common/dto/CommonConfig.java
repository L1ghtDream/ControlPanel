package dev.lightdream.common.dto;

import dev.lightdream.messagebuilder.MessageBuilder;

public class CommonConfig {

    public boolean debug = false;

    public MessageBuilder SERVER_START_CMD = new MessageBuilder(
            "cd %path%; screen -dmS %id% -L -Logfile session.log bash -c \"sh start.sh\"; screen -S %id% -X colon \"logfile flush 0^M\""
    );
    public MessageBuilder SFTP_MODULE_INSTALL_CMD = new MessageBuilder(
            "mkdir %path%; cd %path%; wget %url% -O SFTP_module.jar"
    );

    public String sftpModuleInstallPath = "/home/ControlPanel/modules/SFTP";
    public MessageBuilder sftpModuleDownloadURL = new MessageBuilder(
            "https://github.com/L1ghtDream/ControlPanel/releases/download/latest/ControlPanel-SFTP-%version%.jar"
    );

    public MessageBuilder oneClickSFTPURL = new MessageBuilder(
            "sftp://username:password@host:port/"
    );

    public MessageBuilder passwordSFTPURL = new MessageBuilder(
            "sftp://username@host:port/"
    );

    public MessageBuilder installNode = new MessageBuilder(
            "sftp://username@host:port/"
    );

    public String installScriptURL = "https://raw.githubusercontent.com/L1ghtDream/ControlPanel/master/scripts/node_install.sh";
}



