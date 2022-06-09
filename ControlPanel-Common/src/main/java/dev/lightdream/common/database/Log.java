package dev.lightdream.common.database;

import dev.lightdream.common.CommonMain;
import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.entry.impl.IntegerDatabaseEntry;

@DatabaseTable(table = "logs")
public class Log extends IntegerDatabaseEntry {

    @DatabaseField(columnName = "type")
    public String type;
    @DatabaseField(columnName = "timestamp")
    public Long timestamp;
    @DatabaseField(columnName = "user")
    public String user;
    @DatabaseField(columnName = "password")
    public String password;
    @DatabaseField(columnName = "server")
    public String server;
    @DatabaseField(columnName = "log")
    public String log;

    public Log(String type, Long timestamp, String user, String password, String server, String log) {
        super(CommonMain.instance);
        this.type = type;
        this.timestamp = timestamp;
        this.user = user;
        this.password = password;
        this.server = server;
        this.log = log;
        save();
    }

    public Log() {
        super(CommonMain.instance);
    }
}
