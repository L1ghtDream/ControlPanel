package dev.lightdream.common.manager;

import com.google.common.hash.Hashing;
import dev.lightdream.common.database.Node;
import dev.lightdream.common.database.Server;
import dev.lightdream.common.database.User;
import dev.lightdream.common.dto.permission.Permission;
import dev.lightdream.common.dto.permission.PermissionContainer;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.databasemanager.database.ProgrammaticHikariDatabaseManager;
import dev.lightdream.databasemanager.dto.QueryConstrains;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class DatabaseManager extends ProgrammaticHikariDatabaseManager {
    public DatabaseManager(DatabaseMain main) {
        super(main);
    }

    @Override
    public void setup() {

        registerDataType(Node.class, "INT");
        registerDataType(User.class, "INT");

        registerDataType(Server.class, "TEXT");
        registerDataType(PermissionEnum.class, "TEXT");
        registerDataType(PermissionContainer.class, "TEXT");

        registerSDPair(Node.class, node -> node.id, id -> getNode((Integer) id));
        registerSDPair(User.class, user -> user.id, id -> getUser((Integer) id));
        registerSDPair(Server.class, server -> server.id, id -> getServer((Integer) id));
        registerSDPair(PermissionEnum.class, Enum::toString, str -> PermissionEnum.valueOf((String) str));
        registerSDPair(PermissionContainer.class, PermissionContainer::getPermissionIdentifier,
                str -> PermissionContainer.getByIdentifier((String) str));

        setup(Node.class);
        setup(Server.class);
        setup(Permission.class);
        setup(User.class);
    }

    public List<Node> getNodes() {
        return get(Node.class).query();
    }

    @Nullable
    public Node getNode(int id) {
        return get(Node.class).query(
                        new QueryConstrains().equals("id", id)
                ).query()
                .stream().findAny().orElse(null);
    }

    public Node getNode(String nodeID) {
        return get(Node.class).query(
                        new QueryConstrains().equals("node_id", nodeID)
                ).query()
                .stream().findAny().orElse(null);
    }

    public List<Server> getServers() {
        return get(Server.class).query();
    }

    @Nullable
    public Server getServer(int id) {
        return get(Server.class).query(
                        new QueryConstrains().equals("id", id)
                ).query()
                .stream().findAny().orElse(null);
    }

    public Server getServer(String serverID) {
        return get(Server.class).query(
                        new QueryConstrains().equals("server_id", serverID)
                ).query()
                .stream().findAny().orElse(null);
    }

    @Nullable
    public User getUser(int id) {
        return get(User.class).query(
                        new QueryConstrains().equals("id", id)
                ).query()
                .stream().findAny().orElse(null);
    }

    public User getUser(String username) {
        return get(User.class).query(
                        new QueryConstrains().equals("username", username)
                ).query()
                .stream().findAny().orElse(null);
    }

    public List<Permission> getPermissions(User user, PermissionContainer target) {
        return get(Permission.class).query(
                new QueryConstrains().and(
                        new QueryConstrains().equals("user_id", user.id),
                        new QueryConstrains().equals("target", target.getPermissionIdentifier())
                )
        ).query();
    }

    public List<Permission> getPermissions(PermissionContainer target) {
        return get(Permission.class).query(
                new QueryConstrains().equals("target", target.getPermissionIdentifier())
        ).query();
    }

    public void createUser(String username, String password, String otpSecret) {
        if (getUser(username) != null) {
            return;
        }

        new User(
                username,
                Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString(),
                otpSecret
        ).save();
    }


    public void createServer(String serverID, String name, String path, Node node, int port) {
        if (getServer(serverID) != null) {
            return;
        }

        new Server(serverID, name, path, node, port).save();
    }


    public void createNode(String nodeID, String name, String ip, String username, int sshPort) {
        if (getNode(nodeID) != null) {
            return;
        }

        new Node(nodeID, name, ip, username, sshPort).save();
    }
}
