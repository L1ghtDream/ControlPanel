package dev.lightdream.common.dto.data.impl;

import dev.lightdream.common.database.Node;
import dev.lightdream.common.database.Server;
import dev.lightdream.common.dto.data.Validatable;
import dev.lightdream.common.dto.data.annotation.Validate;
import dev.lightdream.logger.Debugger;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ServerData extends Validatable {

    @Validate(validateMethod = "validateNode")
    public String nodeID;
    @Validate
    public String name;
    @Validate
    public String path;
    @Validate(validateMethod = "validatePort")
    public Integer port;
    @Validate(validateMethod = "validateJava")
    public String java;
    @Validate(validateMethod = "validateRAM")
    public String ram;
    @Validate(validateMethod = "validateJar")
    public String serverJar;
    @Validate(emptyAllowed = true)
    public String args;
    public boolean startIfOffline;

    @SuppressWarnings("unused")
    public boolean validatePort() {
        return port < 65536 &&
                port > 0;
    }

    @SuppressWarnings("unused")
    public boolean validateRAM() {
        return ram.matches("[0-9]+[KMG]");
    }

    @SuppressWarnings("unused")
    public boolean validateJar() {
        return serverJar.endsWith(".jar");
    }

    @SuppressWarnings("unused")
    public boolean validateJava() {
        return java.equals("8") ||
                java.equals("JDK_8") ||
                java.equals("17") ||
                java.equals("JDK_17") ||
                java.equals("16") ||
                java.equals("JDK_16") ||
                java.equals("11") ||
                java.equals("JDK_11");
    }


    @SuppressWarnings("unused")
    public boolean validateNode() {
        Debugger.log("Validating node with ID: " + nodeID);
        return Node.getNode(nodeID) != null;
    }

    public static class Create extends ServerData {
        @Validate(validateMethod = "validateID")
        public String id;

        @SuppressWarnings("unused")
        public boolean validateID() {
            return Server.getServer(id) == null;
        }
    }

    public static class Update extends ServerData {
        @Validate(validateMethod = "validateID")
        public String id;

        @SuppressWarnings("unused")
        public boolean validateID() {
            return Server.getServer(id) != null;
        }
    }

}
