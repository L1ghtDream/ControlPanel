package dev.lightdream.common.database;

import dev.lightdream.common.CommonMain;
import dev.lightdream.databasemanager.database.HibernateDatabaseManager;
import dev.lightdream.databasemanager.dto.DatabaseEntry;
import jakarta.persistence.*;

@Entity
@Table(name = "ip_logs",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id"})
        }
)
public class IPLog extends DatabaseEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, length = 11)
    public Integer id;
    @Column(name = "timestamp")
    public Long timestamp;
    @Column(name = "ip")
    public String ip;
    @Column(name = "user")
    public String user;
    @Column(name = "password")
    public String password;

    public IPLog(Long timestamp, String user, String password, String ip) {
        super(CommonMain.instance);
        this.timestamp = timestamp;
        this.user = user;
        this.password = password;
        this.ip = ip;

        HibernateDatabaseManager.Query<IPLog> query = CommonMain.instance.databaseManager.get(IPLog.class);
        query.query.where(
                query.builder.equal(query.root.get("ip"), ip),
                query.builder.equal(query.root.get("user"), user)
        );
        if (query.execute().size() == 0) {
            save();
        }
    }

    public IPLog() {
        super(CommonMain.instance);
    }

    @Override
    public Integer getID() {
        return id;
    }
}
