# Installing dependencies
sudo apt-get install screen lsof openjdk-11-jdk

# Get the start script and make it executable
wget https://raw.githubusercontent.com/L1ghtDream/ControlPanel/master/scripts/node/node_start.sh start.sh
chmod u+x start.sh

# Get the jar from GitHub releases
wget https://github.com/L1ghtDream/ControlPanel/releases/download/latest/ControlPanel-SFTP-1.0.jar ControlPanel-SFTP.jar

# Add to crontab