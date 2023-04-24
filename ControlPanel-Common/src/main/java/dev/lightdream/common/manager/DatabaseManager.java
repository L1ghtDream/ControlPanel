package dev.lightdream.common.manager;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.*;
import dev.lightdream.common.dto.data.impl.NodeData;
import dev.lightdream.common.dto.data.impl.ServerData;
import dev.lightdream.common.dto.permission.PermissionContainer;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.databasemanager.database.HibernateDatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends HibernateDatabaseManager {
    public DatabaseManager(DatabaseMain main) {
        super(main);
    }

    @Override
    protected List<Class<?>> getClasses() {
        return List.of(
                IPLog.class,
                Log.class,
                Node.class,
                Permission.class,
                Server.class,
                User.class
        );
    }


    public List<Node> getNodes() {
        return getAll(Node.class);
    }

    public Node getNode(String id) {
        Query<Node> query = get(Node.class);
        query.query.where(query.builder.equal(query.root.get("id"), id));
        return query.execute().stream().findAny().orElse(null);
    }

    public List<Server> getServers() {
        return getAll(Server.class);
    }

    /**
     * @param node The node to get the server from
     * @return The server of the node
     */
    public List<Server> getServers(Node node) {
        Query<Server> query = get(Server.class);
        query.query.where(query.builder.equal(query.root.get("node"), node.toString()));
        return query.execute();
    }

    public Server getServer(String id) {
        Query<Server> query = get(Server.class);
        query.query.where(query.builder.equal(query.root.get("id"), id));
        return query.execute().stream().findAny().orElse(null);
    }

    public User getUser(int id) {
        Query<User> query = get(User.class);
        query.query.where(query.builder.equal(query.root.get("id"), id));
        return query.execute().stream().findAny().orElse(null);
    }

    public User getUser(String username) {
        Query<User> query = get(User.class);
        query.query.where(query.builder.equal(query.root.get("username"), username));
        return query.execute().stream().findAny().orElse(null);
    }

    public List<Permission> getPermissions(User user, PermissionContainer target) {
        Query<Permission> query = get(Permission.class);
        query.query.where(
                query.builder.equal(query.root.get("user_id"), user.id),
                query.builder.equal(query.root.get("target"), target.getIdentifier())
        );
        return query.execute();
    }

    public List<Permission> getPermissions(PermissionContainer target) {
        Query<Permission> query = get(Permission.class);
        query.query.where(
                query.builder.equal(query.root.get("target"), target.getIdentifier())
        );
        return query.execute();
    }

    public void createServer(ServerData.Create data) {
        if (getServer(data.id) != null) {
            return;
        }

        new Server(
                data.id,
                data.name,
                data.path,
                Node.getNode(data.nodeID),
                data.port,
                data.java,
                data.ram,
                data.serverJar,
                data.args,
                data.startIfOffline
        ).save();
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
        Query<Permission> query = get(Permission.class);
        query.query.where(
                query.builder.equal(query.root.get("user_id"), user.id)
        );
        return query.execute();
    }

    public List<Server> getServers(User user) {
        List<Permission> permissions = getPermissions(user);
        List<Server> servers = new ArrayList<>();

        permissions.forEach(permission -> {
            if (permission.targetType.equals(PermissionEnum.Type.SERVER)) {
                Server server = CommonMain.instance.databaseManager.getServer(permission.target);
                if (servers.contains(server)) {
                    return;
                }
                servers.add(server);
            }
        });

        return servers;
    }

}
