package dev.lightdream.common.manager;

import dev.lightdream.common.database.Node;
import dev.lightdream.common.database.Server;
import dev.lightdream.common.database.User;
import dev.lightdream.common.dto.permission.Permission;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.dto.permission.PermissionTarget;
import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.databasemanager.database.ProgrammaticHikariDatabaseManager;
import dev.lightdream.databasemanager.dto.QueryConstrains;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DatabaseManager extends ProgrammaticHikariDatabaseManager {
    public DatabaseManager(DatabaseMain main) {
        super(main);
    }

    @Override
    public void setup() {

        registerDataType(Node.class, "TEXT");
        registerDataType(Server.class, "TEXT");
        registerDataType(PermissionEnum.class, "TEXT");

        registerSDPair(Node.class, node -> node.id, id -> getNode((Integer) id));
        registerSDPair(Server.class, server -> server.id, id -> getServer((Integer) id));
        registerSDPair(PermissionEnum.class, Enum::toString, str -> PermissionEnum.valueOf((String) str));
        registerSDPair(PermissionTarget.class, PermissionTarget::getPermissionIdentifier,
                str -> PermissionTarget.getByIdentifier((String) str));

        setup(Node.class);
        setup(Server.class);
        setup(Permission.class);
    }

    @SuppressWarnings("unused")
    public List<Node> getNodes() {
        return get(Node.class).query();
    }

    @SuppressWarnings("unused")
    @Nullable
    public Node getNode(int id) {
        return get(Node.class).query(
                        new QueryConstrains().equals("id", id)
                ).query()
                .stream().findAny().orElse(null);
    }

    @SuppressWarnings("unused")
    public List<Server> getServers() {
        return get(Server.class).query();
    }

    @SuppressWarnings("unused")
    @Nullable
    public Server getServer(int id) {
        return get(Server.class).query(
                        new QueryConstrains().equals("id", id)
                ).query()
                .stream().findAny().orElse(null);
    }

    /**
     * Mock user for testing username: "admin", password: "passwd", otp_secret: "UHPVYHCTF3LRTCGAHEJCX3MYTMRHPXPM"
     */
    @SuppressWarnings("unused")
    @Nullable
    public User getUser(int id) {
        return get(User.class).query(
                        new QueryConstrains().equals("id", id)
                ).query()
                .stream().findAny().orElse(null);
    }

    /**
     * Mock user for testing username: "admin", password: "passwd", otp_secret: "UHPVYHCTF3LRTCGAHEJCX3MYTMRHPXPM"
     */
    @SuppressWarnings("unused")
    public User getUser(String username) {
        return get(User.class).query(
                        new QueryConstrains().equals("username", username)
                ).query()
                .stream().findAny().orElse(null);
    }

    public List<Permission> getPermissions(User user, PermissionTarget target) {
        return get(Permission.class).query(
                new QueryConstrains().and(
                        new QueryConstrains().equals("user_id", user.id),
                        new QueryConstrains().equals("target", target.getPermissionIdentifier())
                )
        ).query();
    }

    public List<Permission> getPermissions(PermissionTarget target) {
        return get(Permission.class).query(
                new QueryConstrains().equals("target", target.getPermissionIdentifier())
        ).query();
    }

}
