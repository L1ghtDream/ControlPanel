#!/bin/bash

rm *.jar

# User vars
HOME=/home/ControlPanel
SCREEN_NAME_WEB=Web
SCREEN_NAME_NODE=Node
WEB_APP_PORT=13000

# Code vars
CURRENT_DIR=$(pwd)

# Kill the existing processes running on ports for web and node
# shellcheck disable=SC2046
kill $(lsof -t -i:"$WEB_APP_PORT")
screen -dmS $SCREEN_NAME_NODE -X stuff "^C"

# Go to the working directory
cd $HOME || exit

# Build the project
mvn package

# Copy jar to run folder
cp ControlPanel-Node/target/ControlPanel-Node-*.jar .run/Node.jar
cp ControlPanel-Web/target/ControlPanel-Web-*.jar .run/Web.jar

# Delete unused dirs
rm ControlPanel-Common/target -r
rm ControlPanel-Web/target -r
rm ControlPanel-Node/target -r

# Go back to the working directory
cd "$CURRENT_DIR" || exit

# Start the node and the web server
screen -dmS $SCREEN_NAME_WEB -X stuff "java -jar $HOME/.run/Web.jar\n"
screen -dmS $SCREEN_NAME_NODE -X stuff "java -jar $HOME/.run/Node.jar\n"

echo "Started Web App."
echo "Started Node."