#!/bin/bash

REMOTE=root@htz1.original.gg
WORKING_DIR="/home/ControlPanel"

# Sync the files from the local machine to the remote machine
rsync -avh -e "ssh -i config/ssh_keys/htz-1" ../. root@REMOTE:$WORKING_DIR --delete --exclude '.run/config'

# Stop all the currently running processes
sh kill-all.sh

# Start the run.sh script on the remote machine
ssh -i config/ssh_keys/htz-1 $REMOTE 'sh '$WORKING_DIR'/.run/run.sh'

# Start all the panel processes
sh start-all.sh
