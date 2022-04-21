#!/bin/bash

REMOTE=root@htz1.original.gg
WORKING_DIR="/home/ControlPanel"
WEB_PORT=13000
NODE_PORT=14000

# Kill the existing processes running on ports for web and node
ssh $REMOTE 'kill $(lsof -t -i:'$WEB_PORT')'
ssh $REMOTE 'kill $(lsof -t -i:'$NODE_PORT')'

# Sync the files from the local machine to the remote machine
rsync -avh ../. $REMOTE:/$WORKING_DIR --delete

# Start the run.sh script on the remote machine
ssh $REMOTE 'sh '$WORKING_DIR'/.run/run.sh'