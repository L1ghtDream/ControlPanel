SCREEN_NAME_WEB=Web
HOME=/home/ControlPanel

# Kill
screen -S $SCREEN_NAME_WEB -X stuff \"^C\"
sleep .5
screen -S $SCREEN_NAME_WEB -X stuff \"^C\"
sleep .5
screen -XS $SCREEN_NAME_WEB kill
sleep .5
# shellcheck disable=SC2046
kill $(lsof -t -i:"$WEB_APP_PORT")

# Start
screen -dmS $SCREEN_NAME_WEB
screen -S $SCREEN_NAME_WEB -X stuff "cd $HOME/.run; java -jar Web.jar\n"