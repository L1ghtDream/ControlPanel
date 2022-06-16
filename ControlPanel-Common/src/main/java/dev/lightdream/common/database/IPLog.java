package dev.lightdream.common.database;

import dev.lightdream.common.CommonMain;
import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.QueryConstrains;
import dev.lightdream.databasemanager.dto.entry.impl.IntegerDatabaseEntry;

import java.util.stream.Collectors;

@DatabaseTable(table = "ip_logs")
public class IPLog extends IntegerDatabaseEntry {

    @DatabaseField(columnName = "timestamp")
    public Long timestamp;
    @DatabaseField(columnName = "ip")
    public String ip;
    @DatabaseField(columnName = "user")
    public String user;
    @DatabaseField(columnName = "password")
    public String password;

    public IPLog(Long timestamp, String user, String password, String ip) {
        super(CommonMain.instance);
        this.timestamp = timestamp;
        this.user = user;
        this.password = password;
        this.ip = ip;

        if (CommonMain.instance.databaseManager.get(IPLog.class)
                .query(
                        new QueryConstrains().and(
                                new QueryConstrains().equals("ip", ip),
                                new QueryConstrains().equals("user", user)
                        )
                ).query().size() == 0) {
            save();
        }
    }

    public IPLog() {
        super(CommonMain.instance);
    }
}
