package dev.lightdream.common.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ServerStats {

    public String serverID;
    public Double memoryUsage;
    public Double memoryAllocation;
    public Double cpuUsage;
    public Double storageUsage;
    public Boolean isOnline;

}
