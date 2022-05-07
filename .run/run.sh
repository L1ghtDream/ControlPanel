#!/bin/bash

rm *.jar

# User vars
HOME=/home/ControlPanel

# Go to the working directory
cd $HOME || exit

# Build the project
mvn package

echo "Coping the jar files to the .run directory"

# Copy jar to run folder
cp $HOME/ControlPanel-Web/target/ControlPanel-Web-*.jar $HOME/.run/Web.jar
cp $HOME/ControlPanel-Node/target/ControlPanel-Node-*.jar $HOME/.run/Node.jar

# Delete unused dirs
rm $HOME/ControlPanel-Common/target -r
rm $HOME/ControlPanel-Web/target -r
rm $HOME/ControlPanel-Node/target -r

for i in 1 2 3 4 5; do

  echo "Sending necessary jars to htz-$i"

  ssh root@htz$i.original.gg -o StrictHostKeyChecking=no -i $HOME/.run/config/ssh_keys/htz-$i "mkdir -p $HOME/.run"

  if [ "$i" != "1" ]; then
    ssh root@htz$i.original.gg -o StrictHostKeyChecking=no -i $HOME/.run/config/ssh_keys/htz-$i "rm $HOME/.run/Node.jar"
  fi

  scp -i $HOME/.run/config/ssh_keys/htz-$i $HOME/.run/Node.jar root@htz$i.original.gg:$HOME/.run/Node.jar
done
