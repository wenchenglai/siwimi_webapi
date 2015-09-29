cd
sudo rm -rf siwimi_webapi
git clone https://github.com/wenchenglai/siwimi_webapi.git
cd siwimi_webapi
gradle wrapper
./gradlew build	
cd /usr/local/tomcat8/webapps
sudo /etc/init.d/tomcat8 stop
sleep 3
sudo rm siwimi*.war
sudo rm -rf siwimi*
sudo mv ~/siwimi_webapi/build/libs/siwimi-webapi-0.0.1.war .
sudo /etc/init.d/tomcat8 start
cd
sudo rm -rf siwimi_webapi
