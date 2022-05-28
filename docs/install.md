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

## Web Proxy

### Certbot Certificates

### Nginx Configuration
```nginx
map $http_upgrade $connection_upgrade {
    default upgrade;
    '' close;
}

upstream websocket {
    server <ip-to-web-app-server>:13000;
}

server {
    server_name <domain>;

    location / {
        proxy_pass http://websocket;   
        proxy_http_version 1.1;
	    
        proxy_set_header        Host $host;
        proxy_set_header        X-Real-IP $remote_addr;
        proxy_set_header        X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header        X-Forwarded-Proto $scheme;
        proxy_set_header        Origin "";
        proxy_set_header        Upgrade $http_upgrade;  
	    proxy_set_header        Connection $connection_upgrade;

        proxy_read_timeout  90;
    }

    listen 443 ssl;
    ssl_certificate /etc/letsencrypt/live/<domain>/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/<domain>/privkey.pem;
}

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

