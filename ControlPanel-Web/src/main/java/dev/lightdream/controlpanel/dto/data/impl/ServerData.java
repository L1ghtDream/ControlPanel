package dev.lightdream.controlpanel.dto.data.impl;

import dev.lightdream.controlpanel.dto.data.Validatable;
import dev.lightdream.controlpanel.dto.data.annotation.Validate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ServerData extends Validatable {

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
    private boolean validatePort() {
        return port < 65536 &&
                port > 0;
    }

    private boolean validateRAM(){
        return ram.matches("[0-9]+[KMG]");
    }

    private boolean validateJar(){
        return serverJar.endsWith(".jar");
    }

}
