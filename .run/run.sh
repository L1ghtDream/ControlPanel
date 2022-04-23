#!/bin/bash

rm *.jar

# Vars
HOME=/home/ControlPanel
CURRENT_DIR=$(pwd)

# Go to the working directory
cd $HOME

# Build the project
mvn package

# Copy jar to run folder
cp ControlPanel-Node/target/ControlPanel-Node-1.0.jar .run/Node.jar
cp ControlPanel-Web/target/ControlPanel-Web-1.0.jar .run/Web.jar

# Delete unused dirs
rm ControlPanel-Common/target -r
rm ControlPanel-Web/target -r
rm ControlPanel-Node/target -r

# Go back to the working directory
cd $CURRENT_DIR