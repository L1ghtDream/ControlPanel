#!/bin/bash

HOME=/home/ControlPanel
SCREEN_NAME_WEB=Web
SCREEN_NAME_NODE=Node
CURRENT_DIR="$(pwd)"

for i in 1 2 3 4; do
  echo "Sending configs to htz-$i"

  rsync -avh -e --ignore-existing "ssh -i $CURRENT_DIR/config/ssh_keys/htz-$i" config root@htz$i.original.gg:$HOME/.run/

  echo "Starting node on htz-$i"

  if [ "$i" = "1" ]; then
    ssh root@htz$i.original.gg -o StrictHostKeyChecking=no -i config/ssh_keys/htz-$i "screen -dmS $SCREEN_NAME_WEB"
    ssh root@htz$i.original.gg -o StrictHostKeyChecking=no -i config/ssh_keys/htz-$i "screen -S $SCREEN_NAME_WEB -X stuff \"cd $HOME/.run; java -jar Web.jar\n\""
  fi

  # Start the node
  ssh root@htz$i.original.gg -o StrictHostKeyChecking=no -i config/ssh_keys/htz-$i "screen -dmS $SCREEN_NAME_NODE"
  ssh root@htz$i.original.gg -o StrictHostKeyChecking=no -i config/ssh_keys/htz-$i "screen -S $SCREEN_NAME_NODE -X stuff \"cd  $HOME/.run; java -jar Node.jar\n\""

done
