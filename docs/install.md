# Requirements

Run

```shell
apt update
apt upgrade
```

# Server installation

Go to the folder your want to install the panel to and run

```shell
curl -s https://raw.githubusercontent.com/L1ghtDream/ControlPanel/master/scripts/web/web_install.sh | bash
sh start.sh
```

(Optional)
Now we are going to create an auto restart script. The example bellow is set to auto restart at 3:00 AM (local time)
```shell
crontab -e
```

select your favorite cli text editor (e.g. nano) if prompted

and add the following line at the end of the file. (save and exit)
```shell
0 3 * * * bash /home/ControlPanel/.run/restart.sh
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
curl -s https://raw.githubusercontent.com/L1ghtDream/ControlPanel/master/scripts/node/node_install.sh | bash
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

### Additional SSH Key information
You will want to copy the `id_rsa` to your web app /config/ssh_keys folder.
You can do this by the following command. Do not forge to replace `[password]`, `[ip]` and `[node_id]` with yours.
```shell
sshpass -p "[password]" scp -r root@[ip]:/home/ControlPanel/config/ssh_keys/[node_id] /root/.ssh/id_rsa
```



