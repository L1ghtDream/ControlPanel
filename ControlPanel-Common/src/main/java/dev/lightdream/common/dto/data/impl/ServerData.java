package dev.lightdream.common.dto.data.impl;

import dev.lightdream.common.database.Node;
import dev.lightdream.common.dto.data.Validatable;
import dev.lightdream.common.dto.data.annotation.Validate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ServerData extends Validatable {

    @Validate
    public String id;
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

    public boolean validateRAM() {
        return ram.matches("[0-9]+[KMG]");
    }

    public boolean validateJar() {
        return serverJar.endsWith(".jar");
    }

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

    public boolean validateNode(){
        return Node.getNode(nodeID) != null;
    }

}
