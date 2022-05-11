package dev.lightdream.common.manager;

import dev.lightdream.common.database.*;
import dev.lightdream.common.dto.data.impl.NodeData;
import dev.lightdream.common.dto.data.impl.ServerData;
import dev.lightdream.common.dto.permission.PermissionContainer;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.databasemanager.database.ProgrammaticHikariDatabaseManager;
import dev.lightdream.databasemanager.dto.QueryConstrains;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends ProgrammaticHikariDatabaseManager {
    public DatabaseManager(DatabaseMain main) {
        super(main);
    }

    @Override
    public void setup() {

        registerDataType(Node.class, "TEXT");
        registerDataType(User.class, "INT");
        registerDataType(Server.class, "TEXT");
        registerDataType(PermissionEnum.class, "TEXT");
        registerDataType(GlobalPermissionContainer.class, "TEXT");
        registerDataType(PermissionContainer.class, "TEXT");

        registerSDPair(User.class, user -> user.id, id -> getUser((Integer) id));
        registerSDPair(Node.class, node -> "\"" + node.id + "\"", id -> Node.getNode((String) id));
        registerSDPair(Server.class, server -> "\"" + server.getIdentifier() + "\"", id -> (Server) Server.getByIdentifier((String) id));
        registerSDPair(PermissionEnum.class, permission -> "\"" + permission.toString() + "\"", str -> PermissionEnum.valueOf((String) str));
        registerSDPair(GlobalPermissionContainer.class, permission -> "\"" + permission.toString() + "\"", str -> GlobalPermissionContainer.getInstance());
        registerSDPair(PermissionContainer.class, PermissionContainer::getIdentifier,
                str -> PermissionContainer.getByIdentifier((String) str));

        setup(Node.class);
        setup(Server.class);
        setup(Permission.class);
        setup(User.class);
    }

    public List<Node> getNodes() {
        return get(Node.class).query();
    }

    public Node getNode(String id) {
        return get(Node.class).query(
                        new QueryConstrains().equals("id", id)
                ).query()
                .stream().findAny().orElse(null);
    }

    public List<Server> getServers() {
        return get(Server.class).query();
    }

    /**
     * @param node The node to get the server from
     * @return The server of the node
     */
    public List<Server> getServers(Node node) {
        return get(Server.class)
                .query(new QueryConstrains()
                        .equals("node", node.toString())
                ).query();
    }

    public Server getServer(String id) {
        return get(Server.class).query(
                        new QueryConstrains().equals("id", id)
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
        if (username == null) {
            return null;
        }

        return get(User.class).query(
                        new QueryConstrains().equals("username", username)
                ).query()
                .stream().findAny().orElse(null);
    }

    public List<Permission> getPermissions(User user, PermissionContainer target) {
        return get(Permission.class).query(
                new QueryConstrains().and(
                        new QueryConstrains().equals("user_id", user.id),
                        new QueryConstrains().equals("target", target.getIdentifier())
                )
        ).query();
    }

    public List<Permission> getPermissions(PermissionContainer target) {
        return get(Permission.class).query(
                new QueryConstrains().equals("target", target.getIdentifier())
        ).query();
    }

    public void createUser(String username, String password) {
        if (getUser(username) != null) {
            return;
        }

        new User(username, password).save();
    }

    /**
     * @param name      The server's name
     * @param path      The server's path on the machine
     * @param node      The server's node (machine)
     * @param port      The server's port
     * @param java      The server's java version (JDK_8, JDK_11, JDK_16, JDK_17)
     * @param ram       The server's max ram
     * @param serverJar The server's jar file (server.jar)
     */
    public void createServer(String serverID, String name, String path, Node node, int port, String java, String ram,
                             String serverJar, String args, boolean startIfOffline) {
        if (getServer(serverID) != null) {
            return;
        }

        new Server(serverID, name, path, node, port, java, ram, serverJar, args, startIfOffline).save();
    }

    public void createServer(ServerData.Create data) {
        if (getServer(data.id) != null) {
            return;
        }

        new Server(data.id, data.name, data.path, Node.getNode(data.nodeID), data.port, data.java, data.ram,
                data.serverJar, data.args, data.startIfOffline).save();
    }


    public void createNode(String nodeID, String name, String ip, String username, int sshPort, int sftpPort) {
        if (getNode(nodeID) != null) {
            return;
        }

        new Node(nodeID, name, ip, username, sshPort, sftpPort).save();
    }

    public void createNode(NodeData.Create data) {
        createNode(data.id, data.name, data.ip, data.username, data.sshPort, data.sftpPort);
    }

    public List<Permission> getPermissions(User user) {
        return get(Permission.class).query(
                new QueryConstrains().equals("user_id", user.id)
        ).query();
    }

    public List<Server> getServers(User user) {
        List<Permission> permissions = getPermissions(user);
        List<Server> servers = new ArrayList<>();

        permissions.forEach(permission -> {
            if (permission.target.getType().equals(PermissionEnum.Type.SERVER)) {
                Server server = (Server) permission.target;
                if (servers.contains(server)) {
                    return;
                }
                servers.add(server);
            }
        });

        return servers;
    }

}
