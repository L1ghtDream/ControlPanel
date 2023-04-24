package dev.lightdream.common.database;

import dev.lightdream.common.CommonMain;
import dev.lightdream.databasemanager.dto.DatabaseEntry;
import jakarta.persistence.*;

@Entity
@Table(name = "logs",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id"})
        }
)
public class Log extends DatabaseEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, length = 11)
    public Integer id;
    @Column(name = "type")
    public String type;
    @Column(name = "timestamp")
    public Long timestamp;
    @Column(name = "user")
    public String user;
    @Column(name = "password")
    public String password;
    @Column(name = "server")
    public String server;
    @Column(name = "log")
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

    @Override
    public Integer getID() {
        return id;
    }
}
