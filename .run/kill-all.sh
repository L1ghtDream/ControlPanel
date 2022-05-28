#!/bin/bash

HOME=/home/ControlPanel
SCREEN_NAME_WEB=Web
SCREEN_NAME_NODE=Node
WEB_APP_PORT=13000


for i in 1 2 3 4 5; do
  echo "Killing node on htz-$i"

  # Killing the current version (if running)
  ssh root@htz$i.original.gg -o StrictHostKeyChecking=no -i config/ssh_keys/htz-$i "screen -S $SCREEN_NAME_NODE -X stuff \"^C\""
  sleep .5
  ssh root@htz$i.original.gg -o StrictHostKeyChecking=no -i config/ssh_keys/htz-$i "screen -S $SCREEN_NAME_NODE -X stuff \"^C\""
  sleep .5
  ssh root@htz$i.original.gg -o StrictHostKeyChecking=no -i config/ssh_keys/htz-$i "screen -XS $SCREEN_NAME_NODE kill"

  if [ "$i" = "1" ]; then
    ssh root@htz$i.original.gg -o StrictHostKeyChecking=no -i config/ssh_keys/htz-$i "screen -S $SCREEN_NAME_WEB -X stuff \"^C\""
    sleep .5
    ssh root@htz$i.original.gg -o StrictHostKeyChecking=no -i config/ssh_keys/htz-$i "screen -S $SCREEN_NAME_WEB -X stuff \"^C\""
    sleep .5
    ssh root@htz$i.original.gg -o StrictHostKeyChecking=no -i config/ssh_keys/htz-$i "screen -XS $SCREEN_NAME_WEB kill"
    sleep .5
    ssh root@htz$i.original.gg -o StrictHostKeyChecking=no -i config/ssh_keys/htz-$i "kill $(lsof -t -i:\"$WEB_APP_PORT\")"
  fi

done
