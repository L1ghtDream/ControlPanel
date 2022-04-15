package dev.lightdream.common.sftp.manager;

import com.google.gson.Gson;
import dev.lightdream.common.sftp.database.Node;
import dev.lightdream.common.sftp.database.Server;
import dev.lightdream.common.sftp.database.User;
import dev.lightdream.common.sftp.dto.permission.Permission;
import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.databasemanager.database.ProgrammaticHikariDatabaseManager;

public class DatabaseManager extends ProgrammaticHikariDatabaseManager {
    public DatabaseManager(DatabaseMain main) {
        super(main);
    }

    @Override
    public void setup() {

        registerDataType(Node.class, "TEXT");
        registerDataType(Server.class, "TEXT");
        registerDataType(Permission.class, "TEXT");

        registerSDPair(Node.class, node -> node.id, id -> getNode((Integer) id));
        registerSDPair(Server.class, server -> server.id, id -> getServer((Integer) id));
        registerSDPair(Permission.class, perm -> new Gson().toJson(perm.toGsonCompatible()),
                string -> new Gson().fromJson((String) string, Permission.GsonPermission.class).toPermission());

        setup(Node.class);
        setup(Server.class);
    }

    @SuppressWarnings("unused")
    public Node getNode(int id) {
        return null; //todo
    }

    @SuppressWarnings("unused")
    public Server getServer(int id) {
        return null; //todo
    }

    @SuppressWarnings("unused")
    public User getUser(int id) {
        return null; //todo
    }

    @SuppressWarnings("unused")
    public User getUser(String username) {
        //TODO
        User user = new User(
                "admin",
                "passwd",
                "UHPVYHCTF3LRTCGAHEJCX3MYTMRHPXPM"
        );
        user.id = 1;
        return user;
    }

}
