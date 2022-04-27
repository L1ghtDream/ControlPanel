package dev.lightdream.controlpanel.dto;

import dev.lightdream.common.dto.BuildProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class NodeBuildProperties {
    public dev.lightdream.common.database.Node node;
    public BuildProperties build;
}
