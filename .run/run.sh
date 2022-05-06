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

screen -dmS $SCREEN_NAME_WEB -X stuff "^C"
sleep .5
screen -dmS $SCREEN_NAME_WEB -X stuff "^C"

# Go to the working directory
cd $HOME || exit

# Build the project
mvn package

echo "Coping the jar files to the .run directory"

# Copy jar to run folder
echo "cp $HOME/ControlPanel-Node/target/ControlPanel-Node-*.jar $HOME/.run/Node.jar"
echo "cp $HOME/ControlPanel-Web/target/ControlPanel-Web-*.jar $HOME/.run/Web.jar"

cp $HOME/ControlPanel-Web/target/ControlPanel-Web-*.jar $HOME/.run/Web.jar
cp $HOME/ControlPanel-Node/target/ControlPanel-Node-*.jar $HOME/.run/Node.jar

# Delete unused dirs
# TODO remove comment
# rm $HOME/ControlPanel-Common/target -r
# rm $HOME/ControlPanel-Web/target -r
# rm $HOME/ControlPanel-Node/target -r

# Go back to the working directory
cd "$CURRENT_DIR" || exit

echo "Starting Web App."

# Kill the existing processes running on ports for web
screen -S $SCREEN_NAME_WEB -X stuff "^C"
sleep .5
screen -S $SCREEN_NAME_WEB -X stuff "^C"
sleep .5
screen -XS $SCREEN_NAME_WEB kill

# Start the web server
screen -dmS $SCREEN_NAME_WEB -X stuff "java -jar $HOME/.run/Web.jar\n"

for i in 1 2 3 4 5; do
  echo "Starting node on htz-$i"

  # Execute unless the node is (1)
  if [ "$i" != "1" ]; then
    ssh root@htz$i.original.gg -o StrictHostKeyChecking=no -i $HOME/.run/config/ssh_keys/htz-$i "rm $HOME/.run/Node.jar"
  fi

  scp -i $HOME/.run/config/ssh_keys/htz-$i $HOME/.run/Node.jar root@htz$i.original.gg:$HOME/.run/Node.jar

  # Killing the current version (if running)
  ssh root@htz$i.original.gg -o StrictHostKeyChecking=no -i $HOME/.run/config/ssh_keys/htz-$i "screen -S $SCREEN_NAME_NODE -X stuff \"^C\""
  sleep .5
  ssh root@htz$i.original.gg -o StrictHostKeyChecking=no -i $HOME/.run/config/ssh_keys/htz-$i "screen -S $SCREEN_NAME_NODE -X stuff \"^C\""
  sleep .5
  ssh root@htz$i.original.gg -o StrictHostKeyChecking=no -i $HOME/.run/config/ssh_keys/htz-$i "screen -XS $SCREEN_NAME_NODE kill"

  # Start the node
  ssh root@htz$i.original.gg -o StrictHostKeyChecking=no -i $HOME/.run/config/ssh_keys/htz-$i "screen -dmS $SCREEN_NAME_NODE -X stuff \"java -jar $HOME/.run/Node.jar\n\""

done
