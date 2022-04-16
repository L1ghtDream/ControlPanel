# Server installation

# SFTP Module installation

# Node Installation

You will need to create an ssh key for the node that you want to create and add in the folder /config/ssh_keys/[node_id]
where `node_id` is the id of the node you want to install.

## Creating an ssh key

All the bellow are created with the user `root` if you want to create another user just change the user in all the
commands.

### Creating ip bound ssh key

```shell
ssh-keygen -m PEM
echo "from=\"[allowed_ip]\" $(cat id_rsa.pub)" > authorized_keys
```

### Creating normal ssh key

```shell
ssh-keygen -m PEM
echo "$(cat id_rsa.pub)" > authorized_keys
```

# Server setup

# Admin user creation

# User setup


