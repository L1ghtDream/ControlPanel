package dev.lightdream.common.sftp.utils;

import dev.lightdream.messagebuilder.MessageBuilder;

public class Globals {

    //TODO add config for session.log, start.sh, logfile flush 0
    public static final MessageBuilder SERVER_START_CMD = new MessageBuilder("cd %path%; screen -dmS %id% -L -Logfile session.log bash -c \"sh start.sh\"; screen -S %id% -X colon \"logfile flush 0^M\"");

}
