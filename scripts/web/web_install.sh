# Vars
version="1.0.0";

# Installing dependencies
sudo apt-get install screen                                                     # Web server
sudo apt-get install openjdk-8-jdk openjdk-11-jdk openjdk-16-jdk openjdk-17-jdk # Java
sudo apt-get install nginx                                                      # Web Proxy

# Get the start script and make it executable
wget https://raw.githubusercontent.com/L1ghtDream/ControlPanel/master/scripts/web/web_start.sh start.sh
chmod u+x start.sh

# Get the jar from GitHub releases
wget "https://github.com/L1ghtDream/ControlPanel/releases/download/latest/ControlPanel-Web-${version}.jar" -O ControlPanel-Web.jar

# Setup web-server
mkdir "/var/www/html/google_auth"
mkdir "/var/www/html/google_auth/qr"

# Add to crontab
