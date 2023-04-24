package dev.lightdream.common.database;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.databasemanager.dto.DatabaseEntry;
import dev.lightdream.logger.Debugger;
import jakarta.persistence.*;

@Entity
@Table(name = "permissions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id"})
        }
)
public class Permission extends DatabaseEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, length = 11)
    public Integer id;
    @JoinColumn(name = "user_id")
    public Integer user_id;
    @Column(name = "permission")
    public PermissionEnum permission;
    @Column(name = "target")
    public String target;
    @Column(name = "target_type")
    public PermissionEnum.Type targetType;

    @SuppressWarnings("unused")
    public Permission() {
        super(CommonMain.instance);
    }

    public Permission(User user, PermissionEnum permission, String target, PermissionEnum.Type targetType) {
        super(CommonMain.instance);
        this.user_id = user.id;
        this.permission = permission;
        this.target = target;
        this.targetType = targetType;
    }

    public String getTitle() {
        return permission.title;
    }

    public String getName() {
        return permission.name();
    }

    @SuppressWarnings("unused")
    public String getDescription() {
        return permission.description;
    }

    @Override
    public Integer getID() {
        return id;
    }

    @Override
    public void save() {
        if (!permission.getType().equals(targetType)) {
            Debugger.info("Permission type mismatch: " + permission.getType() + " != " + targetType + " for "
                    + permission + " @ " + target);
            return;
        }
        super.save();
    }
}
