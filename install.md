# Requirements

Run

```shell
apt update
apt upgrade
```

# Server installation

Go to the folder your want to install the panel to and run

```shell
curl -s https://raw.githubusercontent.com/L1ghtDream/ControlPanel/master/scripts/web_install.sh | bash
sh start.sh
```

# Node Installation

Go to the folder your want to install the node to and run

```shell
curl -s https://raw.githubusercontent.com/L1ghtDream/ControlPanel/master/scripts/node_install.sh | bash
sh start.sh
```

You will need to create an ssh key for the node that you want to create and add in the folder /config/ssh_keys/[node_id]
where `node_id` is the id of the node you want to install.

## Creating an ssh key

All the bellow are created with the user `root` if you want to create another user just change the user in all the
commands.

### Creating ip bound ssh key

Do not forget the change the `[allowed_ip]` to the ip that you want to allow

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

