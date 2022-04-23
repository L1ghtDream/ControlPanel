package dev.lightdream.common.dto;

import dev.lightdream.common.database.Server;
import dev.lightdream.common.utils.Utils;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ServerStats {

    public String serverID;
    public Double memoryUsage;
    public Double memoryAllocation;
    public Double cpuUsage;
    public Double storageUsage;
    public Boolean isOnline;

    public ServerStats(Server server, Double memoryUsage, Double memoryAllocation, Double cpuUsage, Double storageUsage, Boolean isOnline) {
        this.serverID = server.getID();
        this.memoryUsage = memoryUsage;
        this.memoryAllocation = memoryAllocation;
        this.cpuUsage = cpuUsage;
        this.storageUsage = storageUsage;
        this.isOnline = isOnline;
    }

    @Override
    public String toString() {
        return Utils.toJson(this);
    }
}
