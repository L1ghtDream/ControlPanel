# Vars
version="1.0.0";

# Installing dependencies
sudo apt-get install screen lsof sshpass                                               # Required for servers
sudo apt-get install openjdk-8-jdk openjdk-11-jdk openjdk-16-jdk openjdk-17-jdk # Java

# Get the start script and make it executable
wget https://raw.githubusercontent.com/L1ghtDream/ControlPanel/master/scripts/node/node_start.sh start.sh
chmod u+x start.sh

# Get the jar from GitHub releases
wget "https://github.com/L1ghtDream/ControlPanel/releases/download/latest/ControlPanel-Node-${version}.jar" -O ControlPanel-Node.jar

# Add to crontab