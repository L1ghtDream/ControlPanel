#!/bin/bash

REMOTE=root@htz1.original.gg
WORKING_DIR="/home/ControlPanel"

# Sync the files from the local machine to the remote machine
rsync -avh ../. $REMOTE:/$WORKING_DIR --delete

# Start the run.sh script on the remote machine
ssh $REMOTE 'sh '$WORKING_DIR'/.run/run.sh'