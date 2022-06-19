#!/bin/bash

REMOTE=root@htz1.original.gg
HOME="/home/ControlPanel"

ssh -i config/ssh_keys/htz-1 $REMOTE 'mkdir -p '$HOME''

# Sync the files from the local machine to the remote machine
rsync -avh -e "ssh -i config/ssh_keys/htz-1" ../. $REMOTE:$HOME --delete --exclude '.run/config'

# Stop all the currently running processes
sh kill-all.sh

# Start the run.sh script on the remote machine
ssh -i config/ssh_keys/htz-1 $REMOTE 'sh '$HOME'/.run/run.sh'

# Start all the panel processes
sh start-all.sh
