A web application that lets the server managers and developers more easily interact with server
instances. The panel is mainly aimed at java processes, but it may support other processes as well that use a port.
The panel lets the manager create new nodes ~ associated with new physical servers ~ as well as create new servers,
or instances of java. Each server has its own permissions for each user, so one developer can have access only to
what he needs to work on. The server allows created on SFTP servers on each node with custom credentials made with
the permissions of each user on each server. Each server is started in a linux screen so in case of a web crash the
servers will not have to suffer any damage. Each server can be accessed and has an open console (if the user has
permission to see it), as well as start / stop / restart / kill actions and stats about the server as CPU, Memory,
Storage usage and Memory allocation of the process (java only). As this is interacting directly with bare metal
the system administrator has the option to force all the users to have 2fa (via Google otp) enabled. 


# Build
```bash
mvn clean package spring-boot:repackage
```

# Install
[docs/install.md](https://github.com/L1ghtDream/ControlPanel/blob/master/docs/install.md)

# How it works?
[docs/how_it_works.md](https://github.com/L1ghtDream/ControlPanel/blob/master/docs/how_it_works.md)
