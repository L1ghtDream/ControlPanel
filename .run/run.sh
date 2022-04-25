#!/bin/bash

rm *.jar

# Vars
HOME=/home/ControlPanel
CURRENT_DIR=$(pwd)

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